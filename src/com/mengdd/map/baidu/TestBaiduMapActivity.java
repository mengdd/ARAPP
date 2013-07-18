package com.mengdd.map.baidu;

import com.mengdd.arapp.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class TestBaiduMapActivity extends Activity
{

	private BaiduMapViewModel mMapViewModel = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.test_baidumap);
		
		mMapViewModel = new BaiduMapViewModel(this);
		
		mMapViewModel.onCreate(null);
		
		
		
		FrameLayout frameLayout = (FrameLayout)findViewById(R.id.frame);
		frameLayout.addView(mMapViewModel.getView(), 0);
		
		
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		
		mMapViewModel.onResume(null);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mMapViewModel.onPause();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		mMapViewModel.onStop();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mMapViewModel.onDestory();
	}
	
	

}
