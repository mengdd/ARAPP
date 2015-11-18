package com.mengdd.tests;

import android.app.Activity;
import android.os.Bundle;

import com.mengdd.search.keywords.KeywordsNaviViewModel;

public class TestNaviUIActivity extends Activity {

    private KeywordsNaviViewModel mNaviViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNaviViewModel = new KeywordsNaviViewModel(this);

        mNaviViewModel.onCreate(savedInstanceState);

        setContentView(mNaviViewModel.getView());

    }
}
