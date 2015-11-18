package com.mengdd.paintable;

import android.graphics.Canvas;
import android.graphics.Color;

/**
 * This class extends PaintableObject to draw an outlined box.
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
public class PaintableBox extends PaintableObject {

    private float width = 0, height = 0;
    private int borderColor = Color.rgb(255, 255, 255);
    private int backgroundColor = Color.argb(128, 0, 0, 0);

    public PaintableBox(float width, float height) {
        this(width, height, Color.rgb(255, 255, 255), Color.argb(128, 0, 0, 0));
    }

    public PaintableBox(float width, float height, int borderColor, int bgColor) {
        set(width, height, borderColor, bgColor);
    }

    /**
     * Set this objects parameters. This should be used instead of creating new
     * objects.
     * 
     * @param width
     *            width of the box.
     * @param height
     *            height of the box.
     */
    public void set(float width, float height) {
        set(width, height, borderColor, backgroundColor);
    }

    /**
     * Set this objects parameters. This should be used instead of creating new
     * objects.
     * 
     * @param width
     *            width of the box.
     * @param height
     *            height of the box.
     * @param borderColor
     *            Color of the border.
     * @param bgColor
     *            Background color of the surrounding box.
     */
    public void set(float width, float height, int borderColor, int bgColor) {
        this.width = width;
        this.height = height;
        this.borderColor = borderColor;
        this.backgroundColor = bgColor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint(Canvas canvas) {
        if (null == canvas) {
            throw new IllegalArgumentException("canvas is null!");
        }

        setFill(true);
        setColor(backgroundColor);
        paintRect(canvas, 0, 0, width, height);

        setFill(false);
        setColor(borderColor);
        paintRect(canvas, 0, 0, width, height);
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
