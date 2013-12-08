package com.mengdd.tests;

import java.util.ArrayList;
import java.util.List;

import com.mengdd.arapp.FrameHeaderViewModel;
import com.mengdd.arapp.R;
import com.mengdd.arapp.FrameHeaderViewModel.OnBackListener;
import com.mengdd.components.ViewModel;
import com.mengdd.location.LocationModel;
import com.mengdd.location.LocationView;
import com.mengdd.location.autonavi.AutoNaviLocationModel;
import com.mengdd.location.baidu.BaiduLocationModel;
import com.mengdd.location.google.GoogleLocationModel;
import com.mengdd.map.BasicMapViewModel;
import com.mengdd.map.CompareBottomViewModel;
import com.mengdd.map.autonavi.AutoNaviMapViewModel;
import com.mengdd.map.baidu.BaiduMapViewModel;
import com.mengdd.map.google.GoogleMapViewModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

public class BackupOfOldMapCompareActivity extends Activity {

    private static final int GOOGLE_MAP = 0;
    private static final int BAIDU_MAP = 1;
    private static final int AUTO_NAVI_MAP = 2;

    private Activity mActivity = null;
    private int mCurrentSceneId = 0;
    private List<BasicMapViewModel> mMapModels = null;
    private List<ViewModel> mViewModels = null;

    private ViewGroup mMapContainerGroup = null;

    // 定位
    private List<LocationModel> mLocationModels = null;
    private LocationView mLocationView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_compare_activity);
        mActivity = this;
        initHeader();
        initBottomMenu();
        mViewModels = new ArrayList<ViewModel>();
        initMapModels();
        initLocationModels();
        initLocationView();
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
        mMapModels = new ArrayList<BasicMapViewModel>();

        BasicMapViewModel googleMap = new GoogleMapViewModel(mActivity);
        mMapModels.add(googleMap);
        BasicMapViewModel baiduMap = new BaiduMapViewModel(mActivity);
        mMapModels.add(baiduMap);
        BasicMapViewModel autonaviMap = new AutoNaviMapViewModel(mActivity);
        mMapModels.add(autonaviMap);
        mMapContainerGroup = (ViewGroup) findViewById(R.id.map_content);

        for (ViewModel viewModel : mMapModels) {
            viewModel.onCreate(null);

            mViewModels.add(viewModel);
        }

    }

    private void initLocationModels() {
        mLocationModels = new ArrayList<LocationModel>();
        GoogleLocationModel googleLocationModel = new GoogleLocationModel(
                mActivity);

        mLocationModels.add(googleLocationModel);
        BaiduLocationModel baiduLocationModel = new BaiduLocationModel(
                mActivity);
        mLocationModels.add(baiduLocationModel);
        AutoNaviLocationModel autoNaviLocationModel = new AutoNaviLocationModel(
                mActivity);
        mLocationModels.add(autoNaviLocationModel);

        for (ViewModel viewModel : mLocationModels) {
            mViewModels.add(viewModel);
        }

    }

    private void initLocationView() {
        mLocationView = new LocationView(mActivity);
        mLocationView.onCreate(null);
        ViewGroup viewGroup = (ViewGroup) mActivity
                .findViewById(R.id.location_content);
        viewGroup.addView(mLocationView.getView());
    }

    private OnClickListener mBottomOnClickListener = new OnClickListener() {

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
        ViewModel viewModel = mMapModels.get(scene);
        viewModel.onResume(null);
        mMapContainerGroup.removeAllViews();
        mMapContainerGroup.addView(viewModel.getView());

        for (int i = 0; i < mLocationModels.size(); ++i) {
            LocationModel model = mLocationModels.get(i);
            if (i == scene) {
                model.setLocationChangedListener(mLocationView);
            } else {
                model.setLocationChangedListener(null);
            }
        }
        mLocationView.clearLocationInfo();

        // for (int i = 0; i < mMapModels.size(); ++i) {
        // View view = mMapModels.get(i).getView();
        // if (i == scene) {
        // view.setVisibility(View.VISIBLE);
        //
        // } else {
        // view.setVisibility(View.GONE);
        // }
        // }
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

        requestAllLocationUpdate();

    }

    private void requestAllLocationUpdate() {
        for (LocationModel model : mLocationModels) {
            model.registerLocationUpdates();
        }
    }

    private void cancelAllLocationUpdate() {
        for (LocationModel model : mLocationModels) {
            model.unregisterLocationUpdates();
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
        cancelAllLocationUpdate();
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
