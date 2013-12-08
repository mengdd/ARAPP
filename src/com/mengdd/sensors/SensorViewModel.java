package com.mengdd.sensors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.mengdd.arapp.GlobalARData;
import com.mengdd.components.ViewModel;
import com.mengdd.utils.AppConstants;
import com.mengdd.utils.LowPassFilter;
import com.mengdd.utils.Matrix;

/**
 * ViewModel to get data from sensors and update to global
 * 
 * The source of the codes "android-augment-reality-framework" project link:
 * http://code.google.com/p/android-augment-reality-framework/
 * 
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 */
public class SensorViewModel extends ViewModel implements LocationListener,
        SensorEventListener {

    // display
    private Display mDisplay = null;
    private int mScreenOrientation = 0;

    public SensorViewModel(Activity activity) {
        super(activity);
        initDisplay();
    }

    @Override
    public View getView() {
        return null;
    }

    private static final String TAG = "SensorsActivity";
    private static final AtomicBoolean computing = new AtomicBoolean(false);

    private static final float temp[] = new float[9]; // Temporary rotation
                                                      // matrix in Android
                                                      // format
    private static final float rotation[] = new float[9]; // Final rotation
                                                          // matrix in Android
                                                          // format
    private static final float grav[] = new float[3]; // Gravity (a.k.a
                                                      // accelerometer data)
    private static final float mag[] = new float[3]; // Magnetic
    /*
     * Using Matrix operations instead. This was way too inaccurate, private
     * static final float apr[] = new float[3]; //Azimuth, pitch, roll
     */

    private static final Matrix worldCoord = new Matrix();
    private static final Matrix magneticCompensatedCoord = new Matrix();

    private static final Matrix mageticNorthCompensation = new Matrix();

    private static GeomagneticField gmf = null;
    private static float smooth[] = new float[3];
    private static SensorManager sensorMgr = null;
    private static List<Sensor> sensors = null;
    private static Sensor sensorGrav = null;
    private static Sensor sensorMag = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume(Intent intent) {
        super.onResume(intent);
        onStart();
    }

    private void onStart() {

        try {
            sensorMgr = (SensorManager) mActivity
                    .getSystemService(Context.SENSOR_SERVICE);

            sensors = sensorMgr.getSensorList(Sensor.TYPE_ACCELEROMETER);
            if (sensors.size() > 0){
                sensorGrav = sensors.get(0);
            }

            sensors = sensorMgr.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
            if (sensors.size() > 0){
                sensorMag = sensors.get(0);
            }

            sensorMgr.registerListener(this, sensorGrav,
                    SensorManager.SENSOR_DELAY_NORMAL);
            sensorMgr.registerListener(this, sensorMag,
                    SensorManager.SENSOR_DELAY_NORMAL);

            try {
                // if no new location,use the hardfix google location at first
                Location location = GlobalARData.getCurrentGoogleLocation();

                gmf = new GeomagneticField((float) location.getLatitude(),
                        (float) location.getLongitude(),
                        (float) location.getAltitude(),
                        System.currentTimeMillis());

                float dec = (float) Math.toRadians(-gmf.getDeclination());

                synchronized (mageticNorthCompensation) {
                    // Identity matrix
                    // [ 1, 0, 0 ]
                    // [ 0, 1, 0 ]
                    // [ 0, 0, 1 ]
                    mageticNorthCompensation.toIdentity();

                    // Counter-clockwise rotation at negative declination around
                    // the y-axis
                    // note: declination of the horizontal component of the
                    // magnetic field
                    // from true north, in degrees (i.e. positive means the
                    // magnetic
                    // field is rotated east that much from true north).
                    // note2: declination is the difference between true north
                    // and magnetic north
                    // [ cos, 0, sin ]
                    // [ 0, 1, 0 ]
                    // [ -sin, 0, cos ]
                    mageticNorthCompensation.set((float) Math.cos(dec), 0f,
                            (float) Math.sin(dec), 0f, 1f, 0f,
                            -(float) Math.sin(dec), 0f, (float) Math.cos(dec));
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        catch (Exception ex1) {
            try {
                if (sensorMgr != null) {
                    sensorMgr.unregisterListener(this, sensorGrav);
                    sensorMgr.unregisterListener(this, sensorMag);
                    sensorMgr = null;
                }

            }
            catch (Exception ex2) {
                ex2.printStackTrace();
            }
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            try {
                sensorMgr.unregisterListener(this, sensorGrav);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                sensorMgr.unregisterListener(this, sensorMag);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            sensorMgr = null;

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent evt) {

        // update screen orientation
        updateScreenOrientation();
        if (!computing.compareAndSet(false, true))
            return;

        if (evt.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            smooth = LowPassFilter.filter(0.5f, 1.0f, evt.values, grav);
            grav[0] = smooth[0];
            grav[1] = smooth[1];
            grav[2] = smooth[2];

        }
        else if (evt.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            smooth = LowPassFilter.filter(2.0f, 4.0f, evt.values, mag);
            mag[0] = smooth[0];
            mag[1] = smooth[1];
            mag[2] = smooth[2];

        }

        // // Find real world position relative to phone location ////
        // Get rotation matrix given the gravity and geomagnetic matrices
        SensorManager.getRotationMatrix(temp, null, grav, mag);

        float devicePitch = SensorMathUtils.calDevicePitch(mScreenOrientation,
                temp);

        Log.i("pitch", "pitch 1 --> " + devicePitch);

        // set parallel tolerance to zero to assume at all conditions device is
        // vertical
        SensorMathUtils.remapCoordinates(temp, rotation, mScreenOrientation,
                devicePitch, 0);

        // Convert from float[9] to Matrix
        worldCoord
                .set(rotation[0], rotation[1], rotation[2], rotation[3],
                        rotation[4], rotation[5], rotation[6], rotation[7],
                        rotation[8]);

        // // Find position relative to magnetic north ////
        // Identity matrix
        // [ 1, 0, 0 ]
        // [ 0, 1, 0 ]
        // [ 0, 0, 1 ]
        magneticCompensatedCoord.toIdentity();
        //
        // synchronized (mageticNorthCompensation) {
        // // Cross product the matrix with the magnetic north compensation
        // magneticCompensatedCoord.prod(mageticNorthCompensation);
        // }

        // Cross product with the world coordinates to get a mag north
        // compensated coords
        magneticCompensatedCoord.prod(worldCoord);

        // Set the rotation matrix (used to translate all object from lat/lon to
        // x/y/z)
        GlobalARData.setRotationMatrix(magneticCompensatedCoord);

        updateOrientationAngles(rotation);

        computing.set(false);

        // notify listeners
        notifySensorListeners(evt);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Ignore
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Ignore
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Ignore
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(AppConstants.LOG_TAG, "SensorViewModel onLocationChanged"
                + location);

        gmf = new GeomagneticField((float) location.getLatitude(),
                (float) location.getLongitude(),
                (float) location.getAltitude(), System.currentTimeMillis());

        float dec = (float) Math.toRadians(-gmf.getDeclination());

        synchronized (mageticNorthCompensation) {
            mageticNorthCompensation.toIdentity();

            mageticNorthCompensation.set((float) Math.cos(dec), 0f,
                    (float) Math.sin(dec), 0f, 1f, 0f, -(float) Math.sin(dec),
                    0f, (float) Math.cos(dec));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (null == sensor) {
            throw new IllegalArgumentException("sensor is null!");
        }

        if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD
                && accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            Log.e(TAG, "Compass data unreliable");
        }
    }

    private void notifySensorListeners(SensorEvent event) {

        if (null != mSensorListeners && mSensorListeners.size() > 0) {
            for (SensorEventListener listener : mSensorListeners) {
                listener.onSensorChanged(event);
            }

        }
    }

    // Observers
    private List<SensorEventListener> mSensorListeners;

    /**
     * Add Sensor Event changed listener to this Observable object. The lister's
     * onLocationChanged() will be called every time when current location data
     * changed.
     * 
     * @param listener
     * @return
     */
    public boolean addSensorEventListener(SensorEventListener listener) {
        boolean result = false;
        if (null == listener) {
            throw new IllegalArgumentException("lister == null");
        }

        if (null == mSensorListeners) {
            mSensorListeners = new ArrayList<SensorEventListener>();
        }

        result = mSensorListeners.add(listener);

        return result;
    }

    /**
     * Remove the listener from the list of listeners. The listener will not be
     * notified any more if location data changed.
     * 
     * @param listener
     * @return
     */
    public boolean removeSensorEventListener(SensorEventListener listener) {
        boolean result = false;
        if (null == listener) {
            throw new IllegalArgumentException("lister == null");
        }

        if (null == mSensorListeners) {
            return result;
        }

        result = mSensorListeners.remove(listener);

        return result;

    }

    // watch screen orientation
    private void initDisplay() {
        WindowManager wManager = (WindowManager) mActivity
                .getSystemService(Context.WINDOW_SERVICE);
        mDisplay = wManager.getDefaultDisplay();
    }

    private void updateScreenOrientation() {
        // get Orientation of Display
        mScreenOrientation = mDisplay.getRotation();

        GlobalARData.screenOrientation = mScreenOrientation;
        //
        // if (Surface.ROTATION_0 == mScreenOrientation
        // || Surface.ROTATION_180 == mScreenOrientation) {
        // GlobalARData.portrait = true;
        // }
        // else {
        // GlobalARData.portrait = false;
        // }
    }

    private void updateOrientationAngles(float[] rotationMatrix) {

        float[] angles = SensorMathUtils.getOrientationAngles(rotationMatrix);
        GlobalARData.setAzimuth(angles[0]);
        GlobalARData.setPitch(angles[1]);
        GlobalARData.setRoll(angles[2]);

        Log.i("pitch", "pitch 2 --> " + angles[1]);

        Log.i("azimuth", "Azimuth--> " + angles[0]);
    }

}
