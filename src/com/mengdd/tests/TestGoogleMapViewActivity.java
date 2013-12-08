package com.mengdd.tests;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import com.mengdd.arapp.R;
import com.mengdd.map.google.GoogleMapViewModel;

/**
 * Test Activity for GoogleMapViewModel
 * 
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 * 
 */
public class TestGoogleMapViewActivity extends Activity {
    private FrameLayout mMapContentFrameLayout = null;
    private GoogleMapViewModel mMapViewModel = null;

    private ToggleButton mMapSwitchBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_mapview);

        mMapContentFrameLayout = (FrameLayout) findViewById(R.id.mapContent);

        mMapViewModel = new GoogleMapViewModel(this);
        mMapViewModel.onCreate(savedInstanceState);

        mMapContentFrameLayout.addView(mMapViewModel.getView());

        mMapSwitchBtn = (ToggleButton) findViewById(R.id.mapSwitch);
        mMapSwitchBtn.setChecked(false);
        mMapSwitchBtn.setOnCheckedChangeListener(mMapCheckedChangeListener);

    }

    private final OnCheckedChangeListener mMapCheckedChangeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked) {
            mMapContentFrameLayout.removeAllViews();
            if (isChecked) {

                mMapContentFrameLayout.addView(mMapViewModel.getView());
            }

        }
    };

}
