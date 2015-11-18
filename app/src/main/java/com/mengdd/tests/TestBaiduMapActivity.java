package com.mengdd.tests;

import com.baidu.location.BDLocation;
import com.mengdd.arapp.GlobalARData;
import com.mengdd.arapp.R;
import com.mengdd.location.baidu.BaiduLocationModel;
import com.mengdd.map.baidu.BaiduMapViewModel;
import com.mengdd.map.baidu.BaiduMyLocationOverlay;
import com.mengdd.utils.AppConstants;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

public class TestBaiduMapActivity extends Activity implements LocationListener {

    private BaiduMapViewModel mMapViewModel = null;
    private BaiduMyLocationOverlay myLocationOverlay = null;
    private BaiduLocationModel mLocationModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_baidumap);

        // basic map
        mMapViewModel = new BaiduMapViewModel(this);
        mMapViewModel.onCreate(null);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame);
        frameLayout.addView(mMapViewModel.getView(), 0);

        // location
        mLocationModel = new BaiduLocationModel(this);
        GlobalARData.addLocationListener(this);

        // my location overlay
        myLocationOverlay = new BaiduMyLocationOverlay(mMapViewModel.getMap());

    }

    @Override
    protected void onResume() {
        super.onResume();

        mMapViewModel.onResume(null);

        mLocationModel.registerLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapViewModel.onPause();

        mLocationModel.unregisterLocationUpdates();
        GlobalARData.removeLocationListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapViewModel.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapViewModel.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {

        BDLocation location2 = mLocationModel.getBDLocaiton();
        Log.i(AppConstants.LOG_TAG, "baiduLocation info: " + location2);
        myLocationOverlay.setLocationData(location2);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
