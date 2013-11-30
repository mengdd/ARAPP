package com.mengdd.tests;

import com.mengdd.search.keywords.KeywordsNaviViewModel;

import android.app.Activity;
import android.os.Bundle;

public class TestNaviUIActivity extends Activity {

    private KeywordsNaviViewModel mNaviViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNaviViewModel = new KeywordsNaviViewModel(this);

        mNaviViewModel.onCreate(null);

        setContentView(mNaviViewModel.getView());

    }
}
