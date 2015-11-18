package com.mengdd.search;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.mengdd.arapp.GlobalARData;
import com.mengdd.arapp.R;
import com.mengdd.camera.CameraViewModel;
import com.mengdd.components.ViewModel;
import com.mengdd.poi.ui.MarkersOverlayView;
import com.mengdd.poi.ui.BaiduMarker;
import com.mengdd.poi.ui.BasicMarker;
import com.mengdd.poi.ui.RadarView;
import com.mengdd.poi.ui.RadarZoomController;
import com.mengdd.sensors.SensorViewModel;
import com.mengdd.utils.AppConstants;
import com.mengdd.utils.BitmapUtils;

public class SearchRealSceneViewModel extends ViewModel implements
        MKSearchListener {
    private View mRootView = null;

    private List<ViewModel> mViewModels = null;

    // camera
    private CameraViewModel mCameraViewModel = null;
    // sensor
    private SensorViewModel mSensorViewModel = null;

    private MarkersOverlayView mPoiView = null;
    private RadarView mRadar = null;
    private RadarZoomController mZoomController = null;

    // marker
    private Bitmap mMarkerIcon = null;

    protected SearchRealSceneViewModel(Activity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = mInflater.inflate(R.layout.search_real_scene, null);

        mViewModels = new ArrayList<ViewModel>();

        mCameraViewModel = new CameraViewModel(mActivity);

        mSensorViewModel = new SensorViewModel(mActivity);

        mViewModels.add(mCameraViewModel);
        mViewModels.add(mSensorViewModel);

        for (ViewModel viewModel : mViewModels) {
            viewModel.onCreate(null);
        }

        mCameraViewModel.setCaptureButtonVisibility(View.GONE);

        // add camera view
        FrameLayout cameraLayout = (FrameLayout) mRootView
                .findViewById(R.id.camera_frame);
        cameraLayout.addView(mCameraViewModel.getView(), 0);

        mPoiView = new MarkersOverlayView(mActivity);
        cameraLayout.addView(mPoiView, 1);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // radar
        mRadar = new RadarView(mActivity);
        mRadar.setLayoutParams(layoutParams);
        FrameLayout radarFrame = (FrameLayout) mRootView
                .findViewById(R.id.radar_frame);
        radarFrame.addView(mRadar, 0);

        // seekBar
        mZoomController = new RadarZoomController(mActivity);
        FrameLayout zoomFrame = (FrameLayout) mRootView
                .findViewById(R.id.zoom_frame);
        SeekBar seekBar = mZoomController.getSeekBar();
        seekBar.setLayoutParams(layoutParams);
        zoomFrame.addView(seekBar, 0);
        mZoomController.addOnZoomChangedListener(mRadar);
        mZoomController.addOnZoomChangedListener(mPoiView);

        // ARView需要监听传感器的姿态变化
        mSensorViewModel.addSensorEventListener(mPoiView);
        mSensorViewModel.addSensorEventListener(mRadar);

        // prepare for markers

        Resources res = mActivity.getResources();
        // mMarkerIcon = BitmapFactory.decodeResource(res, R.drawable.baidu);
        mMarkerIcon = BitmapUtils.getBitmapFromShape(res,
                R.drawable.shape_baidu_ar_icon, 50, 50);

        GlobalARData.setCurrentBaiduLocation(GlobalARData.hardFixBD);

        // initTestForBaidu();

    }

    private void initTestForBaidu() {

        List<BasicMarker> markers = new ArrayList<BasicMarker>();
        Resources res = mActivity.getResources();
        Bitmap icon = BitmapFactory.decodeResource(res, R.drawable.baidu);

        // 眉州东坡116.322986,39.983426
        MKPoiInfo info1 = new MKPoiInfo();
        info1.name = "Meizhou";
        info1.pt = new GeoPoint((int) (39.983426 * 1e6),
                (int) (116.322986 * 1e6));
        BasicMarker m1 = new BaiduMarker("N: MeiZhou", Color.WHITE, icon, info1);

        // 豪景大厦116.328286,39.981614
        MKPoiInfo info2 = new MKPoiInfo();
        info2.name = "HaoJing";
        info2.pt = new GeoPoint((int) (39.981614 * 1e6),
                (int) (116.328286 * 1e6));
        BasicMarker m2 = new BaiduMarker("E: HaoJing", Color.RED, icon, info2);

        // 人民大学站116.328088,39.97278
        MKPoiInfo info3 = new MKPoiInfo();
        info3.name = "Renmin";
        info3.pt = new GeoPoint((int) (39.97278 * 1e6),
                (int) (116.328088 * 1e6));
        BasicMarker m3 = new BaiduMarker("S: RenMin Subway", Color.BLUE, icon,
                info3);

        // 苏州街站116.312772,39.981707
        MKPoiInfo info4 = new MKPoiInfo();
        info4.name = "Suzhou";
        info4.pt = new GeoPoint((int) (39.981707 * 1e6),
                (int) (116.312772 * 1e6));
        BasicMarker m4 = new BaiduMarker("W: Suzhou Subway", Color.YELLOW,
                icon, info4);

        markers.add(m1);
        markers.add(m2);
        markers.add(m3);
        markers.add(m4);

        GlobalARData.addMarkers(markers);
        // some test data
        Location myLocation = new Location("network");
        // 地铁海淀黄庄站116.324351,39.98187
        myLocation.setLatitude(39.98187);
        myLocation.setLongitude(116.324351);
        myLocation.setAltitude(19);

        GlobalARData.setCurrentGoogleLocation(myLocation);
    }

    @Override
    public View getView() {
        return mRootView;
    }

    @Override
    public void onResume(Intent intent) {

        super.onResume(intent);

        for (ViewModel viewModel : mViewModels) {
            viewModel.onResume(null);
        }
        GlobalARData.addLocationListener(mSensorViewModel);

    }

    @Override
    public void onPause() {

        super.onPause();
        for (ViewModel viewModel : mViewModels) {
            viewModel.onPause();
        }

        GlobalARData.removeLocationListener(mSensorViewModel);

    }

    @Override
    public void onStop() {
        super.onStop();
        for (ViewModel viewModel : mViewModels) {
            viewModel.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        for (ViewModel viewModel : mViewModels) {
            viewModel.onDestroy();
        }
    }

    @Override
    public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {

    }

    @Override
    public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {
    }

    @Override
    public void onGetSuggestionResult(MKSuggestionResult result, int iError) {
    }

    @Override
    public void onGetPoiResult(MKPoiResult result, int type, int iError) {
        if (null == result || 0 != iError) {
            return;
        }

        Log.w(AppConstants.LOG_TAG, "onGetPoiResult in SearchRealScene!!");

        List<BasicMarker> markers = new ArrayList<BasicMarker>();

        for (MKPoiInfo poi : result.getAllPoi()) {
            BasicMarker m = new BaiduMarker(poi.name, Color.WHITE, mMarkerIcon,
                    poi);

            markers.add(m);
        }
        GlobalARData.clearMarkers();
        GlobalARData.addMarkers(markers);

    }

    @Override
    public void onGetPoiDetailSearchResult(int type, int iError) {
    }

    @Override
    public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {

    }

    @Override
    public void onGetBusDetailResult(MKBusLineResult result, int iError) {
    }

    @Override
    public void onGetAddrResult(MKAddrInfo result, int iError) {
    }

    @Override
    public void onGetShareUrlResult(MKShareUrlResult result, int type,
            int iError) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mCameraViewModel.onConfigurationChanged(newConfig);

    }
}
