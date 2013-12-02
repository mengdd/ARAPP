package com.mengdd.poi.data;

/**
 * This class is used to represent a physical locations which have a latitude,
 * longitude, and alitude.
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
public class PhysicalLocation {

    private double latitude = 0.0;
    private double longitude = 0.0;
    private double altitude = 0.0;

    public PhysicalLocation() {
    }

    public PhysicalLocation(PhysicalLocation pl) {
        if (null == pl) {
            throw new IllegalArgumentException(
                    "physical location input is null!");
        }

        set(pl.latitude, pl.longitude, pl.altitude);
    }

    /**
     * Set this objects parameters. This should be used instead of creating new
     * objects.
     * 
     * @param latitude
     *            Latitude of the location in decimal format (example
     *            39.931269).
     * @param longitude
     *            Longitude of the location in decimal format (example
     *            -75.051261).
     * @param altitude
     *            Altitude of the location in meters (>0 is above sea level).
     */
    public void set(double latitude, double longitude, double altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    /**
     * Set the Latitude of the PhysicalLocation.
     * 
     * @param latitude
     *            Latitude of the location in decimal format (example
     *            39.931269).
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Get the Latitude of the PhysicalLocation.
     * 
     * @return double representation the latitude of the location in decimal
     *         format (example 39.931269).
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Set the Longitude of the PhysicalLocation.
     * 
     * @param longitude
     *            Longitude of the location in decimal format (example
     *            -75.051261).
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Get the Longitude of the PhysicalLocation.
     * 
     * @return double representation the longitude of the location in decimal
     *         format (example -75.051261).
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Set the Altitude of the PhysicalLocation.
     * 
     * @param altitude
     *            Altitude of the location in meters (>0 is above sea level).
     */
    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    /**
     * Get the Altitude of the PhysicalLocation.
     * 
     * @return double representation the altitude of the location in meters (>0
     *         is above sea level).
     */
    public double getAltitude() {
        return altitude;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "(lat=" + latitude + ", lng=" + longitude + ", alt=" + altitude
                + ")";
    }
}
