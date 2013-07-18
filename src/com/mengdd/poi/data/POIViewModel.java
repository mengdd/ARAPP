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

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.mengdd.ar.ui.Marker;
import com.mengdd.arapp.GlobalARData;
import com.mengdd.utils.AppConstants;

public class POIViewModel implements LocationListener
{
	public POIViewModel()
	{
		
	}
	private static final String locale = Locale.getDefault().getLanguage();
	private static final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(
			1);
	private static final ThreadPoolExecutor exeService = new ThreadPoolExecutor(
			1, 1, 20, TimeUnit.SECONDS, queue);
	private static final Map<String, NetworkDataSource> sources = new ConcurrentHashMap<String, NetworkDataSource>();

	/**
	 * Update POI data according to the location information
	 * 
	 * @param lat
	 * @param lon
	 * @param alt
	 */
	private void updateData(final double lat, final double lon, final double alt)
	{
		try
		{
			exeService.execute(new Runnable()
			{

				@Override
				public void run()
				{
					for (NetworkDataSource source : sources.values())
					{
						download(source, lat, lon, alt);
					}
				}
			});
		}
		catch (RejectedExecutionException rej)
		{
			Log.w(AppConstants.LOG_TAG,
					"Not running new download Runnable, queue is full.");
		}
		catch (Exception e)
		{
			Log.e(AppConstants.LOG_TAG, "Exception running download Runnable.",
					e);
		}
	}

	/**
	 * Use the NetworkDataSouce and some location info to download.
	 * Get detailed location information and add Markers to GlobalARData.
	 * 
	 * @param source
	 * @param lat
	 * @param lon
	 * @param alt
	 * @return true if Markers are successfully added.
	 */
	private static boolean download(NetworkDataSource source, double lat,
			double lon, double alt)
	{
		if (null == source)
		{
			return false;
		}

		String url = null;
		try
		{
			url = source.createRequestURL(lat, lon, alt,
					GlobalARData.getRadius(), locale);
		}
		catch (NullPointerException e)
		{
			return false;
		}

		List<Marker> markers = null;
		try
		{
			markers = source.parse(url);
		}
		catch (NullPointerException e)
		{
			return false;
		}

		GlobalARData.addMarkers(markers);
		return true;
	}

	@Override
	public void onLocationChanged(Location location)
	{
		updateData(location.getLatitude(), location.getLongitude(),
				location.getAltitude());

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{

	}

	@Override
	public void onProviderEnabled(String provider)
	{

	}

	@Override
	public void onProviderDisabled(String provider)
	{

	}

}
