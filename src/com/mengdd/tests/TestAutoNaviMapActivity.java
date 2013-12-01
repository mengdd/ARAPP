package com.mengdd.tests;

import android.app.Activity;
import android.os.Bundle;

import com.mengdd.location.autonavi.AutoNaviLocationSource;
import com.mengdd.map.autonavi.AutoNaviMapViewModel;

public class TestAutoNaviMapActivity extends Activity {

    private AutoNaviMapViewModel mMapViewModel = null;

    private AutoNaviLocationSource mLocationModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMapViewModel = new AutoNaviMapViewModel(this);
        mLocationModel = new AutoNaviLocationSource(this);

        mMapViewModel.onCreate(savedInstanceState);

        setContentView(mMapViewModel.getView());

        mMapViewModel.setLocationSource(mLocationModel);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapViewModel.onResume(null);
        // LocationModel需要显式调用，LocationSource不用
        // mLocationModel.registerLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mMapViewModel.onPause();
        // mLocationModel.unregisterLocationUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mMapViewModel.onStop();
    }

    @Override
    protected void onDestroy() {
        mMapViewModel.onDestroy();
        super.onDestroy();
    }
}
