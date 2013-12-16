package com.mengdd.camera;

import com.mengdd.utils.AppConstants;

public class CameraData {
    private float mViewAngle = AppConstants.DEFAULT_VIEW_ANGLE_RADIANS;
    private float mDistance = 0;
    private int mWidth = 0;
    private int mHeight = 0;

    public CameraData() {

    }

    public CameraData(int width, int height) {
        setCameraData(width, height);
    }

    public CameraData(int width, int height, float viewAngle) {
        setCameraData(width, height, viewAngle);
    }

    public void setCameraData(int width, int height) {
        setCameraData(width, height, AppConstants.DEFAULT_VIEW_ANGLE_RADIANS);
    }

    public void setCameraData(int width, int height, float viewAngle) {

        mWidth = width;
        mHeight = height;
        mViewAngle = viewAngle;
        mDistance = (mWidth / 2) / (float) Math.tan(mViewAngle / 2);
    }

    public float getViewAngle() {
        return mViewAngle;
    }

    public void setViewAngle(float mViewAngle) {
        this.mViewAngle = mViewAngle;
    }

    public float getDistance() {
        return mDistance;
    }

    public void setDistance(float mDistance) {
        this.mDistance = mDistance;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int mHeight) {
        this.mHeight = mHeight;
    }

}
