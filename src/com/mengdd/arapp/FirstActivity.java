package com.mengdd.arapp;

import java.util.ArrayList;
import java.util.List;

import com.mengdd.camera.CameraViewModel;
import com.mengdd.components.ViewModel;
import com.mengdd.components.ViewModelManager;
import com.mengdd.map.google.GoogleMapViewModel;
import com.mengdd.sensors.CompassViewModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

/**
 * 
 * This class is the first activity for the Project as an independent and
 * functional App.
 * 
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 * 
 */
public class FirstActivity extends Activity
{
	private ViewModelManager mViewModelManager = null;

	private ToggleButton mCompassSwitchBtn = null;

	private List<ViewModel> mViewModels = null;
	private CameraViewModel mCameraViewModel = null;
	private CompassViewModel mCompassViewModel = null;
	
	private Button mMapButton = null;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// no title and fullscreen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.first);

		mViewModels = new ArrayList<ViewModel>();

		mCameraViewModel = new CameraViewModel(this);
		mCompassViewModel = new CompassViewModel(this);

		mViewModels.add(mCameraViewModel);
		mViewModels.add(mCompassViewModel);

		for (ViewModel viewModel : mViewModels)
		{
			viewModel.onCreate(null);
		}

		// add camera view
		FrameLayout cameraLayout = (FrameLayout) findViewById(R.id.camera_frame);
		cameraLayout.addView(mCameraViewModel.getView(), 0);

		// add compass view
		FrameLayout mCompassContent = (FrameLayout) findViewById(R.id.compass_frame);
		mCompassContent.addView(mCompassViewModel.getView(), 0);
		mCompassViewModel.setVisibility(View.GONE);

		// Compass Switch Button
		mCompassSwitchBtn = (ToggleButton) findViewById(R.id.compassSwitch);
		mCompassSwitchBtn.setChecked(false);
		mCompassSwitchBtn
				.setOnCheckedChangeListener(mSwichCheckedChangeListener);
		
		
		//Go to Map Button 
		mMapButton = (Button) findViewById(R.id.goMap);
		mMapButton.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.setClass(FirstActivity.this, MapActivity.class);
				
				startActivity(intent);
			}
		});

	}

	@Override
	protected void onResume()
	{
		super.onResume();
		for (ViewModel viewModel : mViewModels)
		{
			viewModel.onResume(null);
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		for (ViewModel viewModel : mViewModels)
		{
			viewModel.onPause();
		}
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

	private OnCheckedChangeListener mSwichCheckedChangeListener = new OnCheckedChangeListener()
	{

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked)
		{
			switch (buttonView.getId())
			{

				case R.id.compassSwitch:
					if (isChecked)
					{
						mCompassViewModel.setVisibility(View.VISIBLE);

					}
					else
					{
						mCompassViewModel.setVisibility(View.GONE);
					}

					break;
				default:
					break;
			}

		}
	};

}
