package com.mengdd.search;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;
import com.mengdd.utils.AppConstants;

public class SearchResultViewModel extends ViewModel implements
        MKSearchListener {
    private View mRootView = null;
    private MKSearch mSearch = null;

    // 显示出来
    private TextView mInfoTextView = null;
    private TextView mPageTextView = null;
    private ListView mListView = null;
    private PoiInfoAdapter mListAdapter = null;
    private List<MKPoiInfo> mListData = null;

    private int currentPage = 0;
    private int pageCount = 0;

    protected SearchResultViewModel(Activity activity, MKSearch mkSearch) {
        super(activity);
        mSearch = mkSearch;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = mInflater.inflate(R.layout.search_result_list, null);

        mInfoTextView = (TextView) mRootView.findViewById(R.id.info);
        mPageTextView = (TextView) mRootView.findViewById(R.id.pageText);

        mListView = (ListView) mRootView.findViewById(R.id.search_list);
        mListData = new ArrayList<MKPoiInfo>();
        mListAdapter = new PoiInfoAdapter(mActivity, mListData);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);

        Button mNextButton = (Button) mRootView.findViewById(R.id.next_btn);
        mNextButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                goToNextPage(v);

            }
        });

        Button mPreButton = (Button) mRootView.findViewById(R.id.previous_btn);
        mPreButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                goToPreviousPage(v);

            }
        });
    }

    @Override
    public View getView() {
        return mRootView;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume(Intent intent) {
        super.onResume(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void goToPreviousPage(View v) {
        Log.i(AppConstants.LOG_TAG, "goToPreviousPage: current" + currentPage
                + ", count: " + pageCount);
        int flag = -1;
        if (currentPage > 0) {
            // 搜索下一组poi
            flag = mSearch.goToPoiPage(--currentPage);
        }

        if (flag != 0) {
            Toast.makeText(mActivity, "Search Failed!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void goToNextPage(View v) {
        Log.i(AppConstants.LOG_TAG, "goToNextPage: current: " + currentPage
                + ", count: " + pageCount);
        // 搜索下一组poi
        int flag = -1;
        if (currentPage < pageCount) {
            flag = mSearch.goToPoiPage(++currentPage);
        }
        if (flag != 0) {
            Toast.makeText(mActivity, "Search Failed!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

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
        Log.w(AppConstants.LOG_TAG,
                "goto SearchResultViewModel: onGetPoiResult: " + iError);

        // 错误号可参考MKEvent中的定义
        if (iError != 0 || result == null) {
            Toast.makeText(mActivity, "Sorry, No result!", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // update information
        currentPage = result.getPageIndex();
        pageCount = result.getNumPages();
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("POI numbers: " + result.getNumPois() + "\n");
        stringBuffer.append("Page Count: " + pageCount + "\n");
        stringBuffer.append("Current Page: " + currentPage);
        mInfoTextView.setText(stringBuffer.toString());

        // page information

        mPageTextView.setText((currentPage + 1) + "/" + pageCount);
        Log.i(AppConstants.LOG_TAG, "page info: " + (currentPage + 1) + "/"
                + pageCount);
        // add result to the list
        mListData.clear();
        for (MKPoiInfo info : result.getAllPoi()) {
            mListData.add(info);
        }

        mListAdapter.notifyDataSetChanged();
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
    public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1, int arg2) {

    }

    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            Log.i(AppConstants.LOG_TAG, "onItemClick: " + position);

            MKPoiInfo currentInfo = mListData.get(position);
            if (currentInfo.hasCaterDetails) {

                mSearch.poiDetailSearch(currentInfo.uid);
            }
            else {
                Toast.makeText(mActivity, "Sorry, No Details",
                        Toast.LENGTH_LONG).show();
            }

        }
    };
}
