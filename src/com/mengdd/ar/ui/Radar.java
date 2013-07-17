package com.mengdd.ar.ui;

import com.mengdd.arapp.GlobalARData;
import com.mengdd.camera.CameraViewModel;
import com.mengdd.paintable.PaintableCircle;
import com.mengdd.paintable.PaintableLine;
import com.mengdd.paintable.PaintablePosition;
import com.mengdd.paintable.PaintableRadarPoints;
import com.mengdd.paintable.PaintableText;
import com.mengdd.poi.data.ScreenPosition;
import com.mengdd.utils.MathUtils;

import android.graphics.Canvas;
import android.graphics.Color;

/**
 * This class will visually represent a radar screen with a radar radius and
 * blips on the screen in their appropriate locations.
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
public class Radar
{

	public static final float RADIUS = 48;

	private static final int LINE_COLOR = Color.argb(150, 0, 0, 220);
	private static final float PAD_X = 10;
	private static final float PAD_Y = 20;
	private static final int RADAR_COLOR = Color.argb(100, 0, 0, 200);
	private static final int TEXT_COLOR = Color.rgb(255, 255, 255);
	private static final int TEXT_SIZE = 12;

	private static ScreenPosition leftRadarLine = null;
	private static ScreenPosition rightRadarLine = null;
	private static PaintablePosition leftLineContainer = null;
	private static PaintablePosition rightLineContainer = null;
	private static PaintablePosition circleContainer = null;

	private static PaintableRadarPoints radarPoints = null;
	private static PaintablePosition pointsContainer = null;

	private static PaintableText paintableText = null;
	private static PaintablePosition paintedContainer = null;

	public Radar()
	{
		if (leftRadarLine == null)
			leftRadarLine = new ScreenPosition();
		if (rightRadarLine == null)
			rightRadarLine = new ScreenPosition();
	}

	/**
	 * Draw the radar on the given Canvas.
	 * 
	 * @param canvas
	 *            Canvas to draw on.
	 * @throws NullPointerException
	 *             if Canvas is NULL.
	 */
	public void draw(Canvas canvas)
	{
		if (canvas == null)
			throw new NullPointerException();

		// Update the pitch and bearing using the phone's rotation matrix
		MathUtils.calcPitchBearing(GlobalARData.getRotationMatrix());
		GlobalARData.setAzimuth(MathUtils.getAzimuth());

		if (GlobalARData.portrait)
		{
			canvas.save();
			canvas.translate(5, canvas.getHeight() - 5);
			canvas.rotate(-90);
		}

		// Update the radar graphics and text based upon the new pitch and
		// bearing
		drawRadarCircle(canvas);
		drawRadarPoints(canvas);
		drawRadarLines(canvas);
		drawRadarText(canvas);

		if (GlobalARData.portrait)
		{
			canvas.restore();
		}
	}

	private void drawRadarCircle(Canvas canvas)
	{
		if (canvas == null)
			throw new NullPointerException();

		if (circleContainer == null)
		{
			PaintableCircle paintableCircle = new PaintableCircle(RADAR_COLOR,
					RADIUS, true);
			circleContainer = new PaintablePosition(paintableCircle, PAD_X
					+ RADIUS, PAD_Y + RADIUS, 0, 1);
		}
		circleContainer.paint(canvas);
	}

	private void drawRadarPoints(Canvas canvas)
	{
		if (canvas == null)
			throw new NullPointerException();

		if (radarPoints == null)
			radarPoints = new PaintableRadarPoints();

		if (pointsContainer == null)
			pointsContainer = new PaintablePosition(radarPoints, PAD_X, PAD_Y,
					-GlobalARData.getAzimuth(), 1);
		else
			pointsContainer.set(radarPoints, PAD_X, PAD_Y,
					-GlobalARData.getAzimuth(), 1);

		pointsContainer.paint(canvas);
	}

	private void drawRadarLines(Canvas canvas)
	{
		if (canvas == null)
			throw new NullPointerException();

		// Left line
		if (leftLineContainer == null)
		{
			leftRadarLine.set(0, -RADIUS);
			leftRadarLine.rotate(-CameraViewModel.DEFAULT_VIEW_ANGLE / 2);
			leftRadarLine.add(PAD_X + RADIUS, PAD_Y + RADIUS);

			float leftX = leftRadarLine.getX() - (PAD_X + RADIUS);
			float leftY = leftRadarLine.getY() - (PAD_Y + RADIUS);
			PaintableLine leftLine = new PaintableLine(LINE_COLOR, leftX, leftY);
			leftLineContainer = new PaintablePosition(leftLine, PAD_X + RADIUS,
					PAD_Y + RADIUS, 0, 1);
		}
		leftLineContainer.paint(canvas);

		// Right line
		if (rightLineContainer == null)
		{
			rightRadarLine.set(0, -RADIUS);
			rightRadarLine.rotate(CameraViewModel.DEFAULT_VIEW_ANGLE / 2);
			rightRadarLine.add(PAD_X + RADIUS, PAD_Y + RADIUS);

			float rightX = rightRadarLine.getX() - (PAD_X + RADIUS);
			float rightY = rightRadarLine.getY() - (PAD_Y + RADIUS);
			PaintableLine rightLine = new PaintableLine(LINE_COLOR, rightX,
					rightY);
			rightLineContainer = new PaintablePosition(rightLine, PAD_X
					+ RADIUS, PAD_Y + RADIUS, 0, 1);
		}
		rightLineContainer.paint(canvas);
	}

	private void drawRadarText(Canvas canvas)
	{
		if (canvas == null)
			throw new NullPointerException();

		// Direction text
		int range = (int) (GlobalARData.getAzimuth() / (360f / 16f));
		String dirTxt = "";
		if (range == 15 || range == 0)
			dirTxt = "N";
		else if (range == 1 || range == 2)
			dirTxt = "NE";
		else if (range == 3 || range == 4)
			dirTxt = "E";
		else if (range == 5 || range == 6)
			dirTxt = "SE";
		else if (range == 7 || range == 8)
			dirTxt = "S";
		else if (range == 9 || range == 10)
			dirTxt = "SW";
		else if (range == 11 || range == 12)
			dirTxt = "W";
		else if (range == 13 || range == 14)
			dirTxt = "NW";
		int bearing = (int) GlobalARData.getAzimuth();
		radarText(canvas, "" + bearing + ((char) 176) + " " + dirTxt,
				(PAD_X + RADIUS), (PAD_Y - 5), true);

		// Zoom text
		radarText(canvas, formatDist(GlobalARData.getRadius() * 1000),
				(PAD_X + RADIUS), (PAD_Y + RADIUS * 2 - 10), false);
	}

	private void radarText(Canvas canvas, String txt, float x, float y,
			boolean bg)
	{
		if (canvas == null || txt == null)
			throw new NullPointerException();

		if (paintableText == null)
			paintableText = new PaintableText(txt, TEXT_COLOR, TEXT_SIZE, bg);
		else
			paintableText.set(txt, TEXT_COLOR, TEXT_SIZE, bg);

		if (paintedContainer == null)
			paintedContainer = new PaintablePosition(paintableText, x, y, 0, 1);
		else
			paintedContainer.set(paintableText, x, y, 0, 1);

		paintedContainer.paint(canvas);
	}

	private static String formatDist(float meters)
	{
		if (meters < 1000)
		{
			return ((int) meters) + "m";
		}
		else if (meters < 10000)
		{
			return formatDec(meters / 1000f, 1) + "km";
		}
		else
		{
			return ((int) (meters / 1000f)) + "km";
		}
	}

	private static String formatDec(float val, int dec)
	{
		int factor = (int) Math.pow(10, dec);

		int front = (int) (val);
		int back = (int) Math.abs(val * (factor)) % factor;

		return front + "." + back;
	}
}
