package com.mengdd.camera;

import com.mengdd.arapp.R;
import com.mengdd.maps.google.GoogleMapViewModel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Test Activity for Camera Module.
 * 
 * 
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 * 
 */
public class TestCameraActivity extends Activity
{
	private CameraViewModel mCameraViewModel = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		mCameraViewModel = new CameraViewModel(this);
		mCameraViewModel.onCreate(null);

		setContentView(R.layout.test_camera);

		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.camera_frame);
		frameLayout.addView(mCameraViewModel.getView(), 0);

	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mCameraViewModel.onResume(null);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mCameraViewModel.onPause();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		mCameraViewModel.onStop();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mCameraViewModel.onDestory();
	}

}
