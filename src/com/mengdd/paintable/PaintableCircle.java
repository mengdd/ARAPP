package com.mengdd.paintable;

import android.graphics.Canvas;

/**
 * This class extends PaintableObject to draw a circle with a given radius.
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
public class PaintableCircle extends PaintableObject {

    private int color = 0;
    private float radius = 0;
    private boolean fill = false;

    public PaintableCircle(int color, float radius, boolean fill) {
        set(color, radius, fill);
    }

    /**
     * Set the objects parameters. This should be used instead of creating new
     * objects.
     * 
     * @param color
     *            Color of the circle.
     * @param radius
     *            Radius of the circle.
     * @param fill
     *            Fill color of the circle.
     */
    public void set(int color, float radius, boolean fill) {
        this.color = color;
        this.radius = radius;
        this.fill = fill;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint(Canvas canvas) {
        if (null == canvas) {
            throw new IllegalArgumentException("canvas is null!");

        }

        setFill(fill);
        setColor(color);
        paintCircle(canvas, 0, 0, radius);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getWidth() {
        return radius * 2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getHeight() {
        return radius * 2;
    }
}
