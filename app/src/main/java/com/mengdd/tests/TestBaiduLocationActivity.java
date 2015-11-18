package com.mengdd.tests;

import com.mengdd.arapp.GlobalARData;
import com.mengdd.arapp.R;
import com.mengdd.location.LocationView;
import com.mengdd.location.baidu.BaiduLocationModel;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class TestBaiduLocationActivity extends Activity {
    private BaiduLocationModel mLocationModel = null;
    private LocationView mLocationView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_cur_location);

        mLocationModel = new BaiduLocationModel(this);

        // use a LocationView to show the LocationModel data
        mLocationView = new LocationView(this);
        mLocationView.onCreate(null);

        FrameLayout frameLayout2 = (FrameLayout) findViewById(R.id.frame2);
        frameLayout2.addView(mLocationView.getView());
        GlobalARData.addLocationListener(mLocationView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mLocationModel.registerLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationModel.unregisterLocationUpdates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
