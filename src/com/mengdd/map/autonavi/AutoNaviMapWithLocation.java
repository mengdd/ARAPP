package com.mengdd.map.autonavi;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;
import com.mengdd.location.SimpleLocationView;
import com.mengdd.location.autonavi.AutoNaviLocationHelper;
import com.mengdd.location.autonavi.AutoNaviLocationModel;

public class AutoNaviMapWithLocation extends ViewModel implements
        AMapLocationListener {

    private View mRootView = null;

    private AutoNaviMapViewModel mMapViewModel = null;
    private AutoNaviLocationModel mLocationModel = null;
    private SimpleLocationView mLocationView = null;

    private List<ViewModel> mViewModels = null;

    public AutoNaviMapWithLocation(Activity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMapViewModel = new AutoNaviMapViewModel(mActivity);
        mLocationModel = new AutoNaviLocationModel(mActivity);
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

        mLocationModel.setAMapOnLocationChangedListener(this);

    }

    @Override
    public void onResume(Intent intent) {
        super.onResume(null);
        for (ViewModel model : mViewModels) {
            model.onResume(null);
        }

        mLocationModel.registerLocationUpdates();
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
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public View getView() {
        return mRootView;
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        mMapViewModel.changeMapCamera(aMapLocation.getLatitude(),
                aMapLocation.getLongitude());
        if (mMapViewModel.getZoomLevel() < 18) {
            mMapViewModel.zoomIn();
        }
        mMapViewModel.addMarker(aMapLocation.getLatitude(),
                aMapLocation.getLongitude());
        mLocationView.setLocationInfo(AutoNaviLocationHelper
                .getLocationString(aMapLocation));
    }

}
