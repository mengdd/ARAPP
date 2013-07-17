package com.mengdd.arapp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import com.mengdd.ar.ui.Marker;
import com.mengdd.utils.Matrix;

import android.location.Location;
import android.location.LocationListener;
import android.util.Log;

/**
 * Abstract class which should be used to set global data.
 * 
 * The source of the codes:
 * 1."android-augment-reality-framework"
 * project link: http://code.google.com/p/android-augment-reality-framework/
 * 
 * 
 * 2.The book: "Pro Android Augmented Reality"
 * http://www.apress.com/9781430239451
 * Official repository for Pro Android Augmented Reality:
 * https://github.com/RaghavSood/ProAndroidAugmentedReality
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 */
public abstract class GlobalARData
{

	private static final String TAG = "ARData";
	private static final Map<String, Marker> markerList = new ConcurrentHashMap<String, Marker>();
	private static final List<Marker> cache = new CopyOnWriteArrayList<Marker>();
	private static final AtomicBoolean dirty = new AtomicBoolean(false);
	private static final float[] locationArray = new float[3];

	public static boolean portrait = false;

	public static boolean isPortrait()
	{
		return portrait;
	}

	public static void setPortrait(boolean portrait)
	{
		GlobalARData.portrait = portrait;
	}

	/* defaulting to our place */
	public static final Location hardFix = new Location("ATL");
	static
	{
		hardFix.setLatitude(39.931261);
		hardFix.setLongitude(-75.051267);
		hardFix.setAltitude(1);
	}

	private static final Object radiusLock = new Object();
	private static float radius = Float.valueOf(20);
	private static String zoomLevel = new String();
	private static final Object zoomProgressLock = new Object();
	private static int zoomProgress = 0;
	private static Location currentLocation = hardFix;
	private static Matrix rotationMatrix = new Matrix();
	private static final Object azimuthLock = new Object();
	private static float azimuth = 0;
	private static final Object rollLock = new Object();
	private static float roll = 0;

	/**
	 * Set the zoom level.
	 * 
	 * @param zoomLevel
	 *            String representing the zoom level.
	 */
	public static void setZoomLevel(String zoomLevel)
	{
		if (zoomLevel == null)
		{
			throw new IllegalArgumentException("zoomLevel == null!");
		}
			

		synchronized (GlobalARData.zoomLevel)
		{
			GlobalARData.zoomLevel = zoomLevel;
		}
	}

	/**
	 * Get the zoom level.
	 * 
	 * @return String representing the zoom level.
	 */
	public static String getZoomLevel()
	{
		synchronized (GlobalARData.zoomLevel)
		{
			return GlobalARData.zoomLevel;
		}
	}

	/**
	 * Set the zoom progress.
	 * 
	 * @param zoomProgress
	 *            int representing the zoom progress.
	 */
	public static void setZoomProgress(int zoomProgress)
	{
		synchronized (GlobalARData.zoomProgressLock)
		{
			if (GlobalARData.zoomProgress != zoomProgress)
			{
				GlobalARData.zoomProgress = zoomProgress;
				if (dirty.compareAndSet(false, true))
				{
					Log.v(TAG, "Setting DIRTY flag!");
					cache.clear();
				}
			}
		}
	}

	/**
	 * Get the zoom progress.
	 * 
	 * @return int representing the zoom progress.
	 */
	public static int getZoomProgress()
	{
		synchronized (GlobalARData.zoomProgressLock)
		{
			return GlobalARData.zoomProgress;
		}
	}

	/**
	 * Set the radius of the radar screen.
	 * 
	 * @param radius
	 *            float representing the radar screen.
	 */
	public static void setRadius(float radius)
	{
		synchronized (GlobalARData.radiusLock)
		{
			GlobalARData.radius = radius;
		}
	}

	/**
	 * Get the radius (in KM) of the radar screen.
	 * 
	 * @return float representing the radar screen.
	 */
	public static float getRadius()
	{
		synchronized (GlobalARData.radiusLock)
		{
			return GlobalARData.radius;
		}
	}

	/**
	 * Set the current location.
	 * 
	 * @param currentLocation
	 *            Location to set.
	 * @throws IllegalArgumentException
	 */
	public static void setCurrentLocation(Location currentLocation)
			throws IllegalArgumentException
	{
		if (currentLocation == null)
		{
			throw new IllegalArgumentException("currentLocaiont is null!");
		}

		Log.d(TAG, "current location. location=" + currentLocation.toString());
		synchronized (currentLocation)
		{
			GlobalARData.currentLocation = currentLocation;
		}
		onLocationChanged(currentLocation);
	}

	// Observers
	private static List<LocationListener> mLocationListeners;

	/**
	 * Add Location changed listener to this Observable object.
	 * The lister's onLocationChanged() will be called every time when current
	 * location data changed.
	 * 
	 * @param listener
	 * @return
	 */
	public static boolean addLocationListener(LocationListener listener)
	{
		boolean result = false;
		if (null == listener)
		{
			throw new IllegalArgumentException("lister == null");
		}

		if (null == mLocationListeners)
		{
			mLocationListeners = new ArrayList<LocationListener>();
		}

		result = mLocationListeners.add(listener);

		return result;
	}

	/**
	 * Remove the listener from the list of listeners.
	 * The listener will not be notified any more if location data changed.
	 * 
	 * @param listener
	 * @return
	 */
	public static boolean removeLocationListener(LocationListener listener)
	{
		boolean result = false;
		if (null == listener)
		{
			throw new IllegalArgumentException("lister == null");
		}

		if (null == mLocationListeners)
		{
			return result;
		}

		result = mLocationListeners.remove(listener);

		return result;

	}

	private static void onLocationChanged(Location location)
	{
		Log.d(TAG,
				"New location, updating markers. location="
						+ location.toString());

		// the listers do their corresponding work
		if (null != mLocationListeners)
		{
			for (LocationListener listener : mLocationListeners)
			{
				listener.onLocationChanged(location);
			}
		}

		
		//keep temporary *****************************************************************
		for (Marker ma : markerList.values())
		{
			ma.calcRelativePosition(location);
		}

		if (dirty.compareAndSet(false, true))
		{
			Log.v(TAG, "Setting DIRTY flag!");
			cache.clear();
		}
	}

	/**
	 * Get the current Location.
	 * 
	 * @return Location representing the current location.
	 */
	public static Location getCurrentLocation()
	{
		synchronized (GlobalARData.currentLocation)
		{
			return GlobalARData.currentLocation;
		}
	}

	/**
	 * Set the rotation matrix.
	 * 
	 * @param rotationMatrix
	 *            Matrix to use for rotation.
	 */
	public static void setRotationMatrix(Matrix rotationMatrix)
	{
		synchronized (GlobalARData.rotationMatrix)
		{
			GlobalARData.rotationMatrix = rotationMatrix;
		}
	}

	/**
	 * Get the rotation matrix.
	 * 
	 * @return Matrix representing the rotation matrix.
	 */
	public static Matrix getRotationMatrix()
	{
		synchronized (GlobalARData.rotationMatrix)
		{
			return rotationMatrix;
		}
	}

	/**
	 * Add a List of Markers to our Collection.
	 * 
	 * @param markers
	 *            List of Markers to add.
	 */
	public static void addMarkers(Collection<Marker> markers)
	{
		if (markers == null)
			throw new NullPointerException();

		if (markers.size() <= 0)
			return;

		Log.d(TAG,
				"New markers, updating markers. new markers="
						+ markers.toString());
		for (Marker marker : markers)
		{
			if (!markerList.containsKey(marker.getName()))
			{
				marker.calcRelativePosition(GlobalARData.getCurrentLocation());
				markerList.put(marker.getName(), marker);
			}
		}

		if (dirty.compareAndSet(false, true))
		{
			Log.v(TAG, "Setting DIRTY flag!");
			cache.clear();
		}
	}

	/**
	 * Get the Markers collection.
	 * 
	 * @return Collection of Markers.
	 */
	public static List<Marker> getMarkers()
	{
		// If markers we added, zero out the altitude to recompute the collision
		// detection
		if (dirty.compareAndSet(true, false))
		{
			Log.v(TAG,
					"DIRTY flag found, resetting all marker heights to zero.");
			for (Marker ma : markerList.values())
			{
				ma.getLocation().get(locationArray);
				locationArray[1] = ma.getInitialY();
				ma.getLocation().set(locationArray);
			}

			Log.v(TAG, "Populating the cache.");
			List<Marker> copy = new ArrayList<Marker>();
			copy.addAll(markerList.values());
			Collections.sort(copy, comparator);
			// The cache should be sorted from closest to farthest marker.
			cache.clear();
			cache.addAll(copy);
		}
		return Collections.unmodifiableList(cache);
	}

	private static final Comparator<Marker> comparator = new Comparator<Marker>()
	{

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compare(Marker arg0, Marker arg1)
		{
			return Double.compare(arg0.getDistance(), arg1.getDistance());
		}
	};

	/**
	 * Set the current Azimuth.
	 * 
	 * @param azimuth
	 *            float representing the azimuth.
	 */
	public static void setAzimuth(float azimuth)
	{
		synchronized (azimuthLock)
		{
			GlobalARData.azimuth = azimuth;
		}
	}

	/**
	 * Get the current Azimuth.
	 * 
	 * @return azimuth float representing the azimuth.
	 */
	public static float getAzimuth()
	{
		synchronized (azimuthLock)
		{
			return GlobalARData.azimuth;
		}
	}

	/**
	 * Set the current Roll.
	 * 
	 * @param roll
	 *            float representing the roll.
	 */
	public static void setRoll(float roll)
	{
		synchronized (rollLock)
		{
			GlobalARData.roll = roll;
		}
	}

	/**
	 * Get the current Roll.
	 * 
	 * @return roll float representing the roll.
	 */
	public static float getRoll()
	{
		synchronized (rollLock)
		{
			return GlobalARData.roll;
		}
	}
}
