package com.mengdd.location.google;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.mengdd.arapp.GlobalARData;
import com.mengdd.location.LocationModel;
import com.mengdd.utils.AppConstants;

/**
 * Location ViewModel for finding current location information. This class
 * doesn't have any UI elements and use both gps and network as its location
 * providers
 * 
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 * 
 */
public class GoogleLocationModel extends LocationModel implements
        LocationListener {
    private LocationManager mLocationManager;

    private static final int MIN_TIME = 3 * 1000;
    private static final int MIN_DISTANCE = 10;

    private long mLastFixTime = 0;
    private long mCurrentFixTime = 0;

    public long getLastFixTime() {
        return mLastFixTime;
    }

    public GoogleLocationModel(Activity activity) {
        super(activity);
    }

    @Override
    public void registerLocationUpdates() {
        mLastFixTime = mCurrentFixTime = SystemClock.uptimeMillis();

        if (null == mLocationManager) {
            mLocationManager = (LocationManager) mActivity
                    .getSystemService(Context.LOCATION_SERVICE);
        }

        if (null != mLocationManager) {

            // register 2 location providers: GPS and network
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);

            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE,
                    this);
            Log.i(AppConstants.LOG_TAG,
                    "GoogleLocationModel: registerLocationUpdates successfully!");

        }

    }

    @Override
    public void unregisterLocationUpdates() {
        if (null != mLocationManager) {
            mLocationManager.removeUpdates(this);
            mLocationManager = null;

        }

    }

    private LocationListener mLocationListener = null;

    public void setLocationListener(LocationListener listener) {
        mLocationListener = listener;
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentFixTime = SystemClock.uptimeMillis();
        Log.i(AppConstants.LOG_TAG, "GoogleLocationModel: onLocationChanged: "
                + location);
        boolean isNewBetter = isBetterLocation(location, mCurrentLocation);
        if (isNewBetter) {
            mCurrentLocation = location;

            // set the location to a global class so other class can get the
            // updated value
            GlobalARData.setCurrentGoogleLocation(location);
        }

        mLastFixTime = mCurrentFixTime;

        // 基类的Listener
        if (null != mBasicLocationChangedListener) {
            mBasicLocationChangedListener.onLocationChanged(location);
        }

        // 本类的
        if (null != mLocationListener) {
            mLocationListener.onLocationChanged(location);
        }

    }

    private static final int TWO_MINUTES = 1000 * 60 * 2;

    /**
     * Determines whether one Location reading is better than the current
     * Location fix The codes are adapted from:
     * http://developer.android.com/guide/topics/location/strategies.html#
     * BestEstimate
     * 
     * @param newLocation
     *            The new Location that you want to evaluate
     * @param currentBestLocation
     *            The current Location fix, to which you want to compare the new
     *            one
     */
    protected boolean isBetterLocation(Location newLocation,
            Location currentBestLocation) {
        if (null == newLocation) {
            // a null location is not better than any one
            return false;
        }
        if (null == currentBestLocation) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = newLocation.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use
        // the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be
            // worse
        }
        else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (newLocation.getAccuracy() - currentBestLocation
                .getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(newLocation.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and
        // accuracy
        if (isMoreAccurate) {
            return true;
        }
        else if (isNewer && !isLessAccurate) {
            return true;
        }
        else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two location providers are the same */
    private boolean isSameProvider(String provider, String provider2) {
        if (null != provider && provider.equals(provider2)) {
            return true;
        }
        return false;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

}
