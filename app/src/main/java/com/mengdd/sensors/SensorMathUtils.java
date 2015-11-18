package com.mengdd.sensors;

import android.hardware.SensorManager;
import android.util.Log;
import android.view.Surface;

import com.mengdd.utils.AppConstants;

public class SensorMathUtils {

	public static final float PARALLEL_TOLERANCE = 30;

	public static float calDevicePitch(int screenRotation,
			float[] rotationMatrix) {

		float[] selfOrientaionValues = new float[3];
		// get Orientation values without remapCoordinatesSystem, the
		// coordinates are the device coordinates
		SensorManager.getOrientation(rotationMatrix, selfOrientaionValues);
		// convert values to degrees
		// azimuth
		selfOrientaionValues[0] = (float) Math
				.toDegrees(selfOrientaionValues[0]);
		// pitch
		selfOrientaionValues[1] = (float) Math
				.toDegrees(selfOrientaionValues[1]);
		// roll
		selfOrientaionValues[2] = (float) Math
				.toDegrees(selfOrientaionValues[2]);

		float pitch = 0;

		switch (screenRotation) {
		case Surface.ROTATION_0:
			pitch = selfOrientaionValues[1];

			break;
		case Surface.ROTATION_90:
			pitch = -selfOrientaionValues[2];

			break;
		case Surface.ROTATION_180:
			pitch = -selfOrientaionValues[1];

			break;
		case Surface.ROTATION_270:
			pitch = selfOrientaionValues[2];
			break;

		default:
			break;
		}

		Log.i(AppConstants.LOG_TAG, "pitch: " + pitch);
		return pitch;

	}

	public static void remapCoordinates(float[] R, float[] outR,
			int screenRotation, float devicePitch) {
		remapCoordinates(R, outR, screenRotation, devicePitch,
				SensorMathUtils.PARALLEL_TOLERANCE);
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
	public static void remapCoordinates(float[] R, float[] outR,
			int screenRotation, float devicePitch, float parallelTolerance) {

		// the screen orientation decides the map from Device XY to World
		// Coordinates
		// the paramters in remapCoordinateSystem Method means which world axis
		// the devcie xy want to map to

		switch (screenRotation) {
		case Surface.ROTATION_0:

			if (Math.abs(devicePitch) < parallelTolerance) {
				// device parallel to the ground

				// no need to remap
				// but we still want outR values
				SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_X,
						SensorManager.AXIS_Y, outR);
			}
			else {

				// vertical to ground

				SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_X,
						SensorManager.AXIS_Z, outR);

			}

			break;
		case Surface.ROTATION_90:

			if (Math.abs(devicePitch) < parallelTolerance) {

				// device parallel to the ground

				SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_Y,
						SensorManager.AXIS_MINUS_X, outR);
			}
			else {

				// vertical to ground

				SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_Z,
						SensorManager.AXIS_MINUS_X, outR);

			}

			break;

		case Surface.ROTATION_180:

			if (Math.abs(devicePitch) < parallelTolerance) {
				// device parallel to the ground

				SensorManager.remapCoordinateSystem(R,
						SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Y,
						outR);
			}
			else {

				// vertical to ground

				SensorManager.remapCoordinateSystem(R,
						SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Z,
						outR);

			}

			break;

		case Surface.ROTATION_270:

			if (Math.abs(devicePitch) < parallelTolerance) {
				// device parallel to the ground

				SensorManager.remapCoordinateSystem(R,
						SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X, outR);
			}
			else {
				// vertical to ground

				SensorManager.remapCoordinateSystem(R,
						SensorManager.AXIS_MINUS_Z, SensorManager.AXIS_X, outR);
			}
			break;

		default:
			break;
		}
	}

	/**
	 * Get the Orientation Angles in degrees
	 * 
	 * @param rotationMatrix
	 *            the rotation matrix after proper remap coordinates
	 * @return float[0] Bearing or Azimuth, float[1] pitch, float[2] roll. all
	 *         in degrees
	 */
	public static float[] getOrientationAngles(float[] rotationMatrix) {
		float[] values = new float[3];

		SensorManager.getOrientation(rotationMatrix, values);

		// finally we get 3 angle in degrees
		values[0] = (float) Math.toDegrees(values[0]);
		values[0] = (360 + values[0]) % 360;

		values[1] = (float) Math.toDegrees(values[1]);
		values[2] = (float) Math.toDegrees(values[2]);

		return values;

	}
}
