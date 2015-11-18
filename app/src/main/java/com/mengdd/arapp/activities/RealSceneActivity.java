package com.mengdd.arapp.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import com.mengdd.arapp.R;
import com.mengdd.camera.CameraViewModel;
import com.mengdd.components.ViewModel;
import com.mengdd.sensors.CompassViewModel;
import com.mengdd.tests.TestAugmentedPOIActivity;

/**
 *
 * This class is the first activity for the Project as an independent and
 * functional App.
 *
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 *
 */
public class RealSceneActivity extends Activity {

    private ToggleButton mCompassSwitchBtn = null;

    private List<ViewModel> mViewModels = null;
    private CameraViewModel mCameraViewModel = null;
    private CompassViewModel mCompassViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // no title and fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.first);

        mViewModels = new ArrayList<ViewModel>();

        mCameraViewModel = new CameraViewModel(this);
        mCompassViewModel = new CompassViewModel(this);

        mViewModels.add(mCameraViewModel);
        mViewModels.add(mCompassViewModel);

        for (ViewModel viewModel : mViewModels) {
            viewModel.onCreate(null);
        }

        // add camera view
        FrameLayout cameraLayout = (FrameLayout) findViewById(R.id.camera_frame);
        cameraLayout.addView(mCameraViewModel.getView(), 0);

        // add compass view
        FrameLayout mCompassContent = (FrameLayout) findViewById(R.id.compass_frame);
        mCompassContent.addView(mCompassViewModel.getView(), 0);
        mCompassViewModel.setVisibility(View.GONE);

        // Compass Switch Button
        mCompassSwitchBtn = (ToggleButton) findViewById(R.id.compassSwitch);
        mCompassSwitchBtn.setChecked(false);
        mCompassSwitchBtn
                .setOnCheckedChangeListener(mSwichCheckedChangeListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        for (ViewModel viewModel : mViewModels) {
            viewModel.onResume(null);
        }

        // mCameraViewModel.setCameraOrientation(90);
        Log.i("mengdd", "onResume");
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mCameraViewModel.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (ViewModel viewModel : mViewModels) {
            viewModel.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (ViewModel viewModel : mViewModels) {
            viewModel.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (ViewModel viewModel : mViewModels) {
            viewModel.onDestroy();
        }
    }

    private final OnCheckedChangeListener mSwichCheckedChangeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked) {
            switch (buttonView.getId()) {

            case R.id.compassSwitch:
                if (isChecked) {
                    mCompassViewModel.setVisibility(View.VISIBLE);

                }
                else {
                    mCompassViewModel.setVisibility(View.GONE);
                }

                break;
            default:
                break;
            }

        }
    };

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // This method is to init the Menu

        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {

        case R.id.action_baidu_map:

            intent.setClass(RealSceneActivity.this, BaiduMapActivity.class);
            startActivity(intent);

            break;
        case R.id.action_google_map:
            intent.setClass(RealSceneActivity.this, GoogleMapActivity.class);

            startActivity(intent);

            break;
        case R.id.action_poi:
            intent.setClass(RealSceneActivity.this,
                    TestAugmentedPOIActivity.class);

            startActivity(intent);

            break;

        default:
            return super.onOptionsItemSelected(item);

        }

        return true;
    }
}
