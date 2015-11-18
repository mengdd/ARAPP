package com.mengdd.poi.ui;

import com.mengdd.arapp.GlobalARData;
import com.mengdd.arapp.R;
import com.mengdd.paintable.PaintableCircle;
import com.mengdd.paintable.PaintableLine;
import com.mengdd.paintable.PaintablePosition;
import com.mengdd.paintable.PaintableRadarPoints;
import com.mengdd.paintable.PaintableText;
import com.mengdd.poi.data.ScreenPosition;
import com.mengdd.poi.ui.RadarZoomController.OnRadarZoomChangedListener;
import com.mengdd.utils.AppConstants;
import com.mengdd.utils.MathUtils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.Camera.OnZoomChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * This class will visually represent a radar screen with a radar radius and
 * blips on the screen in their appropriate locations.
 * 
 * This class is adapted from: 1."android-augment-reality-framework" project
 * link: http://code.google.com/p/android-augment-reality-framework/
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
public class RadarView extends View implements SensorEventListener,
        OnRadarZoomChangedListener {

    public static float RADIUS = 48;

    private static int LINE_COLOR = Color.argb(150, 0, 0, 220);
    private static float PAD_X = 10;
    private static float PAD_Y = 20;
    private static int RADAR_COLOR = Color.argb(100, 0, 0, 200);
    private static int TEXT_COLOR = Color.rgb(255, 255, 255);
    private static float TEXT_SIZE = 12;

    private static PaintablePosition leftLineContainer = null;
    private static PaintablePosition rightLineContainer = null;
    private static PaintablePosition circleContainer = null;

    private static PaintableRadarPoints radarPoints = null;
    private static PaintablePosition pointsContainer = null;

    private static PaintableText paintableText = null;
    private static PaintablePosition paintedContainer = null;

    public RadarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RadarView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        Resources res = context.getResources();
        if (null != res) {
            RADIUS = res.getDimension(R.dimen.radar_radius);
            LINE_COLOR = res.getColor(R.color.radar_line_color);
            PAD_X = res.getDimension(R.dimen.radar_paddingX);
            PAD_Y = res.getDimension(R.dimen.radar_paddingY);
            RADAR_COLOR = res.getColor(R.color.radar_color);
            TEXT_COLOR = res.getColor(R.color.white);
            TEXT_SIZE = res.getDimension(R.dimen.radar_text);

        }
        else {
            initUseDefaultValues();
        }

    }

    private void initUseDefaultValues() {
        Log.i(AppConstants.LOG_TAG, "Radar init use default values");
        LINE_COLOR = Color.argb(150, 0, 0, 220);
        PAD_X = 10;
        PAD_Y = 20;
        RADAR_COLOR = Color.argb(100, 0, 0, 200);
        TEXT_COLOR = Color.rgb(255, 255, 255);
        TEXT_SIZE = 12;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        draw(canvas);
    }

    /**
     * Draw the radar on the given Canvas.
     * 
     * @param canvas
     *            Canvas to draw on.
     * @throws IllegalArgumentException
     *             if Canvas is NULL.
     */
    public void draw(Canvas canvas) {
        if (null == canvas) {
            throw new IllegalArgumentException("canvas is null!");

        }

        // Log.i(AppConstants.LOG_TAG, "Radar -- draw");

        // Update the radar graphics and text based upon the new pitch and
        // bearing
        drawRadarCircle(canvas);
        drawRadarPoints(canvas);
        drawRadarLines(canvas);
        drawRadarText(canvas);

    }

    private void drawRadarCircle(Canvas canvas) {
        if (null == canvas) {
            throw new IllegalArgumentException("canvas is null!");

        }

        if (circleContainer == null) {
            PaintableCircle paintableCircle = new PaintableCircle(RADAR_COLOR,
                    RADIUS, true);
            circleContainer = new PaintablePosition(paintableCircle, PAD_X
                    + RADIUS, PAD_Y + RADIUS, 0, 1);
        }
        circleContainer.paint(canvas);
    }

    private void drawRadarPoints(Canvas canvas) {
        if (null == canvas) {
            throw new IllegalArgumentException("canvas is null!");
        }
        if (radarPoints == null) {
            radarPoints = new PaintableRadarPoints();
        }

        // rotate the points and draw them
        if (pointsContainer == null) {
            pointsContainer = new PaintablePosition(radarPoints, PAD_X, PAD_Y,
                    -GlobalARData.getAzimuth(), 1);
        }
        else {
            pointsContainer.set(radarPoints, PAD_X, PAD_Y,
                    -GlobalARData.getAzimuth(), 1);
        }

        pointsContainer.paint(canvas);
    }

    private void drawRadarLines(Canvas canvas) {
        if (null == canvas) {
            throw new IllegalArgumentException("canvas is null!");

        }

        // Left line
        if (leftLineContainer == null) {
            leftLineContainer = generateRadarLine(-AppConstants.DEFAULT_VIEW_ANGLE_RADIANS / 2);

        }
        leftLineContainer.paint(canvas);

        // Right line
        if (rightLineContainer == null) {
            rightLineContainer = generateRadarLine(AppConstants.DEFAULT_VIEW_ANGLE_RADIANS / 2);

        }
        rightLineContainer.paint(canvas);
    }

    /**
     * Use to generate the Radar angle line, the left and right line, just use
     * different angle.
     * 
     * @param rotateAngle
     * @return
     */
    private PaintablePosition generateRadarLine(float rotateAngle) {
        PaintablePosition radarLine = null;

        ScreenPosition endPosition = new ScreenPosition();
        endPosition.set(0, -RADIUS);
        endPosition.rotate(rotateAngle);
        endPosition.add(PAD_X + RADIUS, PAD_Y + RADIUS);

        float offsetX = endPosition.getX() - (PAD_X + RADIUS);
        float offsetY = endPosition.getY() - (PAD_Y + RADIUS);

        PaintableLine line = new PaintableLine(LINE_COLOR, offsetX, offsetY);

        radarLine = new PaintablePosition(line, PAD_X + RADIUS, PAD_Y + RADIUS,
                0, 1);

        return radarLine;
    }

    private void drawRadarText(Canvas canvas) {
        if (null == canvas) {
            throw new IllegalArgumentException("canvas is null!");

        }

        String dirTxt = generateDirectionTxt();

        int bearing = (int) GlobalARData.getAzimuth();

        // (char)176 is the ASCII code for degree o
        drawRadarText(canvas, "" + bearing + ((char) 176) + " " + dirTxt,
                (PAD_X + RADIUS), (PAD_Y - 5), true);

        // Zoom text
        drawRadarText(canvas, formatDist(GlobalARData.getRadius() * 1000),
                (PAD_X + RADIUS), (PAD_Y + RADIUS * 2 - 10), false);
    }

    private String generateDirectionTxt() {
        // Direction text
        int range = (int) (GlobalARData.getAzimuth() / (360f / 16f));
        String dirTxt = "";
        switch (range) {
        case 15:
        case 0:
            dirTxt = "N";
            break;

        case 1:
        case 2:
            dirTxt = "NE";
            break;
        case 3:
        case 4:
            dirTxt = "E";
            break;
        case 5:
        case 6:
            dirTxt = "SE";
            break;
        case 7:
        case 8:
            dirTxt = "S";
            break;
        case 9:
        case 10:
            dirTxt = "SW";
            break;
        case 11:
        case 12:
            dirTxt = "W";
            break;
        case 13:
        case 14:
            dirTxt = "NW";
            break;

        default:
            break;
        }
        return dirTxt;
    }

    private void drawRadarText(Canvas canvas, String txt, float x, float y,
            boolean bg) {
        if (canvas == null || txt == null) {
            throw new IllegalArgumentException("canvas or txt is null!");
        }

        if (paintableText == null) {
            paintableText = new PaintableText(txt, TEXT_COLOR, TEXT_SIZE, bg);
        }
        else {
            paintableText.set(txt, TEXT_COLOR, TEXT_SIZE, bg);
        }

        if (paintedContainer == null) {
            paintedContainer = new PaintablePosition(paintableText, x, y, 0, 1);
        }
        else {
            paintedContainer.set(paintableText, x, y, 0, 1);
        }

        paintedContainer.paint(canvas);
    }

    private static String formatDist(float meters) {
        if (meters < 1000) {
            return ((int) meters) + "m";
        }
        else if (meters < 10000) {
            return formatDec(meters / 1000f, 1) + "km";
        }
        else {
            return ((int) (meters / 1000f)) + "km";
        }
    }

    private static String formatDec(float val, int dec) {
        int factor = (int) Math.pow(10, dec);

        int front = (int) (val);
        int back = (int) Math.abs(val * (factor)) % factor;

        return front + "." + back;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        postInvalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onZoomChanged() {
        postInvalidate();
    }

}
