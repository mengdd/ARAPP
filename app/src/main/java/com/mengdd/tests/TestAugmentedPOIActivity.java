package com.mengdd.tests;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.mengdd.arapp.GlobalARData;
import com.mengdd.arapp.R;
import com.mengdd.arapp.R.drawable;
import com.mengdd.arapp.R.id;
import com.mengdd.arapp.R.layout;
import com.mengdd.camera.CameraViewModel;
import com.mengdd.components.ViewModel;
import com.mengdd.location.LocationModel;
import com.mengdd.location.google.GoogleLocationModel;
import com.mengdd.map.baidu.BaiduMapHelper;
import com.mengdd.poi.data.POIViewModel;
import com.mengdd.poi.ui.MarkersOverlayView;
import com.mengdd.poi.ui.BaiduMarker;
import com.mengdd.poi.ui.GoogleMarker;
import com.mengdd.poi.ui.BasicMarker;
import com.mengdd.poi.ui.RadarView;
import com.mengdd.poi.ui.RadarZoomController;
import com.mengdd.sensors.SensorViewModel;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.SeekBar;

public class TestAugmentedPOIActivity extends Activity {
    private List<ViewModel> mViewModels = null;

    // camera
    private CameraViewModel mCameraViewModel = null;
    // sensor
    private SensorViewModel mSensorViewModel = null;
    // location
    private LocationModel mLocationModel = null;
    // get poi data
    private POIViewModel mPoiViewModel = null;

    private MarkersOverlayView mPoiView = null;
    private RadarView mRadar = null;
    private RadarZoomController mZoomController = null;

    // some test data
    private Location myLocation = new Location("network");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // no title and fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (null == BaiduMapHelper.getMapManager()) {
            BaiduMapHelper.initBaiduMapManager(this);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.arpoi);

        mViewModels = new ArrayList<ViewModel>();

        mCameraViewModel = new CameraViewModel(this);
        mSensorViewModel = new SensorViewModel(this);
        mLocationModel = new GoogleLocationModel(this);
        mPoiViewModel = new POIViewModel(this);

        mViewModels.add(mCameraViewModel);
        mViewModels.add(mSensorViewModel);
        mViewModels.add(mPoiViewModel);
        mViewModels.add(mLocationModel);

        for (ViewModel viewModel : mViewModels) {
            viewModel.onCreate(null);
        }

        // add camera view
        FrameLayout cameraLayout = (FrameLayout) findViewById(R.id.camera_frame);
        cameraLayout.addView(mCameraViewModel.getView(), 0);

        mPoiView = new MarkersOverlayView(this);
        cameraLayout.addView(mPoiView);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // radar
        mRadar = new RadarView(this);
        mRadar.setLayoutParams(layoutParams);
        FrameLayout radarFrame = (FrameLayout) findViewById(R.id.radar_frame);
        radarFrame.addView(mRadar, 0);
        // seekBar

        mZoomController = new RadarZoomController(this);
        FrameLayout zoomFrame = (FrameLayout) findViewById(R.id.zoom_frame);
        SeekBar seekBar = mZoomController.getSeekBar();
        seekBar.setLayoutParams(layoutParams);

        zoomFrame.addView(seekBar, 0);

        mZoomController.addOnZoomChangedListener(mPoiViewModel);
        mZoomController.addOnZoomChangedListener(mRadar);
        // ARView需要监听传感器的姿态变化
        mSensorViewModel.addSensorEventListener(mPoiView);
        mSensorViewModel.addSensorEventListener(mRadar);

        // initTest();

        initTestForBaidu2();
    }

    private void initTest() {

        List<BasicMarker> markers = new ArrayList<BasicMarker>();
        Resources res = getResources();
        Bitmap icon = BitmapFactory.decodeResource(res, R.drawable.wikipedia);

        // 必胜客
        BasicMarker m1 = new GoogleMarker("N: Foreign Resturant", Color.WHITE,
                icon, 39.97719, 116.3175, 18);
        // 民生银行
        BasicMarker m2 = new GoogleMarker("E: Bank", Color.RED, icon, 39.97649,
                116.3212, 18);
        // 九头鹰
        BasicMarker m3 = new GoogleMarker("W: Chinese Resturant", Color.BLUE,
                icon, 39.97567, 116.3142, 18);
        // 海淀南站
        BasicMarker m4 = new GoogleMarker("S: Bus station", Color.GREEN, icon,
                39.97418, 116.3182, 18);

        markers.add(m1);
        markers.add(m2);
        markers.add(m3);
        markers.add(m4);

        GlobalARData.addMarkers(markers);

        // 地铁海淀黄庄站
        myLocation.setLatitude(39.97719);
        myLocation.setLongitude(116.3175);
        myLocation.setAltitude(19);

        GlobalARData.setCurrentGoogleLocation(myLocation);
    }

    private void initTestForBaidu() {

        List<BasicMarker> markers = new ArrayList<BasicMarker>();
        Resources res = getResources();
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

        // 地铁海淀黄庄站116.324351,39.98187
        myLocation.setLatitude(39.98187);
        myLocation.setLongitude(116.324351);
        myLocation.setAltitude(19);

        GlobalARData.setCurrentGoogleLocation(myLocation);
    }

    private void initTestForBaidu2() {

        List<BasicMarker> markers = new ArrayList<BasicMarker>();
        Resources res = getResources();
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

        // 地铁海淀黄庄站116.324351,39.98187
        myLocation.setLatitude(39.98187);
        myLocation.setLongitude(116.324351);
        myLocation.setAltitude(19);

        GlobalARData.setCurrentGoogleLocation(myLocation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (ViewModel viewModel : mViewModels) {
            viewModel.onResume(null);
        }
        // mLocationModel.registerLocationUpdates();
        GlobalARData.addLocationListener(mSensorViewModel);
        GlobalARData.addLocationListener(mPoiViewModel);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mCameraViewModel.onConfigurationChanged(newConfig);
    }
    @Override
    protected void onPause() {
        super.onPause();
        for (ViewModel viewModel : mViewModels) {
            viewModel.onPause();
        }
        // mLocationModel.unregisterLocationUpdates();
        GlobalARData.removeLocationListener(mSensorViewModel);
        GlobalARData.removeLocationListener(mPoiViewModel);
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (ViewModel viewModel : mViewModels) {
            viewModel.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (ViewModel viewModel : mViewModels) {
            viewModel.onDestroy();
        }
    }

}
