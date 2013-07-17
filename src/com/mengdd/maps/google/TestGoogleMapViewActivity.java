package com.mengdd.maps.google;

import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
/**
 * Test Activity for GoogleMapViewModel
 * 
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 * 
 */
public class TestGoogleMapViewActivity extends Activity
{
	private FrameLayout mMapContentFrameLayout = null;
	private GoogleMapViewModel mMapViewModel = null;

	private ToggleButton mMapSwitchBtn = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_mapview);

		mMapContentFrameLayout = (FrameLayout) findViewById(R.id.mapContent);

		mMapViewModel = new GoogleMapViewModel(this, null);
		mMapViewModel.onCreate(null);

		mMapContentFrameLayout.addView(mMapViewModel.getView());

		mMapViewModel.setVisibility(View.GONE);

		mMapSwitchBtn = (ToggleButton) findViewById(R.id.mapSwitch);
		mMapSwitchBtn.setChecked(false);
		mMapSwitchBtn.setOnCheckedChangeListener(mMapCheckedChangeListener);

	}

	private OnCheckedChangeListener mMapCheckedChangeListener = new OnCheckedChangeListener()
	{

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked)
		{
			if (isChecked)
			{
				mMapViewModel.setVisibility(View.VISIBLE);
			}
			else
			{
				mMapViewModel.setVisibility(View.GONE);
			}

		}
	};

}
