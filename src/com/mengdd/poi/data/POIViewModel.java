package com.mengdd.poi.data;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mengdd.arapp.GlobalARData;
import com.mengdd.components.ViewModel;
import com.mengdd.poi.ui.BasicMarker;
import com.mengdd.poi.ui.RadarZoomController.OnRadarZoomChangedListener;
import com.mengdd.utils.AppConstants;

public class POIViewModel extends ViewModel implements LocationListener,
        OnRadarZoomChangedListener {

    private static final String locale = Locale.getDefault().getLanguage();
    private static final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(
            1);
    private static final ThreadPoolExecutor exeService = new ThreadPoolExecutor(
            1, 1, 20, TimeUnit.SECONDS, queue);
    private static final Map<String, NetworkDataSource> sources = new ConcurrentHashMap<String, NetworkDataSource>();

    public POIViewModel(Activity activity) {
        super(activity);

        NetworkDataSource wikipedia = new WikipediaDataSource(
                mActivity.getResources());
        sources.put("wiki", wikipedia);

        NetworkDataSource google = new GooglePlacesDataSource(
                mActivity.getResources());
        sources.put("google", google);
    }

    /**
     * Update POI data according to the location information
     * 
     * @param lat
     * @param lon
     * @param alt
     */
    public void updateData(final double lat, final double lon, final double alt) {
        Log.i(AppConstants.LOG_TAG, "POIViewModel updateData");
        try {
            exeService.execute(new Runnable() {

                @Override
                public void run() {
                    for (NetworkDataSource source : sources.values()) {
                        download(source, lat, lon, alt);
                    }
                }
            });
        }
        catch (RejectedExecutionException rej) {
            Log.w(AppConstants.LOG_TAG,
                    "Not running new download Runnable, queue is full.");
        }
        catch (Exception e) {
            Log.e(AppConstants.LOG_TAG, "Exception running download Runnable.",
                    e);
        }
    }

    /**
     * Use the NetworkDataSouce and some location info to download. Get detailed
     * location information and add Markers to GlobalARData.
     * 
     * @param source
     * @param lat
     * @param lon
     * @param alt
     * @return true if Markers are successfully added.
     */
    private static boolean download(NetworkDataSource source, double lat,
            double lon, double alt) {
        if (null == source) {
            return false;
        }

        String url = null;
        try {
            url = source.createRequestURL(lat, lon, alt,
                    GlobalARData.getRadius(), locale);
        }
        catch (NullPointerException e) {
            Log.e(AppConstants.LOG_TAG, "NullPointerException");
            return false;
        }

        List<BasicMarker> markers = null;
        try {
            markers = source.parse(url);
        }
        catch (NullPointerException e) {
            Log.e(AppConstants.LOG_TAG, "NullPointerException");
            return false;
        }

        GlobalARData.addMarkers(markers);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        updateData(location.getLatitude(), location.getLongitude(),
                location.getAltitude());

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public View getView() {
        return null;
    }

    @Override
    public void onZoomChanged() {
        Location last = GlobalARData.getCurrentGoogleLocation();
        updateData(last.getLatitude(), last.getLongitude(), last.getAltitude());

    }

}
