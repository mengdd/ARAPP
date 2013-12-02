package com.mengdd.search;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

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
import com.mengdd.arapp.GlobalARData;
import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;
import com.mengdd.map.baidu.BaiduMapViewModel;
import com.mengdd.map.baidu.BaiduMyLocationOverlay;
import com.mengdd.poi.baidu.BaiduPOIOverlay;
import com.mengdd.poi.baidu.BaiduPOIOverlay.PoiTapListener;
import com.mengdd.utils.AppConstants;

public class SearchMapViewModel extends ViewModel implements MKSearchListener {
    private View mRootView = null;

    private BaiduMapViewModel mMapViewModel = null;
    private BaiduMyLocationOverlay myLocationOverlay = null;
    private BaiduPOIOverlay mPoiOverlay = null;

    private MKSearch mSearch = null;

    protected SearchMapViewModel(Activity activity, MKSearch search) {
        super(activity);
        mSearch = search;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = mInflater.inflate(R.layout.search_map, null);

        // basic map
        mMapViewModel = new BaiduMapViewModel(mActivity);
        mMapViewModel.onCreate(null);
        FrameLayout frameLayout = (FrameLayout) mRootView
                .findViewById(R.id.map);
        frameLayout.addView(mMapViewModel.getView(), 0);

        // my location overlay
        myLocationOverlay = new BaiduMyLocationOverlay(mMapViewModel.getMap());
        myLocationOverlay.setLocationData(GlobalARData
                .getCurrentBaiduLocation());

        // poi overlay
        mPoiOverlay = new BaiduPOIOverlay(mActivity, mMapViewModel.getMap());
        mPoiOverlay.setPoiTabListener(mPoiTapListener);

    }

    private PoiTapListener mPoiTapListener = new PoiTapListener() {

        @Override
        public void onPoiTap(int index, MKPoiInfo poiInfo) {

            if (poiInfo.hasCaterDetails) {
                Log.i(AppConstants.LOG_TAG, "hasCaterDetails == true "
                        + poiInfo.uid);
                // 显示详情页
                mSearch.poiDetailSearch(poiInfo.uid);

            }
            else {
                Log.i(AppConstants.LOG_TAG, "hasCaterDetails == false "
                        + poiInfo.uid);
                Toast.makeText(mActivity, poiInfo.name, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };

    @Override
    public View getView() {
        return mRootView;
    }

    @Override
    public void onStop() {
        super.onStop();

        mMapViewModel.onStop();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        mMapViewModel.onDestroy();
    }

    @Override
    public void onResume(Intent intent) {
        super.onResume(intent);
        mMapViewModel.onResume(null);

        GlobalARData.addLocationListener(mLocationListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapViewModel.onPause();

        GlobalARData.removeLocationListener(mLocationListener);
    }

    private LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            myLocationOverlay.setLocationData(GlobalARData
                    .getCurrentBaiduLocation());
        }
    };

    @Override
    public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {

    }

    @Override
    public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {
    }

    @Override
    public void onGetSuggestionResult(MKSuggestionResult result, int iError) {
    }

    @Override
    public void onGetPoiResult(MKPoiResult result, int type, int iError) {
        if (null == result || 0 != iError) {
            return;
        }
        if (result.getCurrentNumPois() > 0) {
            mPoiOverlay.setData(result.getAllPoi());
        }

    }

    @Override
    public void onGetPoiDetailSearchResult(int type, int iError) {
        if (iError != 0) {
            Toast.makeText(mActivity, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(mActivity, "成功，查看详情页面", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {

    }

    @Override
    public void onGetBusDetailResult(MKBusLineResult result, int iError) {
    }

    @Override
    public void onGetAddrResult(MKAddrInfo result, int iError) {
    }

    @Override
    public void onGetShareUrlResult(MKShareUrlResult result, int type,
            int iError) {

    }

}
