package com.mengdd.utils;

import android.location.Location;
import android.util.Log;

import com.mengdd.arapp.GlobalARData;
import com.mengdd.poi.data.PhysicalLocation;

/**
 * A static class used to calculate azimuth, pitch, and roll given a rotation
 * matrix.
 * 
 * This file was adapted from Mixare <http://www.mixare.org/>
 * 
 * The source of the codes:
 * 1."android-augment-reality-framework"
 * project link: http://code.google.com/p/android-augment-reality-framework/
 * 
 * 2.The book: "Pro Android Augmented Reality"
 * http://www.apress.com/9781430239451
 * Official repository for Pro Android Augmented Reality:
 * https://github.com/RaghavSood/ProAndroidAugmentedReality
 * 
 * @author Daniele Gobbetti <info@mixare.org>
 * @author Justin Wetherell <phishman3579@gmail.com>
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 */
public class MathUtils
{

	private static final Vector looking = new Vector();
	private static final float[] lookingArray = new float[3];
	private static final Matrix tempMatrix = new Matrix();

	private static volatile float azimuth = 0;
	private static volatile float pitch = 0;
	private static volatile float roll = 0;

	private MathUtils()
	{
	}

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
			float post_x, float post_y)
	{
		float delta_x = post_x - center_x;
		float delta_y = post_y - center_y;
		return (float) (Math.atan2(delta_y, delta_x) * 180 / Math.PI);
	}

	/**
	 * Azimuth the phone's camera is pointing. From 0 to 360 with magnetic north
	 * compensation.
	 * 
	 * @return float representing the azimuth the phone's camera is pointing
	 */
	public static synchronized float getAzimuth()
	{
		return MathUtils.azimuth;
	}

	/**
	 * Pitch of the phone's camera. From -90 to 90, where negative is pointing
	 * down and zero is level.
	 * 
	 * @return float representing the pitch of the phone's camera.
	 */
	public static synchronized float getPitch()
	{
		return MathUtils.pitch;
	}

	/**
	 * Roll of the phone's camera. From -90 to 90, where negative is rolled left
	 * and zero is level.
	 * 
	 * @return float representing the roll of the phone's camera.
	 */
	public static synchronized float getRoll()
	{
		return MathUtils.roll;
	}

	/**
	 * Calculate and populate the Azimuth, Pitch, and Roll.
	 * 
	 * @param rotationMatrix
	 *            Rotation matrix used in calculations.
	 */
	public static synchronized void calcPitchBearing(Matrix rotationMatrix)
	{
		if (rotationMatrix == null)
		{
			return;
		}

		//Log.i(AppConstants.LOG_TAG, "calcPitchBearing");
		tempMatrix.set(rotationMatrix);
		tempMatrix.transpose();
		if (GlobalARData.portrait)
		{
			looking.set(0, 1, 0);
		}
		else
		{
			looking.set(1, 0, 0);
		}
		looking.prod(tempMatrix);
		looking.get(lookingArray);
		MathUtils.azimuth = ((getAngle(0, 0, lookingArray[0], lookingArray[2]) + 360) % 360);
		MathUtils.roll = -(90 - Math.abs(getAngle(0, 0, lookingArray[1],
				lookingArray[2])));
		looking.set(0, 0, 1);
		looking.prod(tempMatrix);
		looking.get(lookingArray);
		MathUtils.pitch = -(90 - Math.abs(getAngle(0, 0, lookingArray[1],
				lookingArray[2])));
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
			float distance, int width, int height, float addX, float addY)
	{
		float[] tmp1 = new float[3];
		float[] tmp2 = new float[3];
		orgPoint.get(tmp1);

		tmp2[0] = (distance * tmp1[0] / -tmp1[2]);
		tmp2[1] = (distance * tmp1[1] / -tmp1[2]);
		tmp2[2] = (tmp1[2]);
		tmp2[0] = (tmp2[0] + addX + width / 2);
		tmp2[1] = (-tmp2[1] + addY + height / 2);

		prjPoint.set(tmp2);
	}

	/**
	 * Converts a Location relative to original location to a Vector
	 * 
	 * @param origiLocation
	 *            the location as original point
	 * @param destLocation
	 *            the location as destination
	 * @return
	 */
	public static synchronized Vector convLocationToVector(
			Location origiLocation, PhysicalLocation destLocation)
	{
		if (origiLocation == null || destLocation == null)
		{
			throw new IllegalArgumentException("Location is null");
		}

		Vector vector = new Vector();
		float[] x = new float[1];
		double y = 0.0d;
		float[] z = new float[1];

		Location.distanceBetween(origiLocation.getLatitude(),
				origiLocation.getLongitude(), destLocation.getLatitude(),
				origiLocation.getLongitude(), z);

		Location.distanceBetween(origiLocation.getLatitude(),
				origiLocation.getLongitude(), origiLocation.getLatitude(),
				destLocation.getLongitude(), x);

		y = destLocation.getAltitude() - origiLocation.getAltitude();

		if (origiLocation.getLatitude() < destLocation.getLatitude())
		{
			z[0] *= -1;
		}
		if (origiLocation.getLongitude() > destLocation.getLongitude())
		{
			x[0] *= -1;
		}

		vector.set(x[0], (float) y, z[0]);

		return vector;
	}

}
