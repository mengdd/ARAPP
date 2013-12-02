package com.mengdd.paintable;

import android.graphics.Canvas;

/**
 * This class extends PaintableObject to draw a line.
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
public class PaintableLine extends PaintableObject {

    private int color = 0;
    private float x = 0;
    private float y = 0;

    public PaintableLine(int color, float x, float y) {
        set(color, x, y);
    }

    /**
     * Set this objects parameters. This should be used instead of creating new
     * objects.
     * 
     * @param color
     *            Color of the line.
     * @param x
     *            X coordinate of the line.
     * @param y
     *            Y coordinate of the line.
     */
    public void set(int color, float x, float y) {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint(Canvas canvas) {
        if (null == canvas) {
            throw new IllegalArgumentException("canvas is null!");
        }

        setFill(false);
        setColor(color);
        paintLine(canvas, 0, 0, x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getWidth() {
        return x;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getHeight() {
        return y;
    }
}
