package com.mengdd.paintable;

import com.mengdd.arapp.GlobalARData;
import com.mengdd.poi.ui.BasicMarker;
import com.mengdd.poi.ui.RadarView;

import android.graphics.Canvas;

/**
 * This class extends PaintableObject to draw all the Markers at their
 * appropriate locations.
 * 
 * The source of the codes: 1."android-augment-reality-framework" project link:
 * http://code.google.com/p/android-augment-reality-framework/
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
public class PaintableRadarPoints extends PaintableObject {

    private final float[] locationArray = new float[3];
    private PaintablePoint paintablePoint = null;
    private PaintablePosition pointContainer = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint(Canvas canvas) {
        if (null == canvas) {
            throw new IllegalArgumentException("canvas is null!");

        }

        // Draw the markers in the circle
        float range = GlobalARData.getRadius() * 1000;
        float scale = range / RadarView.RADIUS;

        for (BasicMarker marker : GlobalARData.getMarkers()) {
            marker.getLocationVector().get(locationArray);
            float x = locationArray[0] / scale;
            float y = locationArray[2] / scale;
            if ((x * x + y * y) < (RadarView.RADIUS * RadarView.RADIUS)) {
                if (paintablePoint == null) {
                    paintablePoint = new PaintablePoint(marker.getColor(), true);
                }
                else {
                    paintablePoint.set(marker.getColor(), true);
                }

                float radarPointScale = marker.getRadarPointScale();
                // here translate the Radar point
                if (pointContainer == null) {
                    pointContainer = new PaintablePosition(paintablePoint, (x
                            + RadarView.RADIUS - 1),
                            (y + RadarView.RADIUS - 1), 0, radarPointScale);
                }
                else {
                    pointContainer.set(paintablePoint,
                            (x + RadarView.RADIUS - 1),
                            (y + RadarView.RADIUS - 1), 0, radarPointScale);
                }

                pointContainer.paint(canvas);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getWidth() {
        return RadarView.RADIUS * 2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getHeight() {
        return RadarView.RADIUS * 2;
    }
}
