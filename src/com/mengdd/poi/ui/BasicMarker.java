package com.mengdd.poi.ui;

import java.text.DecimalFormat;

import com.baidu.location.BDLocation;
import com.mengdd.arapp.GlobalARData;
import com.mengdd.camera.CameraData;
import com.mengdd.paintable.PaintableBox;
import com.mengdd.paintable.PaintableBoxedText;
import com.mengdd.paintable.PaintableGps;
import com.mengdd.paintable.PaintableIcon;
import com.mengdd.paintable.PaintableObject;
import com.mengdd.paintable.PaintablePoint;
import com.mengdd.paintable.PaintablePosition;
import com.mengdd.utils.AppConstants;
import com.mengdd.utils.MathUtils;
import com.mengdd.utils.Matrix;
import com.mengdd.utils.Vector;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.util.FloatMath;
import android.util.Log;

/**
 * This class will represent a physical location and will calculate it's
 * visibility and draw it's text and visual representation accordingly. This
 * should be extended if you want to change the way a Marker is viewed.
 * 
 * The source is adapted from: 1."android-augment-reality-framework" project
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
public abstract class BasicMarker implements Comparable<BasicMarker> {
    public enum MarkerType {
        GoogleMarker, BaiduMarker
    }

    protected MarkerType mMarkerType = null;

    // Used to show exact GPS position
    protected boolean debugGpsPosition = false;

    // Used to debug the touching mechanism
    protected boolean debugTouchZone = false;

    protected static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat(
            "@#");

    // Marker's default color
    protected int color = Color.WHITE;
    protected int colorBackup = Color.WHITE;
    private boolean isBeingTouched = false;

    // Container for the circle or icon symbol
    protected PaintableObject gpsSymbol = null;

    protected float initialY = 0.0f;

    // Is in the camera's view
    protected volatile boolean isInView = false;

    // Is within the radar
    protected volatile boolean isOnRadar = false;

    protected float radarPointScale = 1.0f;
    protected float radarPointScaleBackup = 1.0f;

    // a temperary container
    protected final float[] mTempLocationArray = new float[3];

    protected CameraData mCameraData = null;

    // Distance from camera to PhysicalLocation in meters
    protected double mDistance = 0.0;

    // Physical location's X, Y, Z relative to the device's location
    protected Vector mLocationVector = new Vector();

    // the direction Vector, ie, the normal form of LocationVector
    protected Vector mDirectionVector = new Vector();

    // private volatile static CameraModel cam = null;

    // Unique identifier of Marker
    protected String mName = null;

    // marker's X,Y,Z on camera, mLocationVector after projection
    protected Vector mScreenVector = new Vector();
    // For tracking Markers which have no altitude
    protected boolean noAltitude = false;

    // position
    protected volatile PaintablePosition positionContainer = null;
    protected PaintablePoint positionPoint = null;
    // Container for position text
    protected PaintableBoxedText positionTextBox = null;
    protected volatile PaintablePosition positionTextContainer = null;

    protected volatile PaintablePosition symbolContainer = null;
    // Container for text
    protected PaintableBoxedText textBox = null;
    protected volatile PaintablePosition textContainer = null;

    // Touch
    protected PaintableBox touchBox = null;
    protected volatile PaintablePosition touchPosition = null;

    // IconMarker has one, other is null
    protected Bitmap iconBitmap = null;

    protected int iconSize = 0;
    protected final int ICON_SIZE_MAX = 128;
    protected final int ICON_SIZE_MIN = 56;
    protected float textSize = 0;
    protected final float TEXT_SIZE_MAX = 32;
    protected final float TEXT_SIZE_MIN = 24;

    // when mDistance < ICON_DISTANCE_MIN, the iconSize and textSize are Max
    // Values
    protected final float ICON_DISTANCE_MIN = 100;
    // when mDistance > ICON_DISTANCE_MAX, the iconSize and textSize are Min
    // Values
    protected final float ICON_DISTANCE_MAX = 2100;

    // when ICON_DISTANCE_MIN < mDistance < ICON_DISTANCE_MAX, the iconSize and
    // textSize are Linear interpolation values between Max and Min

    private float collisionOffsetX = 0;
    private float collisionOffsetY = 0;

    public BasicMarker() {
    }

    public synchronized void set(String name, int color, Bitmap icon) {
        this.mName = name;
        this.color = color;
        this.colorBackup = color;
        this.iconBitmap = icon;
        this.isOnRadar = false;
        this.isInView = false;
        this.mLocationVector.set(0, 0, 0);
        this.initialY = 0.0f;

    }

    /**
     * Calculate the relative position of this Marker from the given Location.
     * 
     */
    public void updateRelativePosition(Location origLocation,
            BDLocation origBDLocation) {
        calcRelativePosition(origLocation, origBDLocation);
        // Update the markers distance based on the new location.
        updateDistance(origLocation, origBDLocation);

        updateDirection();
        updateSizeInfo();

    }

    protected abstract void calcRelativePosition(Location origLocation,
            BDLocation origBDLocation);

    protected abstract void updateDistance(Location origLocation,
            BDLocation origBDLocation);

    private void updateDirection() {
        mDirectionVector.set(mLocationVector);
        mDirectionVector.norm();
    }

    private void updateSizeInfo() {
        if (mDistance <= ICON_DISTANCE_MIN) {
            iconSize = ICON_SIZE_MAX;
            textSize = TEXT_SIZE_MAX;

        }
        else if (mDistance >= ICON_DISTANCE_MAX) {
            iconSize = ICON_SIZE_MIN;
            textSize = TEXT_SIZE_MIN;

        }
        else {
            float ratio = 1 - (float) (mDistance - ICON_DISTANCE_MIN)
                    / (ICON_DISTANCE_MAX - ICON_DISTANCE_MIN);

            iconSize = ICON_SIZE_MIN
                    + Math.round(ratio * (ICON_SIZE_MAX - ICON_SIZE_MIN));
            textSize = TEXT_SIZE_MIN
                    + Math.round(ratio * (TEXT_SIZE_MAX - TEXT_SIZE_MIN));

        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized int compareTo(BasicMarker another) {
        if (null == another) {
            throw new IllegalArgumentException("another marker is null!");
        }

        return mName.compareTo(another.getName());
    }

    /**
     * Draw this Marker on the Canvas
     * 
     * @param canvas
     *            Canvas to draw on.
     * @throws NullPointerException
     *             if the Canvas is NULL.
     */
    public synchronized void draw(Canvas canvas) {
        if (null == canvas) {
            throw new IllegalArgumentException("canvas is null !");
        }

        // If not visible then do nothing
        if (!isOnRadar || !isInView) {
            return;
        }

        // Draw the Icon and Text
        if (debugTouchZone) {
            drawTouchZone(canvas);
        }
        drawIcon(canvas);
        drawText(canvas);

        // Draw the exact position
        if (debugGpsPosition) {
            drawPosition(canvas);
            drawPositionText(canvas);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean equals(Object marker) {
        if (marker == null || mName == null) {
            throw new IllegalArgumentException(
                    "another marker or mName is null!");
        }

        return mName.equals(((BasicMarker) marker).getName());
    }

    /**
     * Get the color of this Marker.
     * 
     * @return int representing the Color of this Marker.
     */
    public synchronized int getColor() {
        return this.color;
    }

    public synchronized void setColor(int color) {
        this.color = color;

    }

    public float getRadarPointScale() {
        return radarPointScale;
    }

    public void setRadarPointScale(float radarPointScale) {
        this.radarPointScale = radarPointScale;

    }

    /**
     * Get the distance of this Marker from the current GPS position.
     * 
     * @return double representing the distance of this Marker from the current
     *         GPS position.
     */
    public synchronized double getDistance() {
        return this.mDistance;
    }

    public synchronized float getHeight() {
        if (symbolContainer == null || textContainer == null) {
            return 0f;
        }
        return symbolContainer.getHeight() + textContainer.getHeight();
    }

    /**
     * Get the initial Y coordinate of this Marker. Used to reset after
     * collision detection.
     * 
     * @return float representing the initial Y coordinate of this Marker.
     */
    public synchronized float getInitialY() {
        return this.initialY;
    }

    /**
     * Get the the location of the Marker in XYZ.
     * 
     * @return Vector representing the location of the Marker.
     */
    public Vector getLocationVector() {
        return mLocationVector;
    }

    public Vector getDirectionVector() {
        return mDirectionVector;
    }

    /**
     * Get the name of the Marker.
     * 
     * @return String representing the new of the Marker.
     */
    public synchronized String getName() {
        return this.mName;
    }

    public Vector getScreenPosition() {
        return mScreenVector;
    }

    public synchronized float getWidth() {
        if (symbolContainer == null || textContainer == null) {
            return 0f;
        }
        float symbolWidth = symbolContainer.getWidth();
        float textWidth = textContainer.getWidth();
        return (textWidth > symbolWidth) ? textWidth : symbolWidth;
    }

    /**
     * Tell if the x/y position is on this marker (if the marker is visible)
     * 
     * @param x
     *            float x value.
     * @param y
     *            float y value.
     * @return True if Marker is visible and x/y is on the marker.
     */
    public synchronized boolean handleClick(float x, float y) {
        if (!isOnRadar || !isInView) {
            return false;
        }
        // Log.e("handleClick", "point (x="+x+" y="+y+")");
        boolean result = isPointOnMarker(x, y, this);
        return result;
    }

    public void onTouch() {
        // store the old values
        if (isBeingTouched) {
            return;
        }
        isBeingTouched = true;
        colorBackup = color;
        radarPointScaleBackup = radarPointScale;

        // touch values
        color = Color.RED;
        radarPointScale = 5.0f;

        debugTouchZone = true;

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                onUnTouch();
            }
        }, 2000);
    }

    public void onUnTouch() {
        // restore to old values
        if (isBeingTouched) {
            isBeingTouched = false;
            color = colorBackup;
            radarPointScale = radarPointScaleBackup;
            debugTouchZone = false;
        }

    }

    /**
     * Get the whether the Marker is inside the camera's view
     * 
     * @return True if Marker is inside the camera's view.
     */
    public synchronized boolean isInView() {
        return this.isInView;
    }

    /**
     * Determines if the marker is on this Marker.
     * 
     * @param marker
     *            Marker to test for overlap.
     * @return True if the marker is on Marker.
     */
    public synchronized boolean isMarkerOnMarker(BasicMarker marker) {
        return isMarkerOnMarker(marker, true);
    }

    /**
     * Get the whether the Marker is inside the range (relative to slider on
     * view)
     * 
     * @return True if Marker is inside the range.
     */
    public synchronized boolean isOnRadar() {
        return this.isOnRadar;
    }

    @Override
    public String toString() {
        return "Marker [mName=" + mName + ", mLocationVector="
                + mLocationVector + ", mScreenVector=" + mScreenVector
                + ", mDistance=" + mDistance + ", initialY=" + initialY
                + ", isOnRadar=" + isOnRadar + ", isInView=" + isInView
                + ", noAltitude=" + noAltitude + "]";
    }

    /**
     * Update the matrices and visibility of the Marker.
     * 
     * @param canvas
     *            Canvas to use in the CameraModel.
     * @param addX
     *            Adder to the X position.
     * @param addY
     *            Adder to the Y position.
     */
    public synchronized void update(Canvas canvas, float addX, float addY) {
        if (null == canvas) {
            throw new IllegalArgumentException("canvas is null!");
        }
        if (null == mCameraData) {
            mCameraData = new CameraData(canvas.getWidth(), canvas.getHeight());
        }

        // populateMatrices(addX, addY);

        collisionOffsetX = addX;
        collisionOffsetY = addY;

        updateScreenPosition();

        updateRadar();

        // since updateScreenPosition() method has solved the isInView problem
        // there is no need to call updateView()
        // updateView();
    }

    private void updateScreenPosition() {

        float markerAzimuth = MathUtils.getLocationAzimuth(
                mLocationVector.getX(), mLocationVector.getZ());

        float deviceAzimuth = GlobalARData.getAzimuth();

        float diffAngle = markerAzimuth - deviceAzimuth;

        if (Math.abs(diffAngle) > AppConstants.DEFAULT_VIEW_ANGLE_DEGREES / 2) {
            isInView = false;
        }
        else {
            isInView = true;
            // TODO set an screen position to draw the icon

            double screenX = 0;
            double diffRadians = Math.toRadians(diffAngle);
            // margin: screenX = center + width/2, screenX = center + width/2
            // center: screenX = center

            // formular: screenX = center +
            // sin(diff)*(width/(2*sin(viewAngle/2)))
            // this way: when sin(diff) == 0 --> center
            // when sin(diff) == sin(viewAngle/s) --> center + width /2

            double sinViewAngle = Math
                    .sin(AppConstants.DEFAULT_VIEW_ANGLE_RADIANS / 2);
            double sinDiff = Math.sin(diffRadians);

            screenX = mCameraData.getWidth() / 2 + (sinDiff / sinViewAngle)
                    * mCameraData.getWidth() / 2;

            mScreenVector = new Vector((float) screenX,
                    mCameraData.getHeight() / 2, 0);

            // for collision
            mScreenVector
                    .add(new Vector(collisionOffsetX, collisionOffsetY, 0));
        }

    }

    protected synchronized void drawIcon(Canvas canvas) {
        if (canvas == null) {
            throw new IllegalArgumentException("canvas is null!");
        }

        if (gpsSymbol == null) {
            if (null == iconBitmap) {
                gpsSymbol = new PaintableGps(36, 8, true, getColor());
            }
            else {
                gpsSymbol = new PaintableIcon(iconBitmap, iconSize, iconSize);
            }
        }

        Log.i(AppConstants.LOG_TAG, "iconSize: " + iconSize + ", distance: "
                + mDistance);

        getScreenPosition().get(mTempLocationArray);
        float x = mTempLocationArray[0];
        float y = mTempLocationArray[1];

        y -= gpsSymbol.getHeight() / 2;

        float currentAngle = 0;

        if (null == symbolContainer) {
            symbolContainer = new PaintablePosition(gpsSymbol, x, y,
                    currentAngle, 1);
        }
        else {
            symbolContainer.set(gpsSymbol, x, y, currentAngle, 1);

        }

        symbolContainer.paint(canvas);
    }

    protected synchronized void drawPosition(Canvas canvas) {
        if (canvas == null) {
            throw new IllegalArgumentException("canvas is null!");
        }

        if (positionPoint == null) {
            positionPoint = new PaintablePoint(Color.RED, true);
        }

        getScreenPosition().get(mTempLocationArray);
        float currentAngle = 0;

        if (positionContainer == null) {
            positionContainer = new PaintablePosition(positionPoint,
                    mTempLocationArray[0], mTempLocationArray[1], currentAngle,
                    1);
        }
        else {
            positionContainer.set(positionPoint, mTempLocationArray[0],
                    mTempLocationArray[1], currentAngle, 1);
        }

        positionContainer.paint(canvas);
    }

    protected synchronized void drawText(Canvas canvas) {
        if (canvas == null) {
            throw new IllegalArgumentException("canvas is null!");
        }

        String textStr = null;
        if (mDistance < 1000.0) {
            textStr = mName + " (" + DECIMAL_FORMAT.format(mDistance) + "m)";
        }
        else {
            double d = mDistance / 1000.0;
            textStr = mName + " (" + DECIMAL_FORMAT.format(d) + "km)";
        }

        // int minSize = (canvas.getHeight() < canvas.getWidth()) ? canvas
        // .getHeight() : canvas.getWidth();

        // float maxHeight = Math.round(minSize / 20f) + 1;
        float maxHeight = textSize;

        if (textBox == null) {
            textBox = new PaintableBoxedText(textStr,
                    Math.round(maxHeight / 2f) + 1, 300);
        }
        else {
            textBox.set(textStr, Math.round(maxHeight / 2f) + 1, 300);
        }

        getScreenPosition().get(mTempLocationArray);
        float x = mTempLocationArray[0];
        float y = mTempLocationArray[1];

        x -= textBox.getWidth() / 2;

        float currentAngle = 0;

        if (textContainer == null) {
            textContainer = new PaintablePosition(textBox, x, y, currentAngle,
                    1);
        }
        else {
            textContainer.set(textBox, x, y, currentAngle, 1);
        }

        textContainer.paint(canvas);
    }

    protected synchronized void drawPositionText(Canvas canvas) {
        if (canvas == null) {
            throw new IllegalArgumentException("canvas is null!");
        }

        String textStr = mName + getScreenPosition().toString();

        float maxHeight = Math.round(canvas.getHeight() / 10f) + 1;

        if (positionTextBox == null) {
            positionTextBox = new PaintableBoxedText(textStr,
                    Math.round(maxHeight / 2f) + 1, 300);
        }
        else {
            positionTextBox.set(textStr, Math.round(maxHeight / 2f) + 1, 300);
        }

        getScreenPosition().get(mTempLocationArray);
        float x = mTempLocationArray[0];
        float y = mTempLocationArray[1];

        x -= positionTextBox.getWidth() / 2;

        float currentAngle = 0;

        if (positionTextContainer == null) {
            positionTextContainer = new PaintablePosition(positionTextBox, x,
                    y, currentAngle, 1);
        }
        else {
            positionTextContainer.set(positionTextBox, x, y, currentAngle, 1);
        }

        positionTextContainer.paint(canvas);
    }

    protected synchronized void drawTouchZone(Canvas canvas) {
        if (canvas == null) {
            throw new IllegalArgumentException("canvas is null!");
        }

        if (gpsSymbol == null) {
            return;
        }

        if (touchBox == null) {
            touchBox = new PaintableBox(getWidth(), getHeight(), Color.WHITE,
                    Color.GREEN);
        }
        else {
            touchBox.set(getWidth(), getHeight());
        }

        getScreenPosition().get(mTempLocationArray);
        float x = mTempLocationArray[0];
        float y = mTempLocationArray[1];

        x -= textBox.getWidth() / 2;
        y -= gpsSymbol.getHeight();

        float currentAngle = 0;

        if (touchPosition == null) {
            touchPosition = new PaintablePosition(touchBox, x, y, currentAngle,
                    1);
        }
        else {
            touchPosition.set(touchBox, x, y, currentAngle, 1);
        }
        touchPosition.paint(canvas);
    }

    /**
     * Determines if the marker is on this Marker.
     * 
     * @param marker
     *            Marker to test for overlap.
     * @param reflect
     *            if True the Marker will call it's self recursively with the
     *            opposite arguments.
     * @return True if the marker is on Marker.
     */
    protected synchronized boolean isMarkerOnMarker(BasicMarker marker,
            boolean reflect) {
        if (marker == null)
            return false;

        marker.getScreenPosition().get(mTempLocationArray);
        float x = mTempLocationArray[0];
        float y = mTempLocationArray[1];

        float width = marker.getWidth();
        float height = marker.getHeight();

        x -= width / 2;
        y -= height / 2;

        float middleX = 0;
        float middleY = 0;

        middleX = x + (width / 2);
        middleY = y + (height / 2);

        boolean middleOfMarker = isPointOnMarker(middleX, middleY, this);
        if (middleOfMarker)
            return true;

        float ulX = x;
        float ulY = y;

        float urX = x;
        float urY = y;

        urX += width;

        float llX = x;
        float llY = y;

        llY += height;

        float lrX = x;
        float lrY = y;

        lrX += width;
        lrY += height;

        /*
         * Log.w("isMarkerOnMarker", "name "+this.name);
         * Log.w("isMarkerOnMarker", "ul (x="+(ulX)+" y="+(ulY)+")");
         * Log.w("isMarkerOnMarker", "ur (x="+(urX)+" y="+(urY)+")");
         * Log.w("isMarkerOnMarker", "ll (x="+(llX)+" y="+(llY)+")");
         * Log.w("isMarkerOnMarker", "lr (x="+(lrX)+" y="+(lrY)+")");
         */
        boolean upperLeftOfMarker = isPointOnMarker(ulX, ulY, this);
        if (upperLeftOfMarker)
            return true;

        boolean upperRightOfMarker = isPointOnMarker(urX, urY, this);
        if (upperRightOfMarker)
            return true;

        boolean lowerLeftOfMarker = isPointOnMarker(llX, llY, this);
        if (lowerLeftOfMarker)
            return true;

        boolean lowerRightOfMarker = isPointOnMarker(lrX, lrY, this);
        if (lowerRightOfMarker)
            return true;

        // If reflect is True then reverse the arguments and see if this Marker
        // is on the marker.
        return (reflect) ? marker.isMarkerOnMarker(this, false) : false;
    }

    /**
     * Determines if the point is on this Marker.
     * 
     * @param xPoint
     *            X point.
     * @param yPoint
     *            Y point.
     * @param marker
     *            Marker to determine if the point is on.
     * @return True if the point is on Marker.
     */
    protected synchronized boolean isPointOnMarker(float xPoint, float yPoint,
            BasicMarker marker) {
        if (marker == null)
            return false;

        marker.getScreenPosition().get(mTempLocationArray);
        float x = mTempLocationArray[0];
        float y = mTempLocationArray[1];

        float width = marker.getWidth();
        float height = marker.getHeight();

        x -= width / 2;
        y -= height / 2;

        float ulX = x;
        float ulY = y;

        float lrX = x + width;
        float lrY = y + height;

        /*
         * Log.w("isPointOnMarker", "xPoint="+(xPoint)+" yPoint="+(yPoint));
         * Log.w("isPointOnMarker", "name "+this.name); Log.w("isPointOnMarker",
         * "ul (x="+(ulX)+" y="+(ulY)+")"); Log.w("isPointOnMarker",
         * "lr (x="+(lrX)+" y="+(lrY)+")");
         */

        if (xPoint >= ulX && xPoint <= lrX && yPoint >= ulY && yPoint <= lrY) {
            return true;
        }

        return false;
    }

    // this method is used to get the mScreenVector
    // but since we couldn't get good results from it, we use
    // updateScreenPosition() instead
    private synchronized void populateMatrices(float addX, float addY) {
        Vector tmpVector = new Vector();
        Vector tmpLocationVector = new Vector();

        // Find the location given the rotation matrix

        tmpLocationVector.set(mLocationVector);

        Matrix rotateMatrix = GlobalARData.getRotationMatrix();

        tmpLocationVector.prod(rotateMatrix);

        // projection

        MathUtils.projectPoint(tmpLocationVector, tmpVector,
                mCameraData.getDistance(), mCameraData.getWidth(),
                mCameraData.getHeight(), addX, addY);

        mScreenVector.set(tmpVector);
    }

    private synchronized void updateRadar() {
        isOnRadar = false;

        float range = GlobalARData.getRadius() * 1000;
        float scale = range / RadarView.RADIUS;
        mLocationVector.get(mTempLocationArray);
        float x = mTempLocationArray[0] / scale;
        float y = mTempLocationArray[2] / scale; // z==y Switched on purpose
        if ((x * x + y * y) < (RadarView.RADIUS * RadarView.RADIUS)) {
            isOnRadar = true;
        }
    }

    private synchronized void updateView() {
        isInView = false;

        // If it's not on the radar, can't be in view
        if (!isOnRadar) {
            return;
        }

        // If it's not in the same side as our viewing angle
        mScreenVector.get(mTempLocationArray);
        if (mTempLocationArray[2] >= -1f) {
            return;
        }

        float x = mTempLocationArray[0];
        float y = mTempLocationArray[1];

        float width = getWidth();
        float height = getHeight();

        if (GlobalARData.portrait) {
            x -= height / 2;
            y += width / 2;
        }
        else {
            x -= width / 2;
            y -= height / 2;
        }

        float ulX = x;
        float ulY = y;

        float lrX = x;
        float lrY = y;
        if (GlobalARData.portrait) {
            lrX += height;
            lrY -= width;
        }
        else {
            lrX += width;
            lrY += height;
        }

        if (GlobalARData.portrait
                && (lrX >= -1 && ulX <= mCameraData.getWidth() && ulY >= -1 && lrY <= mCameraData
                        .getHeight())) {
            isInView = true;
        }
        else if (lrX >= -1 && ulX <= mCameraData.getWidth() && lrY >= -1
                && ulY <= mCameraData.getHeight()) {
            isInView = true;
        }

        /*
         * Log.w("updateView", "name "+this.name); Log.w("updateView",
         * "ul (x="+(ulX)+" y="+(ulY)+")"); Log.w("updateView",
         * "lr (x="+(lrX)+" y="+(lrY)+")"); Log.w("updateView",
         * "cam (w="+(cam.getWidth())+" h="+(cam.getHeight())+")"); if
         * (!isInView) Log.w("updateView", "isInView "+isInView); else
         * Log.e("updateView", "isInView "+isInView);
         */

    }

}
