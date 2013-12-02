package com.mengdd.paintable;

import android.graphics.Canvas;

/**
 * This class extends PaintableObject to draw a circle with a given radius and a
 * stroke width.
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
public class PaintableGps extends PaintableObject {

    private static final int FRAME_SIZE = 15;

    private float radius = 0;
    private float strokeWidth = 0;
    private boolean fill = false;
    private int color = 0;

    public PaintableGps(float radius, float strokeWidth, boolean fill, int color) {
        set(radius, strokeWidth, fill, color);
    }

    /**
     * Set this objects parameters. This should be used instead of creating new
     * objects.
     * 
     * @param radius
     *            Radius of the circle representing the GPS position.
     * @param strokeWidth
     *            Stroke width of the text representing the GPS position.
     * @param fill
     *            Fill color of the circle representing the GPS position.
     * @param color
     *            Color of the circle representing the GPS position.
     */
    public void set(float radius, float strokeWidth, boolean fill, int color) {
        this.radius = radius;
        this.strokeWidth = strokeWidth;
        this.fill = fill;
        this.color = color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint(Canvas canvas) {
        if (null == canvas) {
            throw new IllegalArgumentException("canvas is null!");
        }

        setStrokeWidth(strokeWidth);
        setFill(fill);
        setColor(color);
        paintCircle(canvas, 0, 0, radius);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getWidth() {
        return (radius * 2) + FRAME_SIZE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getHeight() {
        return (radius * 2) + FRAME_SIZE;
    }
}
