package com.mengdd.search;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import com.baidu.mapapi.search.MKPoiInfo;
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
import com.mengdd.location.baidu.BaiduLocationHelper;
import com.mengdd.utils.AppConstants;

public class SearchUIViewModel extends ViewModel implements MKSearchListener
{
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

	protected SearchUIViewModel(Activity activity, MKSearch mkSearch)
	{
		super(activity);
		mSearch = mkSearch;
	}

	@Override
	public void onCreate(Intent intent)
	{
		super.onCreate(intent);
		mRootView = mInflater.inflate(R.layout.search_ui, null);

		mResources = mActivity.getResources();

		keywordsView = (AutoCompleteTextView) mRootView
				.findViewById(R.id.keywords);
		sugAdapter = new ArrayAdapter<String>(mActivity,
				android.R.layout.simple_dropdown_item_1line);
		keywordsView.setAdapter(sugAdapter);
		keywordsView.addTextChangedListener(mTextWatcher);

		editCity = (EditText) mRootView.findViewById(R.id.city);

		Button searchBtn = (Button) mRootView.findViewById(R.id.search_btn);
		searchBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				searchButtonProcess(v);

			}
		});

		mRadiusTextView = (TextView) mRootView.findViewById(R.id.radius);
		mRadiusSeekBar = (SeekBar) mRootView.findViewById(R.id.search_radius);
		mRadiusSeekBar.setMax(MAX_RADIUS);
		mRadiusSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{

			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser)
			{
				mRadiusTextView.setText(mResources
						.getString(R.string.search_radius)
						+ ": "
						+ progress
						+ "m");

			}
		});

		// mode choose
		initSearchModeRadioGroup();

	}

	private void initSearchModeRadioGroup()
	{
		mCityModeView = (View) mRootView.findViewById(R.id.city_mode);
		mNearModeView = (View) mRootView.findViewById(R.id.near_mode);

		mRadioGroup = (RadioGroup) mRootView.findViewById(R.id.radioGroup1);
		mRadioButtonCity = (RadioButton) mRootView
				.findViewById(R.id.radio_mode_inCity);
		mRadioButtonNear = (RadioButton) mRootView
				.findViewById(R.id.radio_mode_nearby);

		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				switch (checkedId)
				{
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
	public View getView()
	{
		return mRootView;
	}

	@Override
	public void onStop()
	{
		super.onStop();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public void onResume(Intent intent)
	{
		super.onResume(intent);

	}

	@Override
	public void onPause()
	{
		super.onPause();
	}

	private TextWatcher mTextWatcher = new TextWatcher()
	{

		@Override
		public void onTextChanged(CharSequence cs, int start, int before,
				int count)
		{
			if (cs.length() <= 0)
			{
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
				int after)
		{

		}

		@Override
		public void afterTextChanged(Editable s)
		{

		}
	};

	/**
	 * 影响搜索按钮点击事件
	 * 
	 * @param v
	 */
	public void searchButtonProcess(View v)
	{
		int result = -1;
		if (null != mSearch)
		{

			// Search in the city
			if (mRadioButtonCity.isChecked())
			{

				result = mSearch.poiSearchInCity(editCity.getText().toString(),
						keywordsView.getText().toString());
			}
			else if (mRadioButtonNear.isChecked())
			{
				// Search nearby
				// keywords
				String input = keywordsView.getText().toString();

				// location
				BDLocation location = GlobalARData.getCurrentBaiduLocation();
				GeoPoint geoPoint = BaiduLocationHelper
						.getGeoPointFromBDLocation(location);

				// radius
				int radius = mRadiusSeekBar.getProgress();

				// public int poiMultiSearchNearBy(String[] keys,GeoPoint pt,
				// int radius)
				result = mSearch.poiSearchNearBy(input, geoPoint, radius);
			}

			if (0 == result)
			{
				// search succefully, turn to the result page
				if (null != mSearchSuccessListener)
				{
					mSearchSuccessListener.onSearchCompleted();
				}

			}
			else
			{
				Toast.makeText(mActivity,
						"Search Failed! Please Check the Network!",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	public interface OnSearchSuccessListener
	{
		public void onSearchCompleted();
	}

	private OnSearchSuccessListener mSearchSuccessListener = null;

	public void setOnSearchSuccessListener(OnSearchSuccessListener listener)
	{
		mSearchSuccessListener = listener;
	}

	@Override
	public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError)
	{

	}

	@Override
	public void onGetTransitRouteResult(MKTransitRouteResult result, int iError)
	{
	}

	@Override
	public void onGetSuggestionResult(MKSuggestionResult result, int iError)
	{
		Log.i(AppConstants.LOG_TAG, "onGetSuggestionResult: " + iError);
		if (result == null || result.getAllSuggestions() == null)
		{
			return;
		}
		sugAdapter.clear();
		for (MKSuggestionInfo info : result.getAllSuggestions())
		{
			if (info.key != null)
			{
				sugAdapter.add(info.key);
			}
		}
		sugAdapter.notifyDataSetChanged();
	}

	@Override
	public void onGetPoiResult(MKPoiResult result, int type, int iError)
	{

	}

	@Override
	public void onGetPoiDetailSearchResult(int type, int iError)
	{
	}

	@Override
	public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError)
	{

	}

	@Override
	public void onGetBusDetailResult(MKBusLineResult result, int iError)
	{
	}

	@Override
	public void onGetAddrResult(MKAddrInfo result, int iError)
	{
	}

	@Override
	public void onGetShareUrlResult(MKShareUrlResult result, int type, int iError)
	{
		
	}

}
