package com.mengdd.custommarker;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.mengdd.arapp.FrameHeaderViewModel;
import com.mengdd.arapp.R;
import com.mengdd.arapp.FrameHeaderViewModel.OnBackListener;
import com.mengdd.components.ViewModel;
import com.mengdd.map.baidu.BaiduMapHelper;

public class MainCustomMarkerViewModel extends ViewModel {
    public enum CustomMarkerScene {
        Map, List, RealScene
    }

    private CustomMarkerScene mCurrentScene = CustomMarkerScene.Map;

    private View mRootView = null;

    private FrameHeaderViewModel mHeaderViewModel = null;
    private Resources resources = null;

    private List<ViewModel> mViewModels = null;

    private MapCustomMarkerViewModel mMapViewModel = null;
    private ListCustomMarkerViewModel mListViewModel = null;
    private RealSceneCMViewModel mRealSceneViewModel = null;

    private View mMapView = null;
    private View mListView = null;
    private View mRealSceneView = null;
    private RelativeLayout mContentLayout = null;

    public MainCustomMarkerViewModel(Activity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null == BaiduMapHelper.getMapManager()) {
            BaiduMapHelper.initBaiduMapManager(mActivity);
        }

        mRootView = mInflater.inflate(R.layout.custom_main, null);

        initHeader();
        initBottom();

        // map
        mMapViewModel = new MapCustomMarkerViewModel(mActivity);
        // list
        mListViewModel = new ListCustomMarkerViewModel(mActivity);
        // real scene
        mRealSceneViewModel = new RealSceneCMViewModel(mActivity);

        mViewModels = new ArrayList<ViewModel>();

        mViewModels.add(mMapViewModel);
        mViewModels.add(mListViewModel);
        mViewModels.add(mRealSceneViewModel);

        for (ViewModel viewModel : mViewModels) {
            viewModel.onCreate(null);
        }

        mMapView = mMapViewModel.getView();
        mListView = mListViewModel.getView();
        mRealSceneView = mRealSceneViewModel.getView();

        mContentLayout = (RelativeLayout) mRootView
                .findViewById(R.id.main_content);

        switchScene(mCurrentScene);

    }

    private void initHeader() {
        // Header
        resources = mActivity.getResources();
        mHeaderViewModel = new FrameHeaderViewModel(mActivity);
        mHeaderViewModel.onCreate(null);
        mHeaderViewModel.setSettingVisibility(View.GONE);
        mHeaderViewModel.setTitle(resources
                .getString(R.string.custom_marker_title));
        ViewGroup headerGourp = (ViewGroup) mRootView.findViewById(R.id.title);
        headerGourp.addView(mHeaderViewModel.getView(), 0);
        mHeaderViewModel.setOnBackListener(new OnBackListener() {

            @Override
            public void onBack() {
                // mActivity.onKeyDown(KeyEvent.KEYCODE_BACK, new KeyEvent(
                // KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));

                mActivity.finish();

            }
        });

    }

    private void initBottom() {

        // Bottom Menu
        CustomBottomViewModel bottomViewModel = new CustomBottomViewModel(
                mActivity);
        bottomViewModel.onCreate(null);
        // bottom menu
        FrameLayout bottomFrameLayout = (FrameLayout) mRootView
                .findViewById(R.id.bottom_menu);
        bottomFrameLayout.addView(bottomViewModel.getView());
        bottomViewModel.setOnClickListener(mBottomOnClickListener);

        bottomViewModel.getButton(0).setSelected(true);
    }

    private OnClickListener mBottomOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.custom_map:

                    switchScene(CustomMarkerScene.Map);

                    break;
                case R.id.custom_list:

                    switchScene(CustomMarkerScene.List);

                    break;
                case R.id.custom_realscene:

                    switchScene(CustomMarkerScene.RealScene);

                    break;

                default:
                    break;
            }

        }

    };

    private void switchScene(CustomMarkerScene scene) {

        switch (scene) {
            case Map:

                if (CustomMarkerScene.RealScene == mCurrentScene) {
                    mRealSceneViewModel.onPause();
                }

                mContentLayout.removeAllViews();
                mMapViewModel.onResume(null);
                mContentLayout.addView(mMapView);

                break;
            case List:
                if (CustomMarkerScene.RealScene == mCurrentScene) {
                    mRealSceneViewModel.onPause();
                }
                if (CustomMarkerScene.Map == mCurrentScene) {
                    mMapViewModel.onPause();
                }

                mContentLayout.removeAllViews();
                mContentLayout.addView(mListView);

                break;
            case RealScene:
                if (CustomMarkerScene.Map == mCurrentScene) {
                    mMapViewModel.onPause();
                }
                mContentLayout.removeAllViews();
                mRealSceneViewModel.onResume(null);
                mContentLayout.addView(mRealSceneView);

                break;

            default:
                break;
        }

        mCurrentScene = scene;

    }

    @Override
    public View getView() {
        return mRootView;
    }

    @Override
    public void onStop() {
        super.onStop();

        for (ViewModel viewModel : mViewModels) {
            viewModel.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        for (ViewModel viewModel : mViewModels) {
            viewModel.onDestroy();
        }
    }

    @Override
    public void onResume(Intent intent) {
        super.onResume(intent);

        for (ViewModel viewModel : mViewModels) {
            viewModel.onResume(null);
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        for (ViewModel viewModel : mViewModels) {
            viewModel.onPause();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        for (ViewModel viewModel : mViewModels) {
            viewModel.onConfigurationChanged(newConfig);
        }
    }
}