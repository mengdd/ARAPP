package com.mengdd.poi.ui;

import android.graphics.Bitmap;
import android.location.Location;

import com.baidu.location.BDLocation;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.mengdd.poi.data.PhysicalLocation;
import com.mengdd.utils.MathUtils;

public class GoogleMarker extends BasicMarker {
    // Marker's physical location (Lat, Lon, Alt)
    private final PhysicalLocation mPhysicalLocation = new PhysicalLocation();

    public GoogleMarker(String name, int color, Bitmap icon, double latitude,
            double longitude, double altitude) {

        mMarkerType = MarkerType.GoogleMarker;
        set(name, color, icon, latitude, longitude, altitude);
    }

    /**
     * Set the objects parameters. This should be used instead of creating new
     * objects. Note: this might set some values to the initial state
     * 
     * @param name
     *            String representing the Marker.
     * @param latitude
     *            Latitude of the Marker in decimal format (example 39.931269).
     * @param longitude
     *            Longitude of the Marker in decimal format (example
     *            -75.051261).
     * @param altitude
     *            Altitude of the Marker in meters (>0 is above sea level).
     * @param color
     *            Color of the Marker.
     */
    public synchronized void set(String name, int color, Bitmap icon,
            double latitude, double longitude, double altitude) {
        if (null == name) {
            throw new IllegalArgumentException("name is null!");
        }

        // some fields are initialized in super class
        super.set(name, color, icon);

        this.mPhysicalLocation.set(latitude, longitude, altitude);

        if (altitude == 0.0d) {
            this.noAltitude = true;
        }
        else {
            this.noAltitude = false;
        }
    }

    @Override
    protected synchronized void calcRelativePosition(Location origLocation,
            BDLocation origBDLocation) {
        if (null == origLocation) {
            throw new IllegalArgumentException("location is null!");
        }

        // noAltitude means that the elevation of the POI is not known
        // and should be set to the users GPS altitude
        if (noAltitude) {
            mPhysicalLocation.setAltitude(origLocation.getAltitude());
        }

        // Compute the relative position vector from user position to POI
        // location
        mLocationVector = MathUtils.convLocationToVector(origLocation,
                mPhysicalLocation);
        this.initialY = mLocationVector.getY();

    }

    @Override
    protected synchronized void updateDistance(Location origLocation,
            BDLocation origBDLocation) {
        if (null == origLocation) {
            throw new IllegalArgumentException("location is null!");
        }
        float[] distanceArray = new float[1];
        Location.distanceBetween(mPhysicalLocation.getLatitude(),
                mPhysicalLocation.getLongitude(), origLocation.getLatitude(),
                origLocation.getLongitude(), distanceArray);
        mDistance = distanceArray[0];
    }

    @Override
    public String toString() {
        return "Marker [mName=" + mName + ", mPhysicalLocation="
                + mPhysicalLocation + ", mLocationVector=" + mLocationVector
                + ", mScreenVector=" + mScreenVector + ", mDistance="
                + mDistance + ", initialY=" + initialY + ", isOnRadar="
                + isOnRadar + ", isInView=" + isInView + ", noAltitude="
                + noAltitude + "]";
    }
}
