package com.mengdd.tests;

import android.app.Activity;
import android.os.Bundle;

import com.mengdd.components.ViewModel;
import com.mengdd.map.autonavi.AutoNaviMapViewModel;

public class TestAutoNaviMapActivity extends Activity {

    private ViewModel mMapViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMapViewModel = new AutoNaviMapViewModel(this);

        mMapViewModel.onCreate(savedInstanceState);

        setContentView(mMapViewModel.getView());

    }
}
