package com.mengdd.tests;

import android.app.Activity;
import android.os.Bundle;

import com.mengdd.poi.baidu.BaiduPoiSearchViewModel;

public class TestBaiduPoiActivity extends Activity {
    private BaiduPoiSearchViewModel mBaiduPoiSearchViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBaiduPoiSearchViewModel = new BaiduPoiSearchViewModel(this);
        setContentView(mBaiduPoiSearchViewModel.getView());
    }
}
