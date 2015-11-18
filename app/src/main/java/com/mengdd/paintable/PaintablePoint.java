package com.mengdd.paintable;

import android.graphics.Canvas;

/**
 * This class extends PaintableObject and draws a small rectangle.
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
public class PaintablePoint extends PaintableObject {

    private static int width = 2;
    private static int height = 2;

    private int color = 0;
    private boolean fill = false;

    public PaintablePoint(int color, boolean fill) {
        set(color, fill);
    }

    /**
     * Set this objects parameters. This should be used instead of creating new
     * objects.
     * 
     * @param color
     *            Color to set the rectangle representing this Point.
     * @param fill
     *            Fill color to set the rectangle representing this Point.
     */
    public void set(int color, boolean fill) {
        this.color = color;
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
        paintRect(canvas, -width / 2, -height / 2, width, height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getWidth() {
        return width;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getHeight() {
        return height;
    }
}
