package com.mengdd.map.autonavi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.CancelableCallback;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.mengdd.arapp.R;
import com.mengdd.map.BasicMapViewModel;

/**
 * The basic ViewModel for AutoNavi Map (高德地图)
 * 
 * The MapView's callback methods must be called.
 * 
 * See： http://code.autonavi.com/android/index for more information and guide.
 * 
 * Reference: http://code.autonavi.com/Public/reference/Android%20API%20v2/
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

        // init the map to a better state
        changeMapCamera(39, 116, 0, 0, 12);

    }

    public float getZoomLevel() {
        return mAMap.getCameraPosition().zoom;
    }

    public void zoomIn() {
        CameraUpdate cameraUpdate = CameraUpdateFactory.zoomIn();
        changeMapCamera(cameraUpdate);
    }

    public void zoomOut() {
        CameraUpdate cameraUpdate = CameraUpdateFactory.zoomOut();
        changeMapCamera(cameraUpdate);
    }

    @Override
    public void changeMapCamera(double latitude, double longitude) {
        // LatLng latLng = new LatLng(latitude, longitude);
        // CameraUpdate cameraUpdate = CameraUpdateFactory
        // .newCameraPosition(new CameraPosition(latLng, 0, 0, 0));
        // // CameraPosition(LatLng target, float zoom, float tilt, float
        // bearing)
        // mAMap.moveCamera(cameraUpdate);

        CameraPosition position = mAMap.getCameraPosition();
        changeMapCamera(latitude, longitude, position.bearing, position.tilt,
                position.zoom);
    }

    /**
     * Repositions the camera according to the params. These parames are used to
     * defined the CameraUpdate object.
     * 
     * @param latitude
     * @param longitude
     * @param bearing
     *            orientation
     * @param tilt
     *            viewing angle
     * @param zoom
     */
    public void changeMapCamera(double latitude, double longitude,
            float bearing, float tilt, float zoom) {
        LatLng targetLatLng = new LatLng(latitude, longitude);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(targetLatLng).zoom(zoom).bearing(bearing).tilt(tilt)
                .build();

        CameraUpdate cameraUpdate = CameraUpdateFactory
                .newCameraPosition(cameraPosition);

        changeMapCamera(cameraUpdate);

    }

    /**
     * Repositions the camera according to the instructions defined in the
     * update.
     * 
     * @param cameraUpdate
     */
    public void changeMapCamera(CameraUpdate cameraUpdate) {
        changeMapCamera(cameraUpdate, false, 0, null);
    }

    /**
     * Change the Map Camera Position, the point looking at the map.
     * 
     * @param cameraUpdate
     * @param isAnimated
     *            If the camera changes with animation
     * @param animateDurationMs
     *            The duration of the animation in milliseconds.
     * @param animateCallback
     *            An optional callback to be notified from the main thread when
     *            the animation stops.
     */
    public void changeMapCamera(CameraUpdate cameraUpdate, boolean isAnimated,
            int animateDurationMs, CancelableCallback animateCallback) {

        if (isAnimated) {

            mAMap.animateCamera(cameraUpdate, animateDurationMs,
                    animateCallback);
        }
        else {
            mAMap.moveCamera(cameraUpdate);
        }
    }

    @Override
    public void addMarker(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        mAMap.addMarker(new MarkerOptions().position(latLng).icon(
                BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }

    public void setLocationSource(LocationSource locationSource) {

        mAMap.setLocationSource(locationSource);

        mAMap.setMyLocationStyle(getDefaultLocationStyle());
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

    }

    public MyLocationStyle getDefaultLocationStyle() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        // myLocationStyle.radiusFillColor(color)//设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(5);// 设置圆形的边框粗细

        return myLocationStyle;
    }
}
