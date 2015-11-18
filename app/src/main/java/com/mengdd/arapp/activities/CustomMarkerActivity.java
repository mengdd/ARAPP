package com.mengdd.arapp.activities;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;

import com.mengdd.custommarker.MainCustomMarkerViewModel;

public class CustomMarkerActivity extends Activity {
    private MainCustomMarkerViewModel mMainCustomMarkerViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        mMainCustomMarkerViewModel = new MainCustomMarkerViewModel(this);
        mMainCustomMarkerViewModel.onCreate(null);

        setContentView(mMainCustomMarkerViewModel.getView());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMainCustomMarkerViewModel.onResume(null);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mMainCustomMarkerViewModel.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mMainCustomMarkerViewModel.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mMainCustomMarkerViewModel.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mMainCustomMarkerViewModel.onConfigurationChanged(newConfig);
    }

}
