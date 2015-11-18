package com.mengdd.poi.baidu;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;
import com.mengdd.map.baidu.BaiduMapHelper;
import com.mengdd.utils.AppConstants;

public class BaiduPoiSearchViewModel extends ViewModel {
    private MKSearch mSearch = null;
    private View mRootView = null;

    private AutoCompleteTextView keywordsView = null;
    private ArrayAdapter<String> sugAdapter = null;
    private EditText editCity = null;
    private int load_Index;

    // 显示出来
    private ListView mListView = null;
    private ArrayAdapter<String> mListAdapter = null;
    private List<String> mListData = null;

    public BaiduPoiSearchViewModel(Activity activity) {
        super(activity);
        if (null == BaiduMapHelper.getMapManager()) {
            BaiduMapHelper.initBaiduMapManager(mActivity);
        }

        BMapManager mBMapManager = BaiduMapHelper.getMapManager();

        mRootView = mInflater.inflate(R.layout.baidu_poi_view_model, null);

        mSearch = new MKSearch();
        mSearch.init(mBMapManager, mSearchListener);

        keywordsView = (AutoCompleteTextView) mRootView
                .findViewById(R.id.searchkey);
        sugAdapter = new ArrayAdapter<String>(mActivity,
                android.R.layout.simple_dropdown_item_1line);
        keywordsView.setAdapter(sugAdapter);
        keywordsView.addTextChangedListener(mTextWatcher);

        editCity = (EditText) mRootView.findViewById(R.id.city);

        Button searchBtn = (Button) mRootView.findViewById(R.id.search);
        searchBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                searchButtonProcess(v);

            }
        });

        Button goButton = (Button) mRootView.findViewById(R.id.map_next_data);
        goButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                goToNextPage(v);
            }
        });

        mListView = (ListView) mRootView.findViewById(R.id.resultList);
        mListData = new ArrayList<String>();
        mListAdapter = new ArrayAdapter<String>(mActivity,
                android.R.layout.simple_list_item_1, mListData);
        mListView.setAdapter(mListAdapter);
    }

    @Override
    public View getView() {
        return mRootView;
    }

    private MKSearchListener mSearchListener = new MKSearchListener() {

        @Override
        public void onGetWalkingRouteResult(MKWalkingRouteResult result,
                int iError) {
            Log.i(AppConstants.LOG_TAG, "onGetWalkingRouteResult: " + iError);

        }

        @Override
        public void onGetTransitRouteResult(MKTransitRouteResult result,
                int iError) {
            Log.i(AppConstants.LOG_TAG, "onGetTransitRouteResult: " + iError);
        }

        @Override
        public void onGetSuggestionResult(MKSuggestionResult result, int iError) {
            Log.i(AppConstants.LOG_TAG, "onGetSuggestionResult: " + iError);
            if (result == null || result.getAllSuggestions() == null) {
                return;
            }
            sugAdapter.clear();
            for (MKSuggestionInfo info : result.getAllSuggestions()) {
                if (info.key != null)
                    sugAdapter.add(info.key);
            }
            sugAdapter.notifyDataSetChanged();
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

            mListData.clear();
            for (MKPoiInfo info : result.getAllPoi()) {

                mListData.add(info.address);
            }

            mListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onGetPoiDetailSearchResult(int type, int iError) {
            Log.i(AppConstants.LOG_TAG, "onGetPoiDetailSearchResult: " + iError);
        }

        @Override
        public void onGetDrivingRouteResult(MKDrivingRouteResult result,
                int iError) {
            Log.i(AppConstants.LOG_TAG, "onGetDrivingRouteResult: " + iError);

        }

        @Override
        public void onGetBusDetailResult(MKBusLineResult result, int iError) {
            Log.i(AppConstants.LOG_TAG, "onGetBusDetailResult: " + iError);
        }

        @Override
        public void onGetAddrResult(MKAddrInfo result, int iError) {
            Log.i(AppConstants.LOG_TAG, "onGetAddrResult: " + iError);
        }

        @Override
        public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1,
                int arg2) {

        }
    };

    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence cs, int start, int before,
                int count) {
            if (cs.length() <= 0) {
                return;
            }
            String city = ((EditText) mRootView.findViewById(R.id.city))
                    .getText().toString();
            /**
             * Use suggestion search service of Baidu，the results are updated in
             * onSuggestionResult()
             */
            mSearch.suggestionSearch(cs.toString(), city);

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 影响搜索按钮点击事件
     * 
     * @param v
     */
    public void searchButtonProcess(View v) {

        mSearch.poiSearchInCity(editCity.getText().toString(), keywordsView
                .getText().toString());
    }

    public void goToNextPage(View v) {
        // 搜索下一组poi
        int flag = mSearch.goToPoiPage(++load_Index);
        if (flag != 0) {
            Toast.makeText(mActivity, "先搜索开始，然后再搜索下一组数据", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
