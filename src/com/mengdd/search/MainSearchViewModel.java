package com.mengdd.search;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionInfo;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.mengdd.arapp.FrameHeaderViewModel;
import com.mengdd.arapp.FrameHeaderViewModel.OnBackListener;
import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;
import com.mengdd.location.LocationModel;
import com.mengdd.location.baidu.BaiduLocationModel;
import com.mengdd.map.baidu.BaiduMapHelper;
import com.mengdd.search.SearchUIViewModel.OnSearchSuccessListener;
import com.mengdd.utils.AppConstants;
import com.mengdd.utils.UIUtils;

public class MainSearchViewModel extends ViewModel {
    public enum SearchScene {
        None, SearchUI, Map, RealScene, ResultList
    }

    private SearchScene mCurrentScene = SearchScene.None;
    private View mRootView;

    private FrameHeaderViewModel mHeaderViewModel = null;
    private Resources resources = null;

    private List<ViewModel> mViewModels = null;

    private SearchMapViewModel mSearchMapViewModel = null;
    private SearchRealSceneViewModel mSearchRealSceneViewModel = null;
    private MKSearch mSearch = null;

    private FrameLayout mContentFrameLayout = null;
    private View mSearchUIView = null;
    private View mSearchResultView = null;
    private View mSearchMapView = null;
    private View mSearchRealSceneView = null;

    // main search view model contains location model to update current location
    // information
    // location
    private LocationModel mLocationModel = null;

    public MainSearchViewModel(Activity activity) {
        super(activity);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null == BaiduMapHelper.getMapManager()) {
            BaiduMapHelper.initBaiduMapManager(mActivity);
        }

        BMapManager mBMapManager = BaiduMapHelper.getMapManager();

        mRootView = mInflater.inflate(R.layout.search_main, null);

        mViewModels = new ArrayList<ViewModel>();

        initHeaderAndBottom();

        mSearch = new MKSearch();
        mSearch.init(mBMapManager, mSearchListener);

        // search ui
        SearchUIViewModel searchUIViewModel = new SearchUIViewModel(mActivity,
                mSearch);
        addSearchListener(searchUIViewModel);
        searchUIViewModel.setOnSearchSuccessListener(mOnSearchSuccessListener);
        mViewModels.add(searchUIViewModel);

        // search result
        SearchResultViewModel searchResultViewModel = new SearchResultViewModel(
                mActivity, mSearch);
        addSearchListener(searchResultViewModel);
        mViewModels.add(searchResultViewModel);

        // map
        mSearchMapViewModel = new SearchMapViewModel(mActivity, mSearch);
        addSearchListener(mSearchMapViewModel);
        mViewModels.add(mSearchMapViewModel);

        // real scene
        mSearchRealSceneViewModel = new SearchRealSceneViewModel(mActivity);
        addSearchListener(mSearchRealSceneViewModel);
        mViewModels.add(mSearchRealSceneViewModel);

        // location
        mLocationModel = new BaiduLocationModel(mActivity);

        for (ViewModel viewModel : mViewModels) {
            viewModel.onCreate(null);
        }

        // add Views in layout after the onCreate method
        mContentFrameLayout = (FrameLayout) mRootView
                .findViewById(R.id.main_content);
        mSearchUIView = searchUIViewModel.getView();

        mSearchResultView = searchResultViewModel.getView();

        mSearchRealSceneView = mSearchRealSceneViewModel.getView();

        mSearchMapView = mSearchMapViewModel.getView();

        switchScene(SearchScene.SearchUI);

    }

    private void initHeaderAndBottom() {
        // Header
        resources = mActivity.getResources();
        mHeaderViewModel = new FrameHeaderViewModel(mActivity);
        mHeaderViewModel.onCreate(null);
        mHeaderViewModel.setSettingVisibility(View.GONE);
        mHeaderViewModel.setTitle(resources.getString(R.string.search_title));
        ViewGroup headerGourp = (ViewGroup) mRootView.findViewById(R.id.title);
        headerGourp.addView(mHeaderViewModel.getView(), 0);
        mHeaderViewModel.setOnBackListener(new OnBackListener() {

            @Override
            public void onBack() {
                mActivity.finish();
                //
                // mActivity.onKeyDown(KeyEvent.KEYCODE_BACK, new KeyEvent(
                // KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));

            }
        });

        // Bottom Menu
        SearchBottomViewModel bottomViewModel = new SearchBottomViewModel(
                mActivity);
        bottomViewModel.onCreate(null);

        // bottom menu
        FrameLayout bottomFrameLayout = (FrameLayout) mRootView
                .findViewById(R.id.bottom_menu);
        bottomFrameLayout.addView(bottomViewModel.getView());
        bottomViewModel.setOnClickListener(mBottomOnClickListener);

        // 默认选择第一个tab
        bottomViewModel.getButton(0).setSelected(true);
    }

    private OnClickListener mBottomOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.i(AppConstants.LOG_TAG,
                    "bottom menu: onClickListener: " + v.getId());

            switch (v.getId()) {
                case R.id.search_menu_ui:

                    switchScene(SearchScene.SearchUI);

                    break;
                case R.id.search_menu_list:

                    switchScene(SearchScene.ResultList);

                    break;
                case R.id.search_menu_map:

                    switchScene(SearchScene.Map);

                    break;
                case R.id.search_menu_real:

                    switchScene(SearchScene.RealScene);

                    break;

                default:
                    break;
            }

        }
    };

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

        mLocationModel.registerLocationUpdates();

        for (ViewModel viewModel : mViewModels) {
            viewModel.onResume(null);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        mLocationModel.unregisterLocationUpdates();

        for (ViewModel viewModel : mViewModels) {
            viewModel.onPause();
        }
    }

    @Override
    public View getView() {
        return mRootView;
    }

    public void switchScene(SearchScene scene) {

        if (mCurrentScene == scene) {
            return;
        }
        switch (scene) {
            case SearchUI:

                if (SearchScene.Map == mCurrentScene) {
                    mSearchMapViewModel.onPause();
                }

                if (SearchScene.RealScene == mCurrentScene) {
                    mSearchRealSceneViewModel.onPause();
                }
                mContentFrameLayout.removeAllViews();
                mContentFrameLayout.addView(mSearchUIView);

                break;
            case Map:

                if (SearchScene.RealScene == mCurrentScene) {
                    mSearchRealSceneViewModel.onPause();
                }
                mContentFrameLayout.removeAllViews();
                mSearchMapViewModel.onResume(null);
                mContentFrameLayout.addView(mSearchMapView);

                break;
            case RealScene:
                if (SearchScene.Map == mCurrentScene) {
                    mSearchMapViewModel.onPause();
                }

                mContentFrameLayout.removeAllViews();

                mSearchRealSceneViewModel.onResume(null);
                mContentFrameLayout.addView(mSearchRealSceneView);

                break;
            case ResultList:
                if (SearchScene.Map == mCurrentScene) {
                    mSearchMapViewModel.onPause();
                }

                if (SearchScene.RealScene == mCurrentScene) {
                    mSearchRealSceneViewModel.onPause();
                }
                mContentFrameLayout.removeAllViews();
                mContentFrameLayout.addView(mSearchResultView);
                break;

            default:
                break;
        }

        mCurrentScene = scene;

    }

    private List<MKSearchListener> mSearchListeners = null;

    public void addSearchListener(MKSearchListener listener) {
        if (null == listener) {
            throw new IllegalArgumentException("listener is null!");
        }

        if (null == mSearchListeners) {
            mSearchListeners = new ArrayList<MKSearchListener>();
        }

        mSearchListeners.add(listener);

    }

    public boolean removeSearchListener(MKSearchListener listener) {
        boolean ret = false;
        if (null == listener) {
            throw new IllegalArgumentException("listener is null!");
        }

        if (null == mSearchListeners) {
            return ret;
        }

        ret = mSearchListeners.remove(listener);

        return ret;

    }

    private MKSearchListener mSearchListener = new MKSearchListener() {

        @Override
        public void onGetWalkingRouteResult(MKWalkingRouteResult result,
                int iError) {
            Log.i(AppConstants.LOG_TAG, "onGetWalkingRouteResult: " + iError);

            if (null != mSearchListeners) {
                for (MKSearchListener listener : mSearchListeners) {
                    listener.onGetWalkingRouteResult(result, iError);
                }
            }

        }

        @Override
        public void onGetTransitRouteResult(MKTransitRouteResult result,
                int iError) {
            Log.i(AppConstants.LOG_TAG, "onGetTransitRouteResult: " + iError);
            if (null != mSearchListeners) {
                for (MKSearchListener listener : mSearchListeners) {
                    listener.onGetTransitRouteResult(result, iError);
                }
            }
        }

        @Override
        public void onGetSuggestionResult(MKSuggestionResult result, int iError) {
            Log.i(AppConstants.LOG_TAG, "onGetSuggestionResult: " + iError);
            if (result == null || result.getAllSuggestions() == null) {
                return;
            }

            if (null != mSearchListeners) {
                for (MKSearchListener listener : mSearchListeners) {
                    listener.onGetSuggestionResult(result, iError);
                }
            }
        }

        @Override
        public void onGetPoiResult(MKPoiResult result, int type, int iError) {
            Log.i(AppConstants.LOG_TAG, "onGetPoiResult: " + iError);

            // 错误号可参考MKEvent中的定义
            if (iError != 0 || result == null) {
                Toast.makeText(mActivity, "Sorry, No result!",
                        Toast.LENGTH_LONG).show();
                return;
            }
            if (null != mSearchListeners) {
                for (MKSearchListener listener : mSearchListeners) {
                    listener.onGetPoiResult(result, type, iError);
                }
            }
        }

        @Override
        public void onGetPoiDetailSearchResult(int type, int iError) {
            Log.i(AppConstants.LOG_TAG, "onGetPoiDetailSearchResult: " + iError);
            if (null != mSearchListeners) {
                for (MKSearchListener listener : mSearchListeners) {
                    listener.onGetPoiDetailSearchResult(type, iError);
                }
            }
        }

        @Override
        public void onGetDrivingRouteResult(MKDrivingRouteResult result,
                int iError) {
            Log.i(AppConstants.LOG_TAG, "onGetDrivingRouteResult: " + iError);
            if (null != mSearchListeners) {
                for (MKSearchListener listener : mSearchListeners) {
                    listener.onGetDrivingRouteResult(result, iError);
                }
            }

        }

        @Override
        public void onGetBusDetailResult(MKBusLineResult result, int iError) {
            Log.i(AppConstants.LOG_TAG, "onGetBusDetailResult: " + iError);

            if (null != mSearchListeners) {
                for (MKSearchListener listener : mSearchListeners) {
                    listener.onGetBusDetailResult(result, iError);
                }
            }
        }

        @Override
        public void onGetAddrResult(MKAddrInfo result, int iError) {
            Log.i(AppConstants.LOG_TAG, "onGetAddrResult: " + iError);

            if (null != mSearchListeners) {
                for (MKSearchListener listener : mSearchListeners) {
                    listener.onGetAddrResult(result, iError);
                }
            }
        }

        @Override
        public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1,
                int arg2) {

        }
    };

    private OnSearchSuccessListener mOnSearchSuccessListener = new OnSearchSuccessListener() {

        @Override
        public void onSearchCompleted() {
            switchScene(SearchScene.ResultList);

        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        for (ViewModel viewModel : mViewModels) {
            viewModel.onConfigurationChanged(newConfig);
        }
    };

}
