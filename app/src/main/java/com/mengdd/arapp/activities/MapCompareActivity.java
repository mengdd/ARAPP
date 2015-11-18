package com.mengdd.arapp.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.mengdd.arapp.FrameHeaderViewModel;
import com.mengdd.arapp.FrameHeaderViewModel.OnBackListener;
import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;
import com.mengdd.map.CompareBottomViewModel;
import com.mengdd.map.autonavi.AutoNaviMapWithLocation;
import com.mengdd.map.baidu.BaiduMapWithLocation;
import com.mengdd.map.google.GoogleMapWithLocation;

public class MapCompareActivity extends Activity {

    private static final int GOOGLE_MAP = 0;
    private static final int BAIDU_MAP = 1;
    private static final int AUTO_NAVI_MAP = 2;

    private Activity mActivity = null;
    private int mCurrentSceneId = 0;
    private List<ViewModel> mViewModels = null;

    private ViewGroup mMapContainerGroup = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_compare_activity);
        mActivity = this;
        initHeader();
        initBottomMenu();
        initMapModels();

        switchMapScene(mCurrentSceneId);
    }

    private FrameHeaderViewModel mHeader = null;

    private void initHeader() { // header
        mHeader = new FrameHeaderViewModel(this);
        mHeader.onCreate(null);

        ViewGroup headerGourp = (ViewGroup) findViewById(R.id.title);
        headerGourp.addView(mHeader.getView(), 0);

        mHeader.setSettingVisibility(View.GONE);
        mHeader.setOnBackListener(new OnBackListener() {

            @Override
            public void onBack() {
                mActivity.finish();
            }
        });

    }

    private void setTitle(String string) {
        if (null != mHeader) {
            mHeader.setTitle(string);
        }
    }

    private void initBottomMenu() {
        // Bottom Menu
        CompareBottomViewModel bottomViewModel = new CompareBottomViewModel(
                this);
        bottomViewModel.onCreate(null);

        // bottom menu
        FrameLayout bottomFrameLayout = (FrameLayout) findViewById(R.id.bottom_tab);
        bottomFrameLayout.addView(bottomViewModel.getView());
        bottomViewModel.setOnClickListener(mBottomOnClickListener);

        // 默认选择第一个tab
        bottomViewModel.getButton(mCurrentSceneId).setSelected(true);
    }

    private void initMapModels() {
        mViewModels = new ArrayList<ViewModel>();

        ViewModel googleMap = new GoogleMapWithLocation(mActivity);
        mViewModels.add(googleMap);
        ViewModel baiduMap = new BaiduMapWithLocation(mActivity);
        mViewModels.add(baiduMap);
        ViewModel autonaviMap = new AutoNaviMapWithLocation(mActivity);
        mViewModels.add(autonaviMap);
        mMapContainerGroup = (ViewGroup) findViewById(R.id.map_content);

        for (ViewModel viewModel : mViewModels) {
            viewModel.onCreate(null);
        }

    }

    private final OnClickListener mBottomOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.tab_google:
                switchMapScene(GOOGLE_MAP);
                break;
            case R.id.tab_baidu:
                switchMapScene(BAIDU_MAP);
                break;
            case R.id.tab_autonavi:
                switchMapScene(AUTO_NAVI_MAP);
                break;
            default:
                break;
            }
        }
    };

    private void switchMapScene(int scene) {
        mCurrentSceneId = scene;
        switchTitle(scene);
        ViewModel viewModel = mViewModels.get(scene);
        viewModel.onResume(null);
        mMapContainerGroup.removeAllViews();
        mMapContainerGroup.addView(viewModel.getView());

    }

    private void switchTitle(int scene) {
        switch (scene) {
        case GOOGLE_MAP:
            setTitle(getResources().getString(R.string.googlemap_title));
            break;
        case BAIDU_MAP:
            setTitle(getResources().getString(R.string.baidumap_title));
            break;
        case AUTO_NAVI_MAP:
            setTitle(getResources().getString(R.string.autonavimap_title));
            break;
        default:
            break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (ViewModel model : mViewModels) {
            model.onResume(null);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        for (ViewModel model : mViewModels) {
            model.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (ViewModel model : mViewModels) {
            model.onPause();
        }
    }

    @Override
    protected void onStop() {

        for (ViewModel model : mViewModels) {
            model.onStop();
        }

        super.onStop();
    }

    @Override
    protected void onDestroy() {

        for (ViewModel model : mViewModels) {
            model.onDestroy();
        }
        super.onDestroy();
    }

}
