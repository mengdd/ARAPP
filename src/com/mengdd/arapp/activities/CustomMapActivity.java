package com.mengdd.arapp.activities;


import java.util.ArrayList;
import java.util.List;

import com.mengdd.arapp.FrameHeaderViewModel;
import com.mengdd.arapp.R;
import com.mengdd.arapp.FrameHeaderViewModel.OnBackListener;
import com.mengdd.components.ViewModel;
import com.mengdd.custommarker.MainCustomMarkerViewModel;
import com.mengdd.custommarker.MapCustomMarkerViewModel;
import com.mengdd.utils.AppConstants;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

public class CustomMapActivity extends Activity
{
	private FrameHeaderViewModel mHeaderViewModel = null;
	private Resources resources = null;

	private List<ViewModel> mViewModels = null;
	
	private MapCustomMarkerViewModel mMapViewModel = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.custom_main);
		
		initHeader();
		
		// map
		mMapViewModel = new MapCustomMarkerViewModel(this);
		mViewModels = new ArrayList<ViewModel>();
		mViewModels.add(mMapViewModel);
		
		
		for(ViewModel viewModel: mViewModels)
		{
			viewModel.onCreate(null);
		}
		RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.main_content);
		
		mainLayout.addView(mMapViewModel.getView());
	
	}
	

	private void initHeader()
	{
		// Header
		resources = this.getResources();
		mHeaderViewModel = new FrameHeaderViewModel(this);
		mHeaderViewModel.onCreate(null);
		mHeaderViewModel.setSettingVisibility(View.GONE);
		mHeaderViewModel.setTitle(resources.getString(R.string.custom_marker_title));
		ViewGroup headerGourp = (ViewGroup)findViewById(R.id.title);
		headerGourp.addView(mHeaderViewModel.getView(), 0);
		mHeaderViewModel.setOnBackListener(new OnBackListener()
		{

			@Override
			public void onBack()
			{
				Log.i(AppConstants.LOG_TAG,"onBack in CustomMapActivity");
//				CustomMapActivity.this.onKeyDown(KeyEvent.KEYCODE_BACK, new KeyEvent(
//						KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
				
				
				finish();

			}
		});

	}


	@Override
	protected void onResume()
	{
		super.onResume();
		
		for(ViewModel viewModel: mViewModels)
		{
			viewModel.onResume(null);
		}
	}
	

	@Override
	protected void onPause()
	{
		super.onPause();
		
		for(ViewModel viewModel: mViewModels)
		{
			viewModel.onPause();
		}
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		
		
		for(ViewModel viewModel: mViewModels)
		{
			viewModel.onStop();
		}
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		
		for(ViewModel viewModel: mViewModels)
		{
			viewModel.onDestroy();
		}
	}

}
