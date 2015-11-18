package com.mengdd.map.google;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;
import com.mengdd.location.SimpleLocationView;
import com.mengdd.location.google.GoogleLocationHelper;
import com.mengdd.location.google.GoogleLocationModel;

public class GoogleMapWithLocation extends ViewModel implements
        LocationListener {

    private View mRootView = null;

    private GoogleMapViewModel mMapViewModel = null;
    private GoogleLocationModel mLocationModel = null;
    private SimpleLocationView mLocationView = null;

    private List<ViewModel> mViewModels = null;

    public GoogleMapWithLocation(Activity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMapViewModel = new GoogleMapViewModel(mActivity);
        mLocationModel = new GoogleLocationModel(mActivity);
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

        mLocationModel.setLocationListener(this);

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
    public void onLocationChanged(Location location) {
        mMapViewModel.changeMapCamera(location.getLatitude(),
                location.getLongitude());

        if (mMapViewModel.getZoomLevel() < 18) {
            mMapViewModel.zoomIn();
        }
        mMapViewModel
                .addMarker(location.getLatitude(), location.getLongitude());
        mLocationView.setLocationInfo(GoogleLocationHelper
                .getLocationString(location));
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

}
