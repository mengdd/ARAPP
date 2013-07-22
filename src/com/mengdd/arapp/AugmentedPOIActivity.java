package com.mengdd.arapp;

import java.util.ArrayList;
import java.util.List;

import com.mengdd.camera.CameraViewModel;
import com.mengdd.components.ViewModel;
import com.mengdd.location.LocationModel;
import com.mengdd.location.google.GoogleLocationModel;
import com.mengdd.poi.data.POIViewModel;
import com.mengdd.poi.ui.ARPOIView;
import com.mengdd.poi.ui.IconMarker;
import com.mengdd.poi.ui.Marker;
import com.mengdd.poi.ui.Radar;
import com.mengdd.poi.ui.RadarZoomController;
import com.mengdd.sensors.SensorViewModel;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class AugmentedPOIActivity extends Activity
{
	private List<ViewModel> mViewModels = null;

	// camera
	private CameraViewModel mCameraViewModel = null;
	// sensor
	private SensorViewModel mSensorViewModel = null;
	// location
	private LocationModel mLocationModel = null;
	// get poi data
	private POIViewModel mPoiViewModel = null;

	private ARPOIView mPoiView = null;
	private Radar mRadar = null;
	private RadarZoomController mZoomController = null;

	// some test data
	private Location myLocation = new Location("network");

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		// no title and fullscreen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

		for (ViewModel viewModel : mViewModels)
		{
			viewModel.onCreate(null);
		}

		// add camera view
		FrameLayout cameraLayout = (FrameLayout) findViewById(R.id.camera_frame);
		cameraLayout.addView(mCameraViewModel.getView(), 0);

		mPoiView = new ARPOIView(this);
		cameraLayout.addView(mPoiView);

		ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		// radar
		mRadar = new Radar(this);
		mRadar.setLayoutParams(layoutParams);
		FrameLayout radarFrame = (FrameLayout) findViewById(R.id.radar_frame);
		radarFrame.addView(mRadar, 0);
		// seekBar

		mZoomController = new RadarZoomController(this);
		FrameLayout zoomFrame = (FrameLayout) findViewById(R.id.zoom_frame);
		SeekBar seekBar = mZoomController.getSeekBar();
		seekBar.setLayoutParams(layoutParams);

		zoomFrame.addView(seekBar, 0);

		mZoomController.setOnZoomChangedListener(mPoiViewModel);

		initTest();

		mSensorViewModel.addLocationListener(mPoiView);

		mCameraViewModel.setCameraOrientation(0);

	}

	private void initTest()
	{
		myLocation.setLatitude(39.9);
		myLocation.setLongitude(116.0);
		myLocation.setAltitude(19);

		GlobalARData.setCurrentLocation(myLocation);

		List<Marker> markers = new ArrayList<Marker>();
		Resources res = getResources();
		Bitmap icon = BitmapFactory.decodeResource(res, R.drawable.wikipedia);

		for (int i = 0; i < 1; i++)
		{

			Marker ma = new IconMarker("title", 40, 116, 18, Color.WHITE, icon);

			if (null != ma)
			{
				markers.add(ma);
			}
		}

		GlobalARData.addMarkers(markers);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		for (ViewModel viewModel : mViewModels)
		{
			viewModel.onResume(null);
		}
		mLocationModel.registerLocationUpdates();
		GlobalARData.addLocationListener(mSensorViewModel);
		GlobalARData.addLocationListener(mPoiViewModel);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		for (ViewModel viewModel : mViewModels)
		{
			viewModel.onPause();
		}
		mLocationModel.unregisterLocationUpdates();
		GlobalARData.removeLocationListener(mSensorViewModel);
		GlobalARData.removeLocationListener(mPoiViewModel);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		for (ViewModel viewModel : mViewModels)
		{
			viewModel.onStop();
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		for (ViewModel viewModel : mViewModels)
		{
			viewModel.onDestory();
		}
	}

}
