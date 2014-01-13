package com.mengdd.sensors;

import java.util.Calendar;

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

import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;
import com.mengdd.location.google.GoogleLocationModel;
import com.mengdd.sensors.CompassView.CompassStatus;
import com.mengdd.utils.AppConstants;
import com.mengdd.utils.LogUtils;
import com.mengdd.utils.LowPassFilter;
import com.mengdd.utils.Matrix;

/**
 *
 * CompassViewModel is the Module for the Compass. Get the sensors' data and
 * update the CompassView
 *
 * The Compass results are compared with the Smart Compass App for insurance.
 *
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 *
 */
public class CompassViewModel extends ViewModel implements LocationListener {
	// the RootView of the ViewModel
	private View mRootView = null;

	private String TAG = "Compass";
	// the Accelerometer values and Magnetometer values
	private final float[] aValues = new float[3];
	private final float[] mValues = new float[3];
	private SensorManager sensorManager;

	// the compass view
	private CompassView compassView;

	// display
	private Display mDisplay = null;

	// Smooth
	private static float smooth[] = new float[3];

	// 计算模式
	public static final int REMAP_NONE = 0;
	// public static final int REMAP_FIX = 1;
	public static final int REMAP_WHOLE = 1;
	private int mRemapMode = REMAP_WHOLE;

	public int getRemapMode() {
		return mRemapMode;
	}

	public void setRemapMode(int mode) {
		this.mRemapMode = mode;

	}

	// 是否开启防抖动
	private boolean isAntiAlias = false;

	public boolean isAntiAlias() {
		return isAntiAlias;
	}

	public void setAntiAlias(boolean isAntiAlias) {
		this.isAntiAlias = isAntiAlias;
	}

	// 是否开启地磁场纠偏
	private boolean isMagneticCompensatedOn = true;

	public boolean isMagneticCompensatedOn() {
		return isMagneticCompensatedOn;
	}

	public void setMagneticCompensatedOn(boolean isMagneticCompensatedOn) {
		this.isMagneticCompensatedOn = isMagneticCompensatedOn;
	}

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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
		stopLocationUpdates();
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
			Calendar c = Calendar.getInstance();
			int seconds = c.get(Calendar.SECOND);
			int milliseconds = c.get(Calendar.MILLISECOND);
			// 按照时间输出，查看滤波前后效果，绘制曲线
			LogUtils.i(TAG, "isAntiAlias: " + isAntiAlias + ", time: "
					+ seconds + " s, " + milliseconds + " ms" + ", bearing: "
					+ values[0]);
			LogUtils.logToFile(TAG, "isAntiAlias: " + isAntiAlias + ", time: "
					+ seconds + " s, " + milliseconds + " ms" + ", bearing: "
					+ values[0]);
		}
	}

	/**
	 * Get Orientation values according to accelerometer and magField sensor
	 * values. compare the values of without remapCoordinatesSystem and with
	 * remapCoordinatesSystem. The compared valuse are shown in the TextViews
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

		// UI switch between parallel and vertical status
		updateCompassStatus(devicePitch);

		if (REMAP_WHOLE == mRemapMode) {

			// remap the coordinate according to the device orientation and
			// screen
			// orientation
			SensorMathUtils.remapCoordinates(R, outR, screenRotation,
					devicePitch, 0);
		}

		if (REMAP_NONE == mRemapMode) {
			outR = R;
		}

		// 是否加入地磁场纠偏
		if (isMagneticCompensatedOn) {
			if (!isLocationUpdating) {
				requestLocation();
			}
			compensateMagneticField(outR, outR);
		} else {
			if (isLocationUpdating) {
				stopLocationUpdates();
			}
		}

		// 根据旋转之后的旋转矩阵，得到三个角度值
		SensorManager.getOrientation(outR, values);

		// finally we get 3 angle in degrees
		values[0] = (float) Math.toDegrees(values[0]);
		values[1] = (float) Math.toDegrees(values[1]);
		values[2] = (float) Math.toDegrees(values[2]);

		return values;
	}

	private final SensorEventListener sensorEventListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				// without smooth
				// aValues = event.values;

				// with smooth
				if (isAntiAlias) {
					smooth = LowPassFilter.filter(0.5f, 1.0f, event.values,
							aValues);
					aValues[0] = smooth[0];
					aValues[1] = smooth[1];
					aValues[2] = smooth[2];
				} else {
					aValues[0] = event.values[0];
					aValues[1] = event.values[1];
					aValues[2] = event.values[2];
				}

			}
			if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				// without smooth
				// mValues = event.values;

				// with smooth
				if (isAntiAlias) {
					smooth = LowPassFilter.filter(2.0f, 4.0f, event.values,
							mValues);
					mValues[0] = smooth[0];
					mValues[1] = smooth[1];
					mValues[2] = smooth[2];
				} else {
					mValues[0] = event.values[0];
					mValues[1] = event.values[1];
					mValues[2] = event.values[2];
				}
			}

			// update values here
			updateOrientation(calculateOrientation());

		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}

	};

	private void updateCompassStatus(float devicePitch) {
		if (Math.abs(devicePitch) < SensorMathUtils.PARALLEL_TOLERANCE) {
			compassView.setCompassStatus(CompassStatus.ParallelToGround);
		} else {
			compassView.setCompassStatus(CompassStatus.VerticalToGround);
		}
	}

	private void compensateMagneticField(float[] rotation, float[] outRotation) {

		// 先判断是否更新了地磁场偏移的合理的值
		if (null == sGeomagneticField) {
			return;
		}
		Matrix worldCoord = new Matrix();
		Matrix magneticCompensatedCoord = new Matrix();

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
		synchronized (sMagneticCompensatedValue) {
			// Cross product the matrix with the magnetic north compensation
			magneticCompensatedCoord.prod(sMagneticCompensatedValue);
		}

		// Cross product with the world coordinates to get a mag north
		// compensated coords
		magneticCompensatedCoord.prod(worldCoord);

		// Set the rotation matrix (used to translate all object from lat/lon to
		// x/y/z)

		// GlobalARData.setRotationMatrix(magneticCompensatedCoord);
		magneticCompensatedCoord.get(outRotation);

	}

	// 地磁场补偿相关
	private static GeomagneticField sGeomagneticField = null;
	private static final Matrix sMagneticCompensatedValue = new Matrix();
	private GoogleLocationModel mLocationModel = null;

	private boolean isLocationUpdating = false;

	private void requestLocation() {
		if (null == mLocationModel) {
			mLocationModel = new GoogleLocationModel(mActivity);
			mLocationModel.setLocationListener(this);
		}

		mLocationModel.registerLocationUpdates();
		isLocationUpdating = true;

	}

	private void stopLocationUpdates() {

		if (null != mLocationModel) {
			mLocationModel.unregisterLocationUpdates();

		}
		isLocationUpdating = false;
	}

	@Override
	public void onLocationChanged(Location location) {
		// 地磁场纠偏，需要监听位置，更新偏移量
		Log.i(AppConstants.LOG_TAG, "Compass onLocationChanged" + location);

		sGeomagneticField = new GeomagneticField(
				(float) location.getLatitude(),
				(float) location.getLongitude(),
				(float) location.getAltitude(), System.currentTimeMillis());

		float dec = (float) Math.toRadians(-sGeomagneticField.getDeclination());

		synchronized (sMagneticCompensatedValue) {
			sMagneticCompensatedValue.toIdentity();

			sMagneticCompensatedValue.set((float) Math.cos(dec), 0f,
					(float) Math.sin(dec), 0f, 1f, 0f, -(float) Math.sin(dec),
					0f, (float) Math.cos(dec));
		}

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

}
