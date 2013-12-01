package com.mengdd.map.autonavi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.mengdd.arapp.R;
import com.mengdd.map.BasicMapViewModel;

/**
 * The basic ViewModel for AutoNavi Map (高德地图)
 * 
 * The MapView's callback methods must be called.
 * 
 * See： http://code.autonavi.com/android/index for more information and guide.
 * 
 * @author Dandan Meng
 * @Date 2013-12-01
 * 
 */
public class AutoNaviMapViewModel extends BasicMapViewModel {

    /**
     * MapView is the main UI for map
     */
    private MapView mMapView = null;

    private AMap mAMap = null;

    public AutoNaviMapViewModel(Activity activity) {
        super(activity);
    }

    @Override
    public MapView getMap() {
        return mMapView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRootView = mInflater.inflate(R.layout.autonavi_mapview_root, null);

        mMapView = (MapView) mRootView.findViewById(R.id.a_mapview);
        mMapView.onCreate(savedInstanceState);

        initMap();

    }

    @Override
    public void onResume(Intent intent) {
        super.onResume(intent);

        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        mMapView.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mMapView.onDestroy();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mMapView.onSaveInstanceState(outState);
    }

    private void initMap() {

        mAMap = mMapView.getMap();

    }

    @Override
    public void changeMapCamera(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory
                .newCameraPosition(new CameraPosition(latLng, 0, 0, 0));
        // CameraPosition(LatLng target, float zoom, float tilt, float bearing)
        mAMap.moveCamera(cameraUpdate);
    }

    @Override
    public void addMarker(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        mAMap.addMarker(new MarkerOptions().position(latLng).icon(
                BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }
}
