package com.mengdd.arapp.activities;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.mengdd.arapp.FrameHeaderViewModel;
import com.mengdd.arapp.FrameHeaderViewModel.OnBackListener;
import com.mengdd.arapp.R;

public class LoginActivity extends Activity {

    private FrameHeaderViewModel mHeaderViewModel = null;
    private Resources resources = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_activity);
        resources = getResources();

        // header
        mHeaderViewModel = new FrameHeaderViewModel(this);
        mHeaderViewModel.onCreate(null);
        mHeaderViewModel.setBackVisibility(View.VISIBLE);
        mHeaderViewModel.setSettingVisibility(View.GONE);
        mHeaderViewModel.setTitle(resources.getString(R.string.login_title));
        ViewGroup headerGourp = (ViewGroup) findViewById(R.id.title);
        headerGourp.addView(mHeaderViewModel.getView(), 0);
        mHeaderViewModel.setOnBackListener(new OnBackListener() {

            @Override
            public void onBack() {
                finish();

            }
        });
    }

}
