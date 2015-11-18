package com.mengdd.tests;

import com.mengdd.search.SearchBottomViewModel;

import android.app.Activity;
import android.os.Bundle;

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
