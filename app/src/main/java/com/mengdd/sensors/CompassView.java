package com.mengdd.sensors;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;

import com.mengdd.arapp.R;

/**
 * 
 * CompassView show a Compass UI
 * 
 * The codes are adapted from the book: Pro Android Augmented Reality
 * 
 * 
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 * 
 */
public class CompassView extends View {
    private enum CompassDirection {
        N, NNE, NE, ENE, E, ESE, SE, SSE, S, SSW, SW, WSW, W, WNW, NW, NNW
    }

    public enum CompassStatus {
        ParallelToGround, VerticalToGround
    }

    private CompassStatus mCompassStatus = CompassStatus.VerticalToGround;

    public void setCompassStatus(CompassStatus status) {
        mCompassStatus = status;
    }

    private Resources resources;
    private int[] borderGradientColors;
    private float[] borderGradientPositions;
    private int[] glassGradientColors;
    private float[] glassGradientPositions;

    private Paint backgroudPaint;
    private Paint borderPaint;

    private Paint markerPaint;
    private Paint arrowPaint;
    private Paint textPaint;
    private Paint glassCirclePaint;

    private Paint mainDirectionPaint;
    private Paint subDirectionPaint;
    private Paint glassPaint;
    private int textHeight;

    // size info
    private float ringWidth;
    private int height;
    private int width;

    private int px;
    private int py;

    private Point center;
    private int radius = 10;

    private RectF boundingBox;
    private RectF innerBoundingBox;
    private float innerRadius;

    // Shaders
    private RadialGradient borderGradient;
    private RadialGradient glassShader;

    // 3 key angle values
    // update from outside this class, from calculated sensors values
    private float azimuth;
    private float pitch = 0;
    private float roll = 0;

    // angle value after clamp
    private float tiltDegree;
    private float rollDegree;

    public float getBearing() {
        return azimuth;
    }

    public void setBearing(float bearing) {
        this.azimuth = bearing;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public CompassView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initCompassView();
    }

    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCompassView();
    }

    public CompassView(Context context) {
        super(context);
        initCompassView();
    }

    private void initCompassView() {
        setFocusable(true);
        resources = this.getResources();

        initColors();
        initPaints();

    }

    private void initPaints() {

        // draw background
        backgroudPaint = new Paint();
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setColor(resources.getColor(R.color.compass_marker));
        borderPaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(resources.getColor(R.color.compass_text_color));
        textPaint.setFakeBoldText(true);
        textPaint.setSubpixelText(true);
        textPaint.setTextAlign(Align.LEFT);
        textPaint.setTextSize(resources.getDimension(R.dimen.compass_text));

        // text Height get here
        textHeight = (int) textPaint.measureText("yY");

        markerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        markerPaint.setColor(resources.getColor(R.color.compass_marker));
        markerPaint.setAlpha(220);
        markerPaint.setStyle(Paint.Style.STROKE);
        markerPaint.setShadowLayer(2, 1, 1,
                resources.getColor(R.color.compass_marker_shadow));
        markerPaint.setStrokeWidth(2);

        // to paint the Main Direction Text: N S E W
        mainDirectionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mainDirectionPaint.setColor(resources
                .getColor(R.color.compass_direction_text));
        mainDirectionPaint.setFakeBoldText(true);
        mainDirectionPaint.setSubpixelText(true);
        mainDirectionPaint.setTextAlign(Align.LEFT);
        mainDirectionPaint.setTextSize(resources
                .getDimension(R.dimen.compass_main_dir));

        // to paint the sub Direction Text
        subDirectionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        subDirectionPaint.setColor(resources
                .getColor(R.color.compass_sub_direction_text));
        subDirectionPaint.setFakeBoldText(true);
        subDirectionPaint.setSubpixelText(true);
        subDirectionPaint.setTextAlign(Align.LEFT);
        subDirectionPaint.setTextSize(resources
                .getDimension(R.dimen.compass_sub_dir));

        // arrow Paint
        arrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arrowPaint.setColor(resources.getColor(R.color.compass_arrow));
        arrowPaint.setAlpha(250);
        arrowPaint.setStyle(Paint.Style.STROKE);
        arrowPaint.setShadowLayer(5, 1, 1,
                resources.getColor(R.color.compass_arrow_shadow));
        arrowPaint.setStrokeWidth(12);

        // draw glass
        glassPaint = new Paint();

        glassCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        glassCirclePaint.setColor(resources.getColor(R.color.shadow_color));
        glassCirclePaint.setStrokeWidth(2);
        glassCirclePaint.setStyle(Paint.Style.STROKE);

    }

    private void initColors() {
        borderGradientColors = new int[4];
        borderGradientPositions = new float[4];
        borderGradientColors[3] = resources.getColor(R.color.glass_shadow_3);
        // 下面这三个颜色值好像没体现出来，我也不解为何
        borderGradientColors[2] = resources.getColor(R.color.glass_shadow_2);
        borderGradientColors[1] = resources.getColor(R.color.glass_shadow_1);
        borderGradientColors[0] = resources.getColor(R.color.glass_shadow_0);
        borderGradientPositions[3] = 0.0f;
        borderGradientPositions[2] = 1 - 0.03f;
        borderGradientPositions[1] = 1 - 0.06f;
        borderGradientPositions[0] = 1.0f;

        glassGradientColors = new int[5];
        glassGradientPositions = new float[5];
        int glassColor = 245;
        glassGradientColors[4] = Color.argb(65, glassColor, glassColor,
                glassColor);
        glassGradientColors[3] = Color.argb(100, glassColor, glassColor,
                glassColor);
        glassGradientColors[2] = Color.argb(50, glassColor, glassColor,
                glassColor);
        glassGradientColors[1] = Color.argb(0, glassColor, glassColor,
                glassColor);
        glassGradientColors[0] = Color.argb(0, glassColor, glassColor,
                glassColor);
        glassGradientPositions[4] = 1 - 0.0f;
        glassGradientPositions[3] = 1 - 0.06f;
        glassGradientPositions[2] = 1 - 0.10f;
        glassGradientPositions[1] = 1 - 0.20f;
        glassGradientPositions[0] = 1 - 1.0f;

    }

    /**
     * init shaders according to size info set the shaders to some existing
     * Paints
     */
    private void initShaders() {
        borderGradient = new RadialGradient(px, py, radius,
                borderGradientColors, borderGradientPositions, TileMode.CLAMP);
        backgroudPaint.setShader(borderGradient);

        glassShader = new RadialGradient(px, py, (int) innerRadius,
                glassGradientColors, glassGradientPositions, TileMode.CLAMP);
        glassPaint.setShader(glassShader);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // The compass is a circle that fills as much space as possible.
        // Set the measured dimensions by figuring out the shortest boundary,
        // height or width.
        int measuredWidth = measure(widthMeasureSpec);
        int measuredHeight = measure(heightMeasureSpec);
        int d = Math.min(measuredWidth, measuredHeight);
        setMeasuredDimension(d, d);
    }

    private int measure(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.UNSPECIFIED) {
            // Return a default size of 200 if no bounds are specified.
            result = 200;
        }
        else {
            // As you want to fill the available space
            // always return the full available bounds.
            result = specSize;
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        refreshSizeInfos();

        // init Shaders put here because shaders need some size info
        initShaders();

    }

    private void refreshSizeInfos() {
        ringWidth = textHeight + 4;
        height = getMeasuredHeight();
        width = getMeasuredWidth();

        px = width / 2;
        py = height / 2;

        center = new Point(px, py);

        radius = Math.min(px, py) - 2;

        boundingBox = new RectF(center.x - radius, center.y - radius, center.x
                + radius, center.y + radius);
        innerBoundingBox = new RectF(center.x - radius + ringWidth, center.y
                - radius + ringWidth, center.x + radius - ringWidth, center.y
                + radius - ringWidth);
        innerRadius = innerBoundingBox.height() / 2;

    }

    private void updateRotationInfos() {
        // angle
        tiltDegree = pitch;
        while (tiltDegree > 90 || tiltDegree < -90) {
            if (tiltDegree > 90)
                tiltDegree = -90 + (tiltDegree - 90);
            if (tiltDegree < -90)
                tiltDegree = 90 - (tiltDegree + 90);
        }
        rollDegree = roll;
        while (rollDegree > 180 || rollDegree < -180) {
            if (rollDegree > 180)
                rollDegree = -180 + (rollDegree - 180);
            if (rollDegree < -180)
                rollDegree = 180 - (rollDegree + 180);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        updateRotationInfos();

        drawBackground(canvas, backgroudPaint, borderPaint);
        drawTickMarks(canvas, textPaint, markerPaint);

        if (CompassStatus.VerticalToGround == mCompassStatus) {
            drawDirection(canvas, false, mainDirectionPaint, subDirectionPaint,
                    arrowPaint);
            drawArrow(canvas, arrowPaint);
        }
        else {
            drawDirection(canvas, true, mainDirectionPaint, subDirectionPaint,
                    arrowPaint);

        }

        drawGlass(canvas, glassPaint, glassCirclePaint);
    }

    private void drawBackground(Canvas canvas, Paint bgPaint, Paint borderPaint) {

        Path outerRingPath = new Path();
        outerRingPath.addOval(boundingBox, Direction.CW);
        canvas.drawPath(outerRingPath, bgPaint);

        // draw the circle
        borderPaint.setStrokeWidth(3);
        canvas.drawOval(boundingBox, borderPaint);
        borderPaint.setStrokeWidth(2);
        canvas.drawOval(innerBoundingBox, borderPaint);
    }

    private void drawArrow(Canvas canvas, Paint paint) {
        // draw an Arrow always point at the phone's head direction

        int arrowSize = 25;
        int borderOffset = 30;

        Path rollArrow = new Path();
        rollArrow.moveTo(center.x - arrowSize, (int) innerBoundingBox.top
                + borderOffset);
        rollArrow.lineTo(center.x, (int) innerBoundingBox.top + borderOffset
                - arrowSize);
        rollArrow.lineTo(center.x + arrowSize, innerBoundingBox.top
                + borderOffset);
        // rollArrow.lineTo(center.x, innerBoundingBox.top + borderOffset -
        // arrowSize);
        rollArrow.close();
        canvas.drawPath(rollArrow, paint);
    }

    private void drawTickMarks(Canvas canvas, Paint textPaint, Paint markerPaint) {

        canvas.save();
        // canvas.rotate(180, center.x, center.y);
        canvas.rotate(180 - 1 * (azimuth), px, py);
        for (int i = -180; i < 180; i += 10) {
            if (i % 30 == 0) {
                // the degree marker Text
                String rollString = String.valueOf(i * -1);
                float rollStringWidth = textPaint.measureText(rollString);
                PointF rollStringCenter = new PointF(center.x - rollStringWidth
                        / 2, innerBoundingBox.top + 1 + textHeight);
                canvas.drawText(rollString, rollStringCenter.x,
                        rollStringCenter.y, textPaint);

                // the degree of i % 30 == 0 has longer marker
                canvas.drawLine(center.x, (int) innerBoundingBox.top, center.x,
                        (int) innerBoundingBox.top + 10, markerPaint);
            }
            else {
                canvas.drawLine(center.x, (int) innerBoundingBox.top, center.x,
                        (int) innerBoundingBox.top + 5, markerPaint);
            }
            canvas.rotate(10, center.x, center.y);
        }

        canvas.restore();
    }

    private void drawDirection(Canvas canvas, boolean showArrow,
            Paint mainPaint, Paint subPaint, Paint arrowPaint) {

        canvas.save();
        canvas.rotate(-1 * (azimuth), px, py);

        // 绘制指南针
        if (showArrow) {
            drawNorthArrow(canvas, arrowPaint);
        }

        double increment = 22.5;
        for (double i = 0; i < 360; i += increment) {
            CompassDirection cd = CompassDirection.values()[(int) (i / 22.5)];
            String headString = cd.toString();
            float headStringWidth;
            PointF headStringCenter;

            // Log.i(AppConstants.LOG_TAG, "test: i: " + i);
            if (i % (4 * increment) == 0) {
                // main Direction Text
                headStringWidth = mainPaint.measureText(headString);
                headStringCenter = new PointF(center.x - (headStringWidth / 2),
                        boundingBox.top + 1 + textHeight);

                // Log.i(AppConstants.LOG_TAG,
                // "main direction center: stringWidth: " + headStringWidth
                // +"centerX: "+ headStringCenter.x);
                canvas.drawText(headString, headStringCenter.x,
                        headStringCenter.y - 8, mainPaint);
            }
            else {
                // Sub Direction Text
                headStringWidth = subPaint.measureText(headString);
                headStringCenter = new PointF(center.x - (headStringWidth / 2),
                        boundingBox.top + 1 + textHeight);
                // Log.i(AppConstants.LOG_TAG, "sub direction center:" +
                // headStringCenter.x);
                canvas.drawText(headString, headStringCenter.x,
                        headStringCenter.y - 10, subPaint);
            }
            canvas.rotate((int) increment, center.x, center.y);
        }
        canvas.restore();
    }

    private void drawNorthArrow(Canvas canvas, Paint paint) {

        int arrowSize = 25;
        int borderOffset = 70;

        Path rollArrow = new Path();
        rollArrow.moveTo(center.x - arrowSize, (int) innerBoundingBox.top
                + borderOffset);
        rollArrow.lineTo(center.x, (int) innerBoundingBox.top + borderOffset
                - arrowSize);
        rollArrow.moveTo(center.x + arrowSize, innerBoundingBox.top
                + borderOffset);
        rollArrow.lineTo(center.x, innerBoundingBox.top + borderOffset
                - arrowSize);
        canvas.drawPath(rollArrow, paint);

        canvas.drawLine(px, py + innerRadius - (borderOffset - arrowSize), px,
                py - innerRadius + (borderOffset - arrowSize), paint);

    }

    private void drawGlass(Canvas canvas, Paint glassPaint, Paint circlePaint) {
        canvas.drawOval(innerBoundingBox, glassPaint);

        // draw 2 circle
        circlePaint.setStrokeWidth(1);
        canvas.drawOval(boundingBox, circlePaint);
        circlePaint.setStrokeWidth(2);
        canvas.drawOval(innerBoundingBox, circlePaint);

    }
}
