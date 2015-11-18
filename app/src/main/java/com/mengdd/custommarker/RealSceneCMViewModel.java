package com.mengdd.custommarker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import com.mengdd.arapp.GlobalARData;
import com.mengdd.arapp.R;
import com.mengdd.camera.CameraViewModel;
import com.mengdd.components.ViewModel;
import com.mengdd.db.CustomMarkerTable;
import com.mengdd.poi.ui.BaiduMarker;
import com.mengdd.poi.ui.BasicMarker;
import com.mengdd.poi.ui.MarkersOverlayView;
import com.mengdd.poi.ui.RadarView;
import com.mengdd.poi.ui.RadarZoomController;
import com.mengdd.sensors.SensorViewModel;

public class RealSceneCMViewModel extends ViewModel {
    private View mRootView = null;

    private List<ViewModel> mViewModels = null;
    private Resources mResources = null;

    // camera
    private CameraViewModel mCameraViewModel = null;
    // sensor
    private SensorViewModel mSensorViewModel = null;

    private MarkersOverlayView mPoiView = null;
    private RadarView mRadar = null;
    private RadarZoomController mZoomController = null;

    // marker
    private Bitmap mMarkerIcon = null;

    private boolean mCacheClean = false;

    public RealSceneCMViewModel(Activity activity) {
        super(activity);

        mResources = mActivity.getResources();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRootView = mInflater.inflate(R.layout.custom_realscene, null);

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
        mMarkerIcon = BitmapFactory.decodeResource(res, R.drawable.baidu);

        GlobalARData.setCurrentBaiduLocation(GlobalARData.hardFixBD);

        mCacheClean = false;
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

        // TODO: now suppose every resume needs refresh, need to be fixed later
        mCacheClean = false;
        showCustomMarkers();

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

    private void showCustomMarkers() {
        // Convert custom markers to Baidu Markers and add to GlobalARData

        if (mCacheClean) {
            return;
        }
        GlobalARData.clearMarkers();

        List<BasicMarker> baiduMarkerItems = loadAllCustomMarkers();

        if (null != baiduMarkerItems) {
            GlobalARData.addMarkers(baiduMarkerItems);
        }

        mCacheClean = true;

    }

    private List<BasicMarker> loadAllCustomMarkers() {
        Collection<MarkerItem> markerItems = null;
        List<BasicMarker> resultList = null;

        markerItems = CustomMarkerTable.queryAllCustomMarkerItems();

        if (null != markerItems) {
            resultList = new ArrayList<BasicMarker>();
            Bitmap icon = BitmapFactory.decodeResource(mResources,
                    R.drawable.baidu);
            for (MarkerItem item : markerItems) {

                BaiduMarker newMarker = new BaiduMarker(item.getName(),
                        Color.WHITE, icon, item);
                resultList.add(newMarker);

            }

        }

        return resultList;

    }
}
