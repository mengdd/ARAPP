package com.mengdd.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionInfo;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.mengdd.arapp.GlobalARData;
import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;
import com.mengdd.location.LocationListenerAdapter;
import com.mengdd.location.baidu.BaiduLocationHelper;
import com.mengdd.search.keywords.CategoryView.KeywordListener;
import com.mengdd.search.keywords.KeywordsNaviViewModel;
import com.mengdd.utils.AppConstants;

public class SearchUIViewModel extends ViewModel implements MKSearchListener {
    private View mRootView = null;
    private MKSearch mSearch = null;
    private Resources mResources = null;

    // main search ui
    private AutoCompleteTextView keywordsView = null;
    private ArrayAdapter<String> sugAdapter = null;

    // mode choose
    private RadioGroup mRadioGroup = null;
    private RadioButton mRadioButtonCity = null;
    private RadioButton mRadioButtonNear = null;

    // city mode
    private View mCityModeView = null;
    private EditText editCity = null;

    // near mode
    private View mNearModeView = null;
    private SeekBar mRadiusSeekBar = null;
    private TextView mRadiusTextView = null;
    private final int MAX_RADIUS = 1000;
    // current location
    private TextView mCurrentLocationTextView = null;

    // how to hide input keyboard
    private InputMethodManager mInputMethodManager = null;

    // keywords navigation
    private KeywordsNaviViewModel mKeywordsViewModel = null;

    protected SearchUIViewModel(Activity activity, MKSearch mkSearch) {
        super(activity);
        mSearch = mkSearch;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = mInflater.inflate(R.layout.search_ui, null);

        mResources = mActivity.getResources();

        keywordsView = (AutoCompleteTextView) mRootView
                .findViewById(R.id.keywords);
        sugAdapter = new ArrayAdapter<String>(mActivity,
                android.R.layout.simple_dropdown_item_1line);
        keywordsView.setAdapter(sugAdapter);
        keywordsView.addTextChangedListener(mTextWatcher);

        editCity = (EditText) mRootView.findViewById(R.id.city);

        // hide input keyboard
        mInputMethodManager = (InputMethodManager) mActivity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        View searchBtn = mRootView.findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 关闭软键盘
                mInputMethodManager.hideSoftInputFromWindow(
                        keywordsView.getWindowToken(), 0);

                // 提取keyword
                String keyword = keywordsView.getText().toString();

                // 查询关键字
                searchForKeyword(keyword);

            }
        });

        mRadiusTextView = (TextView) mRootView.findViewById(R.id.radius);
        mRadiusSeekBar = (SeekBar) mRootView.findViewById(R.id.search_radius);
        mRadiusSeekBar.setMax(MAX_RADIUS);
        mRadiusSeekBar
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                            int progress, boolean fromUser) {
                        mRadiusTextView.setText(mResources
                                .getString(R.string.search_radius)
                                + ": "
                                + progress + "m");

                    }
                });

        // mode choose
        initSearchModeRadioGroup();

        mCurrentLocationTextView = (TextView) mRootView
                .findViewById(R.id.current_location);

        // Keywords
        mKeywordsViewModel = new KeywordsNaviViewModel(mActivity);
        mKeywordsViewModel.onCreate(null);
        LinearLayout keyLayout = (LinearLayout) mRootView
                .findViewById(R.id.keywords_layout);
        keyLayout.addView(mKeywordsViewModel.getView());
        mKeywordsViewModel.setKeywordListener(mKeywordListener);


    }

    private KeywordListener mKeywordListener = new KeywordListener() {

        @Override
        public void onKeywordSelected(String keyword) {

            searchForKeyword(keyword);
        }
    };

    // when location changed, get the current location and search for the
    // address
    private LocationListenerAdapter mCurrentLocationListener = new LocationListenerAdapter() {

        @Override
        public void onLocationChanged(android.location.Location location) {

            BDLocation currentLocation = GlobalARData.getCurrentBaiduLocation();
            GeoPoint geoPoint = BaiduLocationHelper
                    .getGeoPointFromBDLocation(currentLocation);

            Log.i(AppConstants.LOG_TAG, "reverseGeocode: " + geoPoint);
            mSearch.reverseGeocode(geoPoint);

            // reverseGeocode和geocode的返回结果在都在MKSearchListener里的onGetAddrResult方法中

        };

    };

    private void initSearchModeRadioGroup() {
        mCityModeView = (View) mRootView.findViewById(R.id.city_mode);
        mNearModeView = (View) mRootView.findViewById(R.id.near_mode);

        mRadioGroup = (RadioGroup) mRootView.findViewById(R.id.radioGroup1);
        mRadioButtonCity = (RadioButton) mRootView
                .findViewById(R.id.radio_mode_inCity);
        mRadioButtonNear = (RadioButton) mRootView
                .findViewById(R.id.radio_mode_nearby);

        mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_mode_inCity:
                        mCityModeView.setVisibility(View.VISIBLE);
                        mNearModeView.setVisibility(View.GONE);

                        break;

                    case R.id.radio_mode_nearby:

                        mCityModeView.setVisibility(View.GONE);
                        mNearModeView.setVisibility(View.VISIBLE);

                        break;

                    default:
                        break;
                }

            }
        });

        mRadioButtonCity.setChecked(true);
        mCityModeView.setVisibility(View.VISIBLE);
        mNearModeView.setVisibility(View.GONE);
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

        GlobalARData.addLocationListener(mCurrentLocationListener);

    }

    @Override
    public void onPause() {
        super.onPause();
        GlobalARData.removeLocationListener(mCurrentLocationListener);
    }

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

    private void searchForKeyword(String keyword) {
        int result = -1;
        if (null != mSearch) {
            // Search in the city
            if (mRadioButtonCity.isChecked()) {

                result = mSearch.poiSearchInCity(editCity.getText().toString(),
                        keyword);
            } else if (mRadioButtonNear.isChecked()) {

                // location
                BDLocation location = GlobalARData.getCurrentBaiduLocation();
                GeoPoint geoPoint = BaiduLocationHelper
                        .getGeoPointFromBDLocation(location);

                // radius
                int radius = mRadiusSeekBar.getProgress();

                // public int poiMultiSearchNearBy(String[] keys,GeoPoint pt,
                // int radius)
                result = mSearch.poiSearchNearBy(keyword, geoPoint, radius);
            }

            if (0 == result) {
                // search succefully, turn to the result page
                if (null != mSearchSuccessListener) {
                    mSearchSuccessListener.onSearchCompleted();
                }

            } else {
                Toast.makeText(mActivity,
                        "Search Failed! Please Check the Network!",
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    public interface OnSearchSuccessListener {
        public void onSearchCompleted();
    }

    private OnSearchSuccessListener mSearchSuccessListener = null;

    public void setOnSearchSuccessListener(OnSearchSuccessListener listener) {
        mSearchSuccessListener = listener;
    }

    @Override
    public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {

    }

    @Override
    public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {
    }

    @Override
    public void onGetSuggestionResult(MKSuggestionResult result, int iError) {
        Log.i(AppConstants.LOG_TAG, "onGetSuggestionResult: " + iError);
        if (result == null || result.getAllSuggestions() == null) {
            return;
        }
        sugAdapter.clear();
        for (MKSuggestionInfo info : result.getAllSuggestions()) {
            if (info.key != null) {
                sugAdapter.add(info.key);
            }
        }
        sugAdapter.notifyDataSetChanged();
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
        // get the reverseGeocode result
        if (iError != 0) {
            String str = String.format("错误号：%d", iError);
            Toast.makeText(mActivity, str, Toast.LENGTH_LONG).show();
            return;
        }

        if (result.type == MKAddrInfo.MK_GEOCODE) {
            // 地理编码：通过地址检索坐标点
            String strInfo = String.format("纬度：%f 经度：%f",
                    result.geoPt.getLatitudeE6() / 1e6,
                    result.geoPt.getLongitudeE6() / 1e6);
            Toast.makeText(mActivity, strInfo, Toast.LENGTH_LONG).show();
        }
        if (result.type == MKAddrInfo.MK_REVERSEGEOCODE) {
            // 反地理编码：通过坐标点检索详细地址及周边poi
            String strInfo = result.strAddr;

            // Toast.makeText(mActivity, strInfo, Toast.LENGTH_LONG).show();

            mCurrentLocationTextView.setText(mResources
                    .getString(R.string.your_current_location) + " " + strInfo);
            mCurrentLocationTextView.setSelected(true);
        }
    }

    @Override
    public void onGetShareUrlResult(MKShareUrlResult result, int type,
            int iError) {

    }

}
