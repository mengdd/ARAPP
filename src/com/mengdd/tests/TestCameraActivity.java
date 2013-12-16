package com.mengdd.tests;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.mengdd.arapp.R;
import com.mengdd.camera.CameraViewModel;
import com.mengdd.utils.LogUtils;

/**
 * Test Activity for Camera Module.
 *
 *
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 *
 */
public class TestCameraActivity extends Activity {
    private CameraViewModel mCameraViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCameraViewModel = new CameraViewModel(this);
        mCameraViewModel.onCreate(savedInstanceState);

        setContentView(R.layout.test_camera);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.camera_frame);
        frameLayout.addView(mCameraViewModel.getView(), 0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraViewModel.onResume(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCameraViewModel.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCameraViewModel.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraViewModel.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        LogUtils.i("hello world");
        mCameraViewModel.onConfigurationChanged(newConfig);
    }

}
