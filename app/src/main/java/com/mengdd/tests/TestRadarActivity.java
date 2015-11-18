package com.mengdd.tests;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.mengdd.arapp.GlobalARData;
import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;
import com.mengdd.poi.ui.MarkersOverlayView;
import com.mengdd.sensors.SensorViewModel;

import java.util.ArrayList;
import java.util.List;

public class TestRadarActivity extends Activity {
    private MarkersOverlayView mPoiView = null;
    private SensorViewModel mSensorViewModel = null;
    private List<ViewModel> mViewModels = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.arpoi);

        FrameLayout cameraLayout = (FrameLayout) findViewById(R.id.camera_frame);

        mPoiView = new MarkersOverlayView(this);

        cameraLayout.addView(mPoiView);

        mSensorViewModel = new SensorViewModel(this);

        mViewModels = new ArrayList<ViewModel>();
        mViewModels.add(mSensorViewModel);

        for (ViewModel viewModel : mViewModels) {
            viewModel.onCreate(null);
        }

        mSensorViewModel.addSensorEventListener(mPoiView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        for (ViewModel viewModel : mViewModels) {
            viewModel.onResume(null);
        }

        GlobalARData.addLocationListener(mSensorViewModel);

    }

    @Override
    protected void onPause() {
        super.onPause();
        for (ViewModel viewModel : mViewModels) {
            viewModel.onPause();
        }

        GlobalARData.removeLocationListener(mSensorViewModel);

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
}
