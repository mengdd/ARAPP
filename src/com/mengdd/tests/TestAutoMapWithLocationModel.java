package com.mengdd.tests;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.mengdd.components.ViewModel;
import com.mengdd.map.autonavi.AutoNaviMapWithLocation;

public class TestAutoMapWithLocationModel extends Activity {

    private ViewModel mViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        mViewModel = new AutoNaviMapWithLocation(this);
        mViewModel.onCreate(null);

        setContentView(mViewModel.getView());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.onResume(null);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mViewModel.onNewIntent(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mViewModel.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mViewModel.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.onDestroy();
    }
}
