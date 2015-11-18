package com.mengdd.map.baidu;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.MapView;
import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;
import com.mengdd.location.SimpleLocationView;
import com.mengdd.location.baidu.BaiduLocationHelper;
import com.mengdd.location.baidu.BaiduLocationModel;
import com.mengdd.map.BasicMapViewModel;

public class BaiduMapWithLocation extends ViewModel implements
        BDLocationListener {

    private View mRootView = null;
    private BasicMapViewModel mMapViewModel = null;
    private BaiduMyLocationOverlay mLocationOverlay = null;
    private BaiduLocationModel mLocationModel = null;
    private List<ViewModel> mViewModels = null;

    private SimpleLocationView mLocationView = null;

    public BaiduMapWithLocation(Activity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMapViewModel = new BaiduMapViewModel(mActivity);
        mLocationModel = new BaiduLocationModel(mActivity);
        mLocationView = new SimpleLocationView(mActivity);

        mViewModels = new ArrayList<ViewModel>();
        mViewModels.add(mMapViewModel);
        mViewModels.add(mLocationModel);
        mViewModels.add(mLocationView);

        for (ViewModel model : mViewModels) {
            model.onCreate(null);
        }

        mRootView = mActivity.getLayoutInflater().inflate(
                R.layout.map_with_location_view_model, null);

        ViewGroup mapContainerGroup = (ViewGroup) mRootView
                .findViewById(R.id.map);
        mapContainerGroup.addView(mMapViewModel.getView());

        ViewGroup locaViewGroup = (ViewGroup) mRootView
                .findViewById(R.id.location);
        locaViewGroup.addView(mLocationView.getView());

        mLocationOverlay = new BaiduMyLocationOverlay(
                (MapView) mMapViewModel.getMap());
    }

    @Override
    public void onResume(Intent intent) {
        super.onResume(null);
        for (ViewModel model : mViewModels) {
            model.onResume(null);
        }

        mLocationModel.registerLocationUpdates();
        mLocationModel.setBDLocationListener(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        for (ViewModel model : mViewModels) {
            model.onPause();
        }

        mLocationModel.setBDLocationListener(null);
        mLocationModel.unregisterLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        for (ViewModel model : mViewModels) {
            model.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (ViewModel model : mViewModels) {
            model.onDestroy();
        }
    }

    @Override
    public View getView() {
        return mRootView;
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        mLocationOverlay.setLocationData(bdLocation);

        mLocationView.setLocationInfo(BaiduLocationHelper
                .getLocationString(bdLocation));

        mMapViewModel.changeMapCamera(bdLocation.getLatitude(),
                bdLocation.getLongitude());
        mMapViewModel.addMarker(bdLocation.getLatitude(),
                bdLocation.getLongitude());
    }

    @Override
    public void onReceivePoi(BDLocation arg0) {

    }

}
