package com.mengdd.custommarker;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import com.mengdd.arapp.GlobalARData;
import com.mengdd.arapp.R;
import com.mengdd.camera.CameraViewModel;
import com.mengdd.components.ViewModel;
import com.mengdd.poi.ui.MarkersOverlayView;
import com.mengdd.poi.ui.RadarView;
import com.mengdd.poi.ui.RadarZoomController;
import com.mengdd.sensors.SensorViewModel;

public class RealSceneCMViewModel extends ViewModel
{
	private View mRootView = null;
	

	private List<ViewModel> mViewModels = null;

	// camera
	private CameraViewModel mCameraViewModel = null;
	// sensor
	private SensorViewModel mSensorViewModel = null;

	private MarkersOverlayView mPoiView = null;
	private RadarView mRadar = null;
	private RadarZoomController mZoomController = null;
	
	//marker
	private Bitmap mMarkerIcon = null; 
	
	
	public RealSceneCMViewModel(Activity activity)
	{
		super(activity);
	}
	
	@Override
	public void onCreate(Intent intent)
	{
		super.onCreate(intent);
		
		mRootView = mInflater.inflate(R.layout.custom_realscene, null);
		
		mViewModels = new ArrayList<ViewModel>();

		mCameraViewModel = new CameraViewModel(mActivity);
		
		mSensorViewModel = new SensorViewModel(mActivity);

		mViewModels.add(mCameraViewModel);
		mViewModels.add(mSensorViewModel);

		for (ViewModel viewModel : mViewModels)
		{
			viewModel.onCreate(null);
		}

		mCameraViewModel.setCaptureButtonVisibility(View.GONE);
		
		// add camera view
		FrameLayout cameraLayout = (FrameLayout) mRootView
				.findViewById(R.id.camera_frame);
		cameraLayout.addView(mCameraViewModel.getView(), 0);

		mPoiView = new MarkersOverlayView(mActivity);
		cameraLayout.addView(mPoiView,1);

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

		mCameraViewModel.setCameraOrientation(90);

		
		//prepare for markers
	
		Resources res = mActivity.getResources();
		mMarkerIcon = BitmapFactory.decodeResource(res, R.drawable.baidu);
		
		GlobalARData.setCurrentBaiduLocation(GlobalARData.hardFixBD);
	}
	@Override
	public View getView()
	{
		return mRootView;
	}
	
	@Override
	public void onResume(Intent intent)
	{

		super.onResume(intent);

		for (ViewModel viewModel : mViewModels)
		{
			viewModel.onResume(null);
		}

		GlobalARData.addLocationListener(mSensorViewModel);

	}

	@Override
	public void onPause()
	{
		super.onPause();
		for (ViewModel viewModel : mViewModels)
		{
			viewModel.onPause();
		}

		GlobalARData.removeLocationListener(mSensorViewModel);

	}

	@Override
	public void onStop()
	{
		super.onStop();
		for (ViewModel viewModel : mViewModels)
		{
			viewModel.onStop();
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		for (ViewModel viewModel : mViewModels)
		{
			viewModel.onDestroy();
		}
	}
	

	public void showCustomMarkers()
	{
		//TODO: Convert custom markers to Baidu Markers and add to GlobalARData
	}

}
