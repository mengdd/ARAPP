package com.mengdd.poi.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.TreeSet;

import com.mengdd.arapp.GlobalARData;
import com.mengdd.poi.ui.RadarZoomController.OnRadarZoomChangedListener;
import com.mengdd.utils.AppConstants;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.hardware.Camera.OnZoomChangeListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * This class will represent POI markers and Radar Icon
 * 
 * The source is adapted from AugmentedView in :
 * "android-augment-reality-framework" project link:
 * http://code.google.com/p/android-augment-reality-framework/
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 */
public class MarkersOverlayView extends View implements SensorEventListener,
        OnRadarZoomChangedListener {

    private static final float[] tempLocationArray = new float[3];
    private static final List<BasicMarker> cache = new ArrayList<BasicMarker>();
    private static final TreeSet<BasicMarker> updated = new TreeSet<BasicMarker>();
    private static final int COLLISION_ADJUSTMENT = 100;

    public MarkersOverlayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public MarkersOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MarkersOverlayView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {

    }

    private int debugRadarCount = 0;
    private int debugViewCount = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        Log.i(AppConstants.LOG_TAG, "onTouchEvent: X: " + x + ",Y: " + y);
        for (BasicMarker marker : GlobalARData.getMarkers()) {
            if (marker.handleClick(x, y)) {
                marker.onTouch();
            }

        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Log.i(AppConstants.LOG_TAG, "ARPOIView -- onDraw");
        if (canvas == null) {
            return;

        }

        // Get all the markers
        List<BasicMarker> collection = GlobalARData.getMarkers();
        Log.w(AppConstants.LOG_TAG, "markers count all: " + collection.size());
        // Prune all the markers that are out of the radar's radius (speeds
        // up drawing and collision detection)
        cache.clear();

        debugRadarCount = 0;
        debugViewCount = 0;
        for (BasicMarker m : collection) {

            m.update(canvas, 0, 0);

            if (m.isOnRadar()) {
                debugRadarCount++;
            }

            if (m.isInView()) {
                debugViewCount++;
            }
            if (m.isOnRadar() && m.isInView()) {
                cache.add(m);
            }
        }
        collection = cache;
        Log.i(AppConstants.LOG_TAG, "isOnRadar: " + debugRadarCount
                + ", isInView: " + debugViewCount);
        Log.i(AppConstants.LOG_TAG,
                "marker count in collection: " + collection.size());

        adjustForCollisions(canvas, collection);

        // Draw AR markers in reverse order since the last drawn should be
        // the closest
        ListIterator<BasicMarker> iter = collection.listIterator(collection
                .size());
        while (iter.hasPrevious()) {
            BasicMarker marker = iter.previous();
            marker.draw(canvas);
        }

        // GlobalARData.logMarkers();

    }

    private static void adjustForCollisions(Canvas canvas,
            List<BasicMarker> collection) {
        updated.clear();

        // Update the AR markers for collisions
        for (BasicMarker marker1 : collection) {
            if (updated.contains(marker1) || !marker1.isInView()) {
                continue;
            }

            int collisions = 1;
            for (BasicMarker marker2 : collection) {
                if (marker1.equals(marker2) || updated.contains(marker2)
                        || !marker2.isInView()) {
                    continue;
                }

                if (marker1.isMarkerOnMarker(marker2)) {

                    // float h = collisions * COLLISION_ADJUSTMENT;
                    float h = collisions * marker2.getHeight();
                    marker2.update(canvas, 0, -h);

                    collisions++;
                    updated.add(marker2);
                }
            }
            updated.add(marker1);
        }
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
