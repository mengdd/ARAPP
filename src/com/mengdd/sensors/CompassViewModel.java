package com.mengdd.sensors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;
import com.mengdd.sensors.CompassView.CompassStatus;
import com.mengdd.utils.AppConstants;
import com.mengdd.utils.LowPassFilter;

/**
 * 
 * CompassViewModel is the Module for the Compass.
 * Get the sensors' data and update the CompassView
 * 
 * The Compass results are compared with the Smart Compass App for insurance.
 * 
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 * 
 */
public class CompassViewModel extends ViewModel {
	// the RootView of the ViewModel
	private View mRootView = null;

	// the Accelerometer values and Magnetometer values
	private float[] aValues = new float[3];
	private float[] mValues = new float[3];
	private SensorManager sensorManager;

	// the compass view
	private CompassView compassView;

	// display
	private Display mDisplay = null;

	// Smooth
	private static float smooth[] = new float[3];

	public void setVisibility(int visibility) {
		if (View.VISIBLE == visibility) {
			onResume(null);
		}

		if (View.GONE == visibility) {
			onPause();
			onStop();
		}

		mRootView.setVisibility(visibility);
	}

	public CompassViewModel(Activity activity) {
		super(activity);

		init();
	}

	private void init() {
		WindowManager wManager = (WindowManager) mActivity
				.getSystemService(Context.WINDOW_SERVICE);
		mDisplay = wManager.getDefaultDisplay();
	}

	@Override
	public View getView() {
		return mRootView;
	}

	@Override
	public void onCreate(Intent intent) {
		super.onCreate(intent);

		mRootView = mInflater.inflate(R.layout.compass_view_model, null);

		compassView = (CompassView) mRootView.findViewById(R.id.horizonView);

		sensorManager = (SensorManager) mActivity
				.getSystemService(Context.SENSOR_SERVICE);

		updateOrientation(new float[] { 0, 0, 0 });

	}

	@Override
	public void onResume(Intent intent) {
		super.onResume(intent);

		// register the accelerometer and magField sensors
		Sensor accelerometer = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		Sensor magField = sensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		sensorManager.registerListener(sensorEventListener, accelerometer,
				SensorManager.SENSOR_DELAY_FASTEST);
		sensorManager.registerListener(sensorEventListener, magField,
				SensorManager.SENSOR_DELAY_FASTEST);

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		// unregister thesensors
		sensorManager.unregisterListener(sensorEventListener);
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * Set 3 rotation values in the CompassView
	 * 
	 * @param values
	 */
	private void updateOrientation(float[] values) {
		if (compassView != null) {
			compassView.setBearing(values[0]);
			compassView.setPitch(values[1]);
			compassView.setRoll(-values[2]);
			compassView.invalidate();
		}
	}

	/**
	 * Get Orientation values according to accelerometer and magField sensor
	 * values.
	 * compare the values of without remapCoordinatesSystem and with
	 * remapCoordinatesSystem.
	 * The compared valuse are shown in the TextViews
	 * 
	 * @return Orientation values with remapCoordinatesSystem
	 */
	private float[] calculateOrientation() {
		float[] values = new float[3];
		float[] R = new float[9];
		float[] outR = new float[9];

		// get RotationMatrix according to accelerometer and magField sensor
		// values
		// the result is return to R
		SensorManager.getRotationMatrix(R, null, aValues, mValues);
		// get Orientation of Display
		int screenRotation = mDisplay.getRotation();
		float devicePitch = SensorMathUtils.calDevicePitch(screenRotation, R);

		float parallelTolerance = 30;

		// remap the coordinate according to the device orientation and screen
		// orientation
		remapCoordinates(R, outR, screenRotation, devicePitch,
				parallelTolerance);

		SensorManager.getOrientation(outR, values);

		// finally we get 3 angle in degrees
		values[0] = (float) Math.toDegrees(values[0]);
		values[1] = (float) Math.toDegrees(values[1]);
		values[2] = (float) Math.toDegrees(values[2]);

		return values;
	}

	/**
	 * 
	 * Map the Rotaion Mathix from the device coordinates to a world coordinates
	 * Which world axis to map are decided by the display orientation and the
	 * device pitch angle
	 * 
	 * @param R
	 *            the Rotation Mathix in device coordinates
	 * @param outR
	 *            the Roation Mathix in the remaped world coordinates
	 * @param screenRotation
	 *            the screen display orientation, including
	 *            ROTATION_0,ROTATION_90,ROTATION_180,ROTATION_270
	 * @param devicePitch
	 *            the device pitch angle according to the device self axis
	 * @param parallelTolerance
	 *            the devicePitch in this bounds should be considered parallel
	 *            to the ground
	 */
	private void remapCoordinates(float[] R, float[] outR, int screenRotation,
			float devicePitch, float parallelTolerance) {

		// the screen orientation decides the map from Device XY to World
		// Coordinates
		// the paramters in remapCoordinateSystem Method means which world axis
		// the devcie xy want to map to

		switch (screenRotation) {
		case Surface.ROTATION_0:

			if (Math.abs(devicePitch) < parallelTolerance) {
				// device parallel to the ground
				compassView.setCompassStatus(CompassStatus.ParallelToGround);

				// no need to remap
				// but we still want outR values
				SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_X,
						SensorManager.AXIS_Y, outR);
			}
			else {

				// vertical to ground
				compassView.setCompassStatus(CompassStatus.VerticalToGround);
				SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_X,
						SensorManager.AXIS_Z, outR);

			}

			break;
		case Surface.ROTATION_90:

			if (Math.abs(devicePitch) < parallelTolerance) {

				// device parallel to the ground
				compassView.setCompassStatus(CompassStatus.ParallelToGround);
				SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_Y,
						SensorManager.AXIS_MINUS_X, outR);
			}
			else {

				// vertical to ground
				compassView.setCompassStatus(CompassStatus.VerticalToGround);
				SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_Z,
						SensorManager.AXIS_MINUS_X, outR);

			}

			break;

		case Surface.ROTATION_180:

			if (Math.abs(devicePitch) < parallelTolerance) {
				// device parallel to the ground
				compassView.setCompassStatus(CompassStatus.ParallelToGround);
				SensorManager.remapCoordinateSystem(R,
						SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Y,
						outR);
			}
			else {

				// vertical to ground
				compassView.setCompassStatus(CompassStatus.VerticalToGround);
				SensorManager.remapCoordinateSystem(R,
						SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Z,
						outR);

			}

			break;

		case Surface.ROTATION_270:

			if (Math.abs(devicePitch) < parallelTolerance) {
				// device parallel to the ground
				compassView.setCompassStatus(CompassStatus.ParallelToGround);
				SensorManager.remapCoordinateSystem(R,
						SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X, outR);
			}
			else {
				// vertical to ground
				compassView.setCompassStatus(CompassStatus.VerticalToGround);
				SensorManager.remapCoordinateSystem(R,
						SensorManager.AXIS_MINUS_Z, SensorManager.AXIS_X, outR);
			}
			break;

		default:
			break;
		}
	}

	private final SensorEventListener sensorEventListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				// without smooth
				// aValues = event.values;

				// with smooth
				smooth = LowPassFilter
						.filter(0.5f, 1.0f, event.values, aValues);
				aValues[0] = smooth[0];
				aValues[1] = smooth[1];
				aValues[2] = smooth[2];

			}
			if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) { // without
																		// smooth
																		// mValues
																		// =
																		// event.values;

				// with smooth
				smooth = LowPassFilter
						.filter(2.0f, 4.0f, event.values, mValues);
				mValues[0] = smooth[0];
				mValues[1] = smooth[1];
				mValues[2] = smooth[2];
			}

			// update values here
			updateOrientation(calculateOrientation());

		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}

	};

}
