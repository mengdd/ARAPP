package com.mengdd.tests;

import android.app.Activity;
import android.os.Bundle;

import com.mengdd.search.SearchBottomViewModel;

public class TestBottomMenuActivity extends Activity {
    private SearchBottomViewModel mBottomViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBottomViewModel = new SearchBottomViewModel(this);
        mBottomViewModel.onCreate(null);

        setContentView(mBottomViewModel.getView());
    }

}
