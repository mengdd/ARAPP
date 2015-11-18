package com.mengdd.camera;

import java.util.List;

import com.mengdd.utils.AppConstants;
import com.mengdd.utils.LogUtils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 *
 * The Camera Preview class extends SurfaceView, showing the real scene throungh
 * camera.
 *
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 *
 */
public class CameraPreview extends SurfaceView implements
        SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Size mPreviewSize;
    private List<Size> mSupportedPreviewSizes;
    private int mRotationDegree = 90;

    public int getDegree() {
        return mRotationDegree;
    }

    public void setDegree(int degree) {
        this.mRotationDegree = degree;
    }

    public CameraPreview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CameraPreview(Context context) {
        super(context);
        init();
    }

    @SuppressWarnings("deprecation")
    private void init() {
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.setFormat(PixelFormat.TRANSLUCENT);

    }

    public void setCamera(Camera camera) {

        mCamera = camera;
        if (null != mCamera) {
            mSupportedPreviewSizes = mCamera.getParameters()
                    .getSupportedPreviewSizes();
            requestLayout();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the
        // preview.

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {

        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (null == mHolder.getSurface()) {
            // preview surface does not exist
            return;
        }

        stopCameraPreview();

        // set preview size and make any resize, rotate or
        // reformatting changes here

        if (null != mCamera) {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);

            requestLayout();

            mCamera.setParameters(parameters);

            mCamera.setDisplayOrientation(mRotationDegree);
            LogUtils.d(
                    AppConstants.LOG_TAG,
                    "camera set parameters successfully!: "
                            + parameters.getPreviewSize());

        }

        startCameraPreview();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (null != mCamera) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

    }

    private void startCameraPreview() {

        try {
            if (null != mCamera) {

                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            }

        }
        catch (Exception e) {
            mCamera.release();
            mCamera = null;
        }

    }

    private void stopCameraPreview() {

        try {
            if (null != mCamera) {

                mCamera.stopPreview();
            }
        }
        catch (Exception e) {
            // ignore: tried to stop a non-existent preview
            e.printStackTrace();

        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // We purposely disregard child measurements because act as a
        // wrapper to a SurfaceView that centers the camera preview instead
        // of stretching it.
        final int width = resolveSize(getSuggestedMinimumWidth(),
                widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(),
                heightMeasureSpec);
        setMeasuredDimension(width, height);

        if (null != mSupportedPreviewSizes) {
            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width,
                    height);
        }
    }

    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (null == sizes) {
            return null;
        }

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) {
                continue;

            }
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (null == optimalSize) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

}
