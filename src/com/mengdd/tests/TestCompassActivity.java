package com.mengdd.tests;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import com.mengdd.arapp.R;
import com.mengdd.sensors.CompassViewModel;

/**
 * Test Activity for CompassViewModel.
 * 
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 * 
 */
public class TestCompassActivity extends Activity {
	private CompassViewModel mCompassViewModel = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.test_compass);

		mCompassViewModel = new CompassViewModel(this);
		mCompassViewModel.onCreate(savedInstanceState);

		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.main_frame);

		frameLayout.addView(mCompassViewModel.getView(), 0);

		ToggleButton remapBtn = (ToggleButton) findViewById(R.id.remap);
		remapBtn.setOnCheckedChangeListener(mCheckedChangeListener);

		ToggleButton antiAliasBtn = (ToggleButton) findViewById(R.id.anti_alias);
		antiAliasBtn.setOnCheckedChangeListener(mCheckedChangeListener);

	}

	private OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {

			switch (buttonView.getId()) {
			case R.id.remap:
				if (isChecked) {
					mCompassViewModel
							.setCurrentMode(CompassViewModel.REMAP_MODE);
				}
				else {
					mCompassViewModel
							.setCurrentMode(CompassViewModel.TRADITIONAL_MODE);
				}

				break;
			case R.id.anti_alias:

				if (isChecked) {
					mCompassViewModel.setAntiAlias(true);
				}
				else {
					mCompassViewModel.setAntiAlias(false);
				}

				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		mCompassViewModel.onResume(null);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mCompassViewModel.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mCompassViewModel.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		mCompassViewModel.onDestroy();
	}

}
