package com.mengdd.arapp.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.mengdd.arapp.FrameHeaderViewModel;
import com.mengdd.arapp.FrameHeaderViewModel.OnBackListener;
import com.mengdd.arapp.GlobalARData;
import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;
import com.mengdd.location.LocationView;
import com.mengdd.location.google.GoogleLocationModel;
import com.mengdd.map.google.GoogleMapViewModel;

public class GoogleMapActivity extends Activity {

    private FrameHeaderViewModel mHeaderViewModel = null;
    private Resources resources = null;

    private GoogleMapViewModel mMapViewModel = null;
    private GoogleLocationModel mLocationModel = null;
    private LocationView mLocationView = null;

    private List<ViewModel> mViewModels = null;

    // listen to the locaiton

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        mMapViewModel = new GoogleMapViewModel(this);
        mLocationModel = new GoogleLocationModel(this);
        mLocationView = new LocationView(this);

        mViewModels = new ArrayList<ViewModel>();
        mViewModels.add(mMapViewModel);
        mViewModels.add(mLocationModel);
        mViewModels.add(mLocationView);

        for (ViewModel model : mViewModels) {
            model.onCreate(null);
        }

        setContentView(R.layout.map_activity);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.map_frame);
        frameLayout.addView(mMapViewModel.getView(), 0);
        frameLayout.addView(mLocationView.getView(), 1);

        resources = getResources();

        // header
        mHeaderViewModel = new FrameHeaderViewModel(this);
        mHeaderViewModel.onCreate(null);
        mHeaderViewModel.setBackVisibility(View.VISIBLE);
        mHeaderViewModel.setSettingVisibility(View.GONE);
        mHeaderViewModel
                .setTitle(resources.getString(R.string.googlemap_title));
        ViewGroup headerGourp = (ViewGroup) findViewById(R.id.title);
        headerGourp.addView(mHeaderViewModel.getView(), 0);
        mHeaderViewModel.setOnBackListener(new OnBackListener() {

            @Override
            public void onBack() {
                finish();

            }
        });

        GlobalARData.addLocationListener(mLocationView);
        GlobalARData.addLocationListener(mLocationListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (ViewModel model : mViewModels) {
            model.onResume(null);
        }

        mLocationModel.registerLocationUpdates();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (ViewModel model : mViewModels) {
            model.onPause();
        }

        mLocationModel.unregisterLocationUpdates();

        GlobalARData.removeLocationListener(mLocationView);
        GlobalARData.removeLocationListener(mLocationListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (ViewModel model : mViewModels) {
            model.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (ViewModel model : mViewModels) {
            model.onDestroy();
        }
    }

    private final LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {

            // since the location's value and the map's value don't match
            // some measures had to be taken to handle this.
            // otherwise the marker isn't on its right place.
            mMapViewModel.changeMapCamera(location.getLatitude(),
                    location.getLongitude());
            mMapViewModel.addMarker(location.getLatitude(),
                    location.getLongitude());

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
    };

}
