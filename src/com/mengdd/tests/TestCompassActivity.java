package com.mengdd.tests;

import com.mengdd.arapp.R;
import com.mengdd.sensors.CompassViewModel;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

/**
 * Test Activity for CompassViewModel.
 * 
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 * 
 */
public class TestCompassActivity extends Activity {
    private CompassViewModel mCompassViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_compass);

        mCompassViewModel = new CompassViewModel(this);
        mCompassViewModel.onCreate(null);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.main_frame);

        frameLayout.addView(mCompassViewModel.getView(), 0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCompassViewModel.onResume(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCompassViewModel.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCompassViewModel.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mCompassViewModel.onDestroy();
    }

}
