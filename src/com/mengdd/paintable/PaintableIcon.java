package com.mengdd.paintable;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * This class extends PaintableObject to draw an icon.
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
public class PaintableIcon extends PaintableObject {

    private Bitmap bitmap = null;

    public PaintableIcon(Bitmap bitmap, int width, int height) {
        set(bitmap, width, height);
    }

    /**
     * Set the bitmap. This should be used instead of creating new objects.
     * 
     * @param bitmap
     *            Bitmap that should be rendered.
     * @throws IllegalArgumentException
     *             if Bitmap is NULL.
     */
    public void set(Bitmap bitmap, int width, int height) {
        if (null == bitmap) {
            throw new IllegalArgumentException("bitmap is null!");
        }

        this.bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint(Canvas canvas) {
        if (null == canvas || null == bitmap) {
            throw new IllegalArgumentException("canvas or bitmap is null!");
        }

        paintBitmap(canvas, bitmap, -(bitmap.getWidth() / 2),
                -(bitmap.getHeight() / 2));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getWidth() {
        return bitmap.getWidth();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getHeight() {
        return bitmap.getHeight();
    }
}
