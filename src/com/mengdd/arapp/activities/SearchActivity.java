package com.mengdd.arapp.activities;

import com.mengdd.search.MainSearchViewModel;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;

public class SearchActivity extends Activity {
    private MainSearchViewModel mMainSearchViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        mMainSearchViewModel = new MainSearchViewModel(this);
        mMainSearchViewModel.onCreate(null);

        setContentView(mMainSearchViewModel.getView());
    }

    @Override
    protected void onResume() {
        super.onResume();

        mMainSearchViewModel.onResume(null);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mMainSearchViewModel.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mMainSearchViewModel.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mMainSearchViewModel.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mMainSearchViewModel.onConfigurationChanged(newConfig);
    }
}
