package com.mengdd.poi.data;

import android.util.FloatMath;

/**
 * This class is used mostly as a utility to calculate relative positions.
 * 
 * The source of the codes: 1."android-augment-reality-framework" project link:
 * http://code.google.com/p/android-augment-reality-framework/
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
public class ScreenPosition {

    private float x = 0f;
    private float y = 0f;

    public ScreenPosition() {
        set(0, 0);
    }

    /**
     * Set method for X and Y. Should be used instead of creating new objects.
     * 
     * @param x
     *            X position.
     * @param y
     *            Y position.
     */
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the X position.
     * 
     * @return Float X position.
     */
    public float getX() {
        return x;
    }

    /**
     * Set the X position.
     * 
     * @param x
     *            Float X position.
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Get the Y position.
     * 
     * @return Float Y position.
     */
    public float getY() {
        return y;
    }

    /**
     * Set the Y position.
     * 
     * @param y
     *            Float Y position.
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Rotate the positions around the angle t.
     * 
     * @param t
     *            Angle to rotate around.
     */
    public void rotate(float t) {
        float xp = FloatMath.cos(t) * x - FloatMath.sin(t) * y;
        float yp = FloatMath.sin(t) * x + FloatMath.cos(t) * y;
        x = xp;
        y = yp;
    }

    /**
     * Add the X and Y to the positions X and Y.
     * 
     * @param x
     *            Float X to add to X.
     * @param y
     *            Float Y to add to Y.
     */
    public void add(float x, float y) {
        this.x += x;
        this.y += y;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "< x=" + x + " y=" + y + " >";
    }
}
