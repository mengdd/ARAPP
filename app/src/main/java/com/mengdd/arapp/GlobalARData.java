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

import android.location.Location;
import android.location.LocationListener;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.baidu.location.BDLocation;
import com.mengdd.location.baidu.BaiduLocationHelper;
import com.mengdd.poi.ui.BasicMarker;
import com.mengdd.utils.AppConstants;
import com.mengdd.utils.Matrix;

/**
 * Abstract class which should be used to set global data.
 * 
 * The source of the codes: 1."android-augment-reality-framework" project link:
 * http://code.google.com/p/android-augment-reality-framework/
 * 
 * 
 * 2.The book: "Pro Android Augmented Reality"
 * http://www.apress.com/9781430239451 Official repository for Pro Android
 * Augmented Reality: https://github.com/RaghavSood/ProAndroidAugmentedReality
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 */
public abstract class GlobalARData {
    public static boolean portrait = true;
    public static int screenOrientation = 0;
    private static final Map<String, BasicMarker> markerList = new ConcurrentHashMap<String, BasicMarker>();
    private static final List<BasicMarker> cache = new CopyOnWriteArrayList<BasicMarker>();
    private static final AtomicBoolean dirty = new AtomicBoolean(false);
    private static final float[] locationArray = new float[3];

    /* defaulting to our place */
    public static final Location hardFix = new Location("ATL");
    static {
        hardFix.setLatitude(39.97603);
        hardFix.setLongitude(116.31757);
        hardFix.setAltitude(0);
    }

    /* defaulting to our place */
    public static final BDLocation hardFixBD = new BDLocation();
    static {
        // 海淀黄庄百度地图坐标116.324338,39.981877
        hardFixBD.setLatitude(39.981877);
        hardFixBD.setLongitude(116.324338);

    }

    private static final Object radiusLock = new Object();
    private static float radius = Float.valueOf(20);
    private static String zoomLevel = new String();
    private static final Object zoomProgressLock = new Object();
    private static int zoomProgress = 0;

    // rotation infomation
    private static Matrix rotationMatrix = new Matrix();
    private static final Object azimuthLock = new Object();
    private static float azimuth = 0;
    private static final Object rollLock = new Object();
    private static float roll = 0;
    private static final Object pitchLock = new Object();
    private static float pitch = 0;

    // we have two location fields, they are updated by different SDK
    private static Location currentGoogleLocation = hardFix;
    private static BDLocation currentBaiduLocation = hardFixBD;
    private static AMapLocation currentAutoNaviLocation = null;

    /**
     * Set the zoom level.
     * 
     * @param zoomLevel
     *            String representing the zoom level.
     */
    public static void setZoomLevel(String zoomLevel) {
        if (zoomLevel == null) {
            throw new IllegalArgumentException("zoomLevel == null!");
        }

        synchronized (GlobalARData.zoomLevel) {
            GlobalARData.zoomLevel = zoomLevel;
        }
    }

    /**
     * Get the zoom level.
     * 
     * @return String representing the zoom level.
     */
    public static String getZoomLevel() {
        synchronized (GlobalARData.zoomLevel) {
            return GlobalARData.zoomLevel;
        }
    }

    /**
     * Set the zoom progress.
     * 
     * @param zoomProgress
     *            int representing the zoom progress.
     */
    public static void setZoomProgress(int zoomProgress) {
        synchronized (GlobalARData.zoomProgressLock) {
            if (GlobalARData.zoomProgress != zoomProgress) {
                GlobalARData.zoomProgress = zoomProgress;
                if (dirty.compareAndSet(false, true)) {
                    Log.v(AppConstants.LOG_TAG, "Setting DIRTY flag!");
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
    public static int getZoomProgress() {
        synchronized (GlobalARData.zoomProgressLock) {
            return GlobalARData.zoomProgress;
        }
    }

    /**
     * Set the radius of the radar screen.
     * 
     * @param radius
     *            float representing the radar screen.
     */
    public static void setRadius(float radius) {
        synchronized (GlobalARData.radiusLock) {
            GlobalARData.radius = radius;
        }
    }

    /**
     * Get the radius (in KM) of the radar screen.
     * 
     * @return float representing the radar screen.
     */
    public static float getRadius() {
        synchronized (GlobalARData.radiusLock) {
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
    public static void setCurrentGoogleLocation(Location currentLocation)
            throws IllegalArgumentException {
        if (currentLocation == null) {
            throw new IllegalArgumentException("currentLocaiont is null!");
        }

        synchronized (currentLocation) {
            GlobalARData.currentGoogleLocation = currentLocation;
        }
        onLocationChanged(GlobalARData.currentGoogleLocation);
    }

    public static void setCurrentBaiduLocation(BDLocation currentLocation)
            throws IllegalArgumentException {
        if (currentLocation == null) {
            throw new IllegalArgumentException("currentLocaiont is null!");
        }

        synchronized (currentLocation) {
            GlobalARData.currentBaiduLocation = currentLocation;
        }
        Location tempLocation = BaiduLocationHelper
                .convertBD2AndroidLocation(currentLocation);
        onLocationChanged(tempLocation);
    }

    public static void setCurrentAutoNaviLocation(AMapLocation currentLocation)
            throws IllegalArgumentException {
        if (currentLocation == null) {
            throw new IllegalArgumentException("currentLocaiont is null!");
        }

        synchronized (currentLocation) {
            GlobalARData.currentAutoNaviLocation = currentLocation;
        }

        // 高德地图的AMapLocation extends Location from Android SDK
        // 所以不需要转换
        onLocationChanged(currentLocation);
    }

    // Observers
    private static List<LocationListener> mLocationListeners;

    /**
     * Add Location changed listener to this Observable object. The lister's
     * onLocationChanged() will be called every time when current location data
     * changed.
     * 
     * @param listener
     * @return
     */
    public static boolean addLocationListener(LocationListener listener) {
        boolean result = false;
        if (null == listener) {
            throw new IllegalArgumentException("lister == null");
        }

        if (null == mLocationListeners) {
            mLocationListeners = new ArrayList<LocationListener>();
        }

        if (!mLocationListeners.contains(listener)) {
            result = mLocationListeners.add(listener);
        }

        return result;
    }

    /**
     * Remove the listener from the list of listeners. The listener will not be
     * notified any more if location data changed.
     * 
     * @param listener
     * @return
     */
    public static boolean removeLocationListener(LocationListener listener) {
        boolean result = false;
        if (null == listener) {
            throw new IllegalArgumentException("lister == null");
        }

        if (null == mLocationListeners) {
            return result;
        }

        result = mLocationListeners.remove(listener);

        return result;

    }

    private static void onLocationChanged(Location location) {
        Log.d(AppConstants.LOG_TAG, "New location, updating markers. location="
                + location.toString());

        // the listers do their corresponding work
        if (null != mLocationListeners) {
            for (LocationListener listener : mLocationListeners) {
                if (null == listener) {
                    throw new NullPointerException(
                            "LocationListener is null ! Please check if you remove listener from GlobalARData before you destroy it.");
                }
                listener.onLocationChanged(location);
            }
        }

        // keep temporary
        // *****************************************************************
        for (BasicMarker ma : markerList.values()) {
            ma.updateRelativePosition(GlobalARData.getCurrentGoogleLocation(),
                    GlobalARData.getCurrentBaiduLocation());
        }

        if (dirty.compareAndSet(false, true)) {
            Log.v(AppConstants.LOG_TAG, "Setting DIRTY flag!");
            cache.clear();
        }
    }

    /**
     * Get the current Google Location.
     * 
     * @return Location representing the current location.
     */
    public static Location getCurrentGoogleLocation() {
        synchronized (GlobalARData.currentGoogleLocation) {
            return GlobalARData.currentGoogleLocation;
        }
    }

    /**
     * Get the current Baidu Location.
     * 
     * @return Location representing the current location.
     */
    public static BDLocation getCurrentBaiduLocation() {
        synchronized (GlobalARData.currentBaiduLocation) {
            return GlobalARData.currentBaiduLocation;
        }
    }

    /**
     * Set the rotation matrix.
     * 
     * @param rotationMatrix
     *            Matrix to use for rotation.
     */
    public static void setRotationMatrix(Matrix rotationMatrix) {
        synchronized (GlobalARData.rotationMatrix) {
            GlobalARData.rotationMatrix = rotationMatrix;
        }
    }

    /**
     * Get the rotation matrix.
     * 
     * @return Matrix representing the rotation matrix.
     */
    public static Matrix getRotationMatrix() {
        synchronized (GlobalARData.rotationMatrix) {
            // Log.i(AppConstants.LOG_TAG, "rotationMatrix: " +
            // rotationMatrix.toString());
            return rotationMatrix;
        }
    }

    public static void clearMarkers() {
        markerList.clear();
    }

    /**
     * Add a List of Markers to our Collection.
     * 
     * @param markers
     *            List of Markers to add.
     */
    public static void addMarkers(Collection<BasicMarker> markers) {
        if (null == markers) {
            throw new IllegalArgumentException(
                    "the Collection of markers is null!");
        }

        if (markers.size() <= 0) {
            return;
        }

        Log.d(AppConstants.LOG_TAG,
                "New markers, updating markers. new markers="
                        + markers.toString());
        for (BasicMarker marker : markers) {
            if (!markerList.containsKey(marker.getName())) {
                marker.updateRelativePosition(
                        GlobalARData.getCurrentGoogleLocation(),
                        GlobalARData.getCurrentBaiduLocation());
                markerList.put(marker.getName(), marker);
            }
        }

        if (dirty.compareAndSet(false, true)) {
            Log.v(AppConstants.LOG_TAG, "Setting DIRTY flag!");
            cache.clear();
        }
    }

    public static void logMarkers() {
        List<BasicMarker> markers = getMarkers();

        if (null != markers) {
            int i = 1;
            for (BasicMarker marker : markers) {
                Log.i(AppConstants.LOG_TAG,
                        "The " + i + " th marker:" + marker.toString());
                i++;

            }
        }
    }

    /**
     * Get the Markers collection.
     * 
     * @return Collection of Markers.
     */
    public static List<BasicMarker> getMarkers() {
        // If markers we added, zero out the altitude to recompute the collision
        // detection
        if (dirty.compareAndSet(true, false)) {
            Log.v(AppConstants.LOG_TAG,
                    "DIRTY flag found, resetting all marker heights to zero.");
            for (BasicMarker ma : markerList.values()) {
                ma.getLocationVector().get(locationArray);
                locationArray[1] = ma.getInitialY();
                ma.getLocationVector().set(locationArray);
            }

            Log.v(AppConstants.LOG_TAG, "Populating the cache.");
            List<BasicMarker> copy = new ArrayList<BasicMarker>();
            copy.addAll(markerList.values());
            Collections.sort(copy, comparator);
            // The cache should be sorted from closest to farthest marker.
            cache.clear();
            cache.addAll(copy);
        }
        return Collections.unmodifiableList(cache);
    }

    private static final Comparator<BasicMarker> comparator = new Comparator<BasicMarker>() {

        /**
         * {@inheritDoc}
         */
        @Override
        public int compare(BasicMarker arg0, BasicMarker arg1) {
            return Double.compare(arg0.getDistance(), arg1.getDistance());
        }
    };

    /**
     * Set the current Azimuth.
     * 
     * @param azimuth
     *            float representing the azimuth.
     */
    public static void setAzimuth(float azimuth) {
        synchronized (azimuthLock) {
            GlobalARData.azimuth = azimuth;
        }
    }

    /**
     * Get the current Azimuth.
     * 
     * @return azimuth float representing the azimuth.
     */
    public static float getAzimuth() {
        synchronized (azimuthLock) {
            return GlobalARData.azimuth;
        }
    }

    /**
     * Set the current Roll.
     * 
     * @param roll
     *            float representing the roll.
     */
    public static void setRoll(float roll) {
        synchronized (rollLock) {
            GlobalARData.roll = roll;
        }
    }

    /**
     * Get the current Roll.
     * 
     * @return roll float representing the roll.
     */
    public static float getRoll() {
        synchronized (rollLock) {
            return GlobalARData.roll;
        }
    }

    /**
     * Set the current Pitch.
     * 
     * @param pitch
     *            float representing the pitch.
     */
    public static void setPitch(float pitch) {
        synchronized (pitchLock) {
            GlobalARData.pitch = pitch;
        }
    }

    /**
     * Get the current Pitch.
     * 
     * @return pitch float representing the pitch.
     */
    public static float getPitch() {
        synchronized (pitchLock) {
            return GlobalARData.pitch;
        }
    }
}
