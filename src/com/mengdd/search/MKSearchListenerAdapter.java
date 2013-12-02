package com.mengdd.search;

import android.util.Log;

import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.mengdd.utils.AppConstants;

//This is an Adapter class to provider empty implements for MKSearchListener
//but since Java only allows Single inheritance, so this class is not used yet
public class MKSearchListenerAdapter implements MKSearchListener {

    @Override
    public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {

    }

    @Override
    public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {
    }

    @Override
    public void onGetSuggestionResult(MKSuggestionResult result, int iError) {
        Log.i(AppConstants.LOG_TAG, "onGetSuggestionResult: " + iError);

    }

    @Override
    public void onGetPoiResult(MKPoiResult result, int type, int iError) {

    }

    @Override
    public void onGetPoiDetailSearchResult(int type, int iError) {
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
