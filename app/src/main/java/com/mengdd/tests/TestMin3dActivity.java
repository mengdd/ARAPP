package com.mengdd.tests;

import java.util.ArrayList;
import java.util.List;

import com.mengdd.arapp.R;
import com.mengdd.camera.CameraViewModel;
import com.mengdd.components.ViewModel;
import com.mengdd.min3d.Min3DLayerViewModel;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class TestMin3dActivity extends Activity {
    private CameraViewModel mCameraViewModel = null;
    private Min3DLayerViewModel mLayerViewModel = null;

    private List<ViewModel> mViewModels = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModels = new ArrayList<ViewModel>();

        mCameraViewModel = new CameraViewModel(this);
        mViewModels.add(mCameraViewModel);

        mLayerViewModel = new Min3DLayerViewModel(this);

        mViewModels.add(mLayerViewModel);

        for (ViewModel viewModel : mViewModels) {
            viewModel.onCreate(null);
        }
        setContentView(R.layout.test_camera);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.camera_frame);
        frameLayout.addView(mCameraViewModel.getView(), 0);

        frameLayout.addView(mLayerViewModel.getView(), 1);

    }

    @Override
    protected void onResume() {
        super.onResume();

        for (ViewModel viewModel : mViewModels) {
            viewModel.onResume(null);
        }
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

}
