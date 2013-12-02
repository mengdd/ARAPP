package com.mengdd.utils;

import android.location.Location;
import android.util.Log;

import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.mengdd.arapp.GlobalARData;
import com.mengdd.poi.data.PhysicalLocation;

/**
 * A static class used to calculate azimuth, pitch, and roll given a rotation
 * matrix.
 * 
 * This file was adapted from Mixare <http://www.mixare.org/>
 * 
 * The source of the codes: 1."android-augment-reality-framework" project link:
 * http://code.google.com/p/android-augment-reality-framework/
 * 
 * 2.The book: "Pro Android Augmented Reality"
 * http://www.apress.com/9781430239451 Official repository for Pro Android
 * Augmented Reality: https://github.com/RaghavSood/ProAndroidAugmentedReality
 * 
 * @author Daniele Gobbetti <info@mixare.org>
 * @author Justin Wetherell <phishman3579@gmail.com>
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 */
public class MathUtils {

    /**
     * Get angle in degrees between two points.
     * 
     * @param center_x
     *            Lesser point's X
     * @param center_y
     *            Lesser point's Y
     * @param post_x
     *            Greater point's X
     * @param post_y
     *            Greater point's Y
     * @return Angle in degrees
     */
    public static final float getAngle(float center_x, float center_y,
            float post_x, float post_y) {
        float delta_x = post_x - center_x;
        float delta_y = post_y - center_y;
        return (float) (Math.atan2(delta_y, delta_x) * 180 / Math.PI);
    }

    /**
     * Project point from the origin Vector to the projected Vector.
     * 
     * @param orgPoint
     *            Vector representing the origin.
     * @param prjPoint
     *            Vector representing the projected point.
     * @param distance
     *            camera distance
     * @param width
     *            camera width
     * @param height
     *            camera height
     * @param addX
     *            Add X to the projected point.
     * @param addY
     *            Add Y to the projected point.
     */
    public static void projectPoint(Vector orgPoint, Vector prjPoint,
            float distance, int width, int height, float addX, float addY) {
        float[] originalV = new float[3];
        float[] projectV = new float[3];
        orgPoint.get(originalV);

        projectV[0] = (distance * originalV[0] / -originalV[2]);
        projectV[1] = (distance * originalV[1] / -originalV[2]);
        projectV[2] = (originalV[2]);
        projectV[0] = (projectV[0] + addX + width / 2);
        projectV[1] = (-projectV[1] + addY + height / 2);

        prjPoint.set(projectV);
    }

    /**
     * Converts a Location relative to original location to a Vector Vector X
     * stands for distance in Longitude,in meters. Vector Y stands for distance
     * in Altitude,in meters. Vector Z stands for distance in Latitude,in
     * meters.
     * 
     * @param origiLocation
     *            the location as original point
     * @param destLocation
     *            the location as destination
     * @return
     */
    public static synchronized Vector convLocationToVector(
            Location origiLocation, PhysicalLocation destLocation) {
        if (origiLocation == null || destLocation == null) {
            throw new IllegalArgumentException("Location is null");
        }

        Vector vector = new Vector();
        float[] x = new float[1];
        double y = 0.0d;
        float[] z = new float[1];

        // store latitude distance in z
        Location.distanceBetween(origiLocation.getLatitude(),
                origiLocation.getLongitude(), destLocation.getLatitude(),
                origiLocation.getLongitude(), z);

        // store longitude distance in x
        Location.distanceBetween(origiLocation.getLatitude(),
                origiLocation.getLongitude(), origiLocation.getLatitude(),
                destLocation.getLongitude(), x);

        y = destLocation.getAltitude() - origiLocation.getAltitude();

        if (origiLocation.getLatitude() < destLocation.getLatitude()) {
            z[0] *= -1;
        }
        if (origiLocation.getLongitude() > destLocation.getLongitude()) {
            x[0] *= -1;
        }

        vector.set(x[0], (float) y, z[0]);

        return vector;
    }

    /**
     * Converts a GeoPoint relative to original GeoPoint to a Vector Vector X
     * stands for distance in Longitude,in meters. Vector Y stands for distance
     * in Altitude,in meters. (Because GeoPoint with no information about
     * Altitude, so this value should always be zero) Vector Z stands for
     * distance in Latitude,in meters.
     * 
     * 
     * @param origiPoint
     * @param destPoint
     * @return
     */
    public static synchronized Vector convGeoPointToVector(GeoPoint origiPoint,
            GeoPoint destPoint) {
        if (origiPoint == null || destPoint == null) {
            throw new IllegalArgumentException("Location is null");
        }

        Vector vector = new Vector();
        double x;
        double y = 0.0d;
        double z;

        // store latitude distance in z

        z = DistanceUtil.getDistance(
                origiPoint,
                new GeoPoint(destPoint.getLatitudeE6(), origiPoint
                        .getLongitudeE6()));

        // store longitude distance in x
        x = DistanceUtil.getDistance(
                origiPoint,
                new GeoPoint(origiPoint.getLatitudeE6(), destPoint
                        .getLongitudeE6()));

        // N
        // |
        // W----- |----- E x+(longitude)
        // |
        // S
        // z+(latitude)
        if (origiPoint.getLatitudeE6() < destPoint.getLatitudeE6()) {
            z *= -1;
        }
        if (origiPoint.getLongitudeE6() > destPoint.getLongitudeE6()) {
            x *= -1;
        }

        vector.set((float) x, (float) y, (float) z);

        return vector;
    }

    /**
     * Get a location's Azimuth angle according to its distance in latitude and
     * longitude N(0) | W----- |----- E x+(longitude) (270) | (90) S(180)
     * z+(latitude)
     * 
     * @param x
     *            store longitude distance in x
     * @param z
     *            store latitude distance in z
     * @return Azimuth angle in degrees. N--0, E--90, S--180, W--270
     */
    public static float getLocationAzimuth(float x, float z) {

        // 两个都是零则错了
        if (0 == z && 0 == x) {
            // throw new IllegalArgumentException("both arguments are zero!");
            return 0;
        }

        float resultAngle = 0;
        double rad = 0;
        // 有一个是0的情况：在轴上，根据不为零的另一个参数判断具体值
        if (0 == x) {
            if (z < 0) {
                resultAngle = 0;
            }
            else {
                resultAngle = 180;
            }

        }
        else if (0 == z) {
            if (x > 0) {
                resultAngle = 90;

            }
            else {
                resultAngle = 270;
            }

        }
        else {
            // 两个参数都不为零的情况

            // 0-90
            if (z < 0 && x > 0) {
                rad = Math.atan(x / (-z));
                resultAngle = (float) Math.toDegrees(rad);
            }
            // 90 - 180
            if (z > 0 && x > 0) {
                rad = Math.atan(x / z);
                resultAngle = 180 - (float) Math.toDegrees(rad);
            }

            // 180-270
            if (z > 0 && x < 0) {
                rad = Math.atan((-x) / z);
                resultAngle = 180 + (float) Math.toDegrees(rad);
            }

            // 270-360
            if (z < 0 && x < 0) {
                rad = Math.atan(x / z);
                resultAngle = 360 - (float) Math.toDegrees(rad);
            }
        }

        return resultAngle;
    }

}
