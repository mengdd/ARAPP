package com.mengdd.custommarker;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.mapapi.map.MapView;
import com.mengdd.arapp.FrameHeaderViewModel;
import com.mengdd.arapp.GlobalARData;
import com.mengdd.arapp.R;
import com.mengdd.arapp.FrameHeaderViewModel.OnBackListener;
import com.mengdd.components.ViewModel;
import com.mengdd.location.baidu.BaiduLocationModel;
import com.mengdd.map.BasicMapViewModel;
import com.mengdd.map.baidu.BaiduMapViewModel;
import com.mengdd.map.baidu.BaiduMyLocationOverlay;
import com.mengdd.utils.AppConstants;

public class MainCustomMarkerViewModel extends ViewModel
{
	private View mRootView = null;

	private FrameHeaderViewModel mHeaderViewModel = null;
	private Resources resources = null;

	private List<ViewModel> mViewModels = null;

	private BasicMapViewModel mMapViewModel = null;
	private MapView mapView= null;
	private CustomMarkersOverlay mMarkersOverlay = null;
	private BaiduLocationModel mLocationModel = null;
	private BaiduMyLocationOverlay mLocationOverlay = null;

	private Button mGoButton = null;
	private Button mNewMarkerBtn = null;
	private Button mSaveMarkerBtn = null;

	public MainCustomMarkerViewModel(Activity activity)
	{
		super(activity);
	}

	@Override
	public void onCreate(Intent intent)
	{
		super.onCreate(intent);

		mRootView = mInflater.inflate(R.layout.custom_main, null);

		initHeader();

		mMapViewModel = new BaiduMapViewModel(mActivity);
		mViewModels = new ArrayList<ViewModel>();
		mViewModels.add(mMapViewModel);

		for (ViewModel viewModel : mViewModels)
		{
			viewModel.onCreate(null);
		}

		RelativeLayout mainLayout = (RelativeLayout) mRootView
				.findViewById(R.id.main_content);
		mainLayout.addView(mMapViewModel.getView(), 0);

		mGoButton = (Button) mRootView.findViewById(R.id.goto_my);
		mGoButton.setOnClickListener(mOnGoToClickListener);

		mNewMarkerBtn = (Button) mRootView.findViewById(R.id.new_marker);
		mNewMarkerBtn.setOnClickListener(mOnAddClickListener);
		

		mSaveMarkerBtn = (Button) mRootView.findViewById(R.id.save_marker);
		mSaveMarkerBtn.setOnClickListener(mOnSaveClickListener);
		mSaveMarkerBtn.setVisibility(View.GONE);

		mapView = (MapView) mMapViewModel.getMap();
		// marker overlay
		mMarkersOverlay = new CustomMarkersOverlay(mActivity, mapView);

		// my location overlay
		mLocationModel = new BaiduLocationModel(mActivity);
		mLocationOverlay = new BaiduMyLocationOverlay(mapView);

	}

	private void initHeader()
	{
		// Header
		resources = mActivity.getResources();
		mHeaderViewModel = new FrameHeaderViewModel(mActivity);
		mHeaderViewModel.onCreate(null);
		mHeaderViewModel.setSettingVisibility(View.GONE);
		mHeaderViewModel.setTitle(resources.getString(R.string.search_title));
		ViewGroup headerGourp = (ViewGroup) mRootView.findViewById(R.id.title);
		headerGourp.addView(mHeaderViewModel.getView(), 0);
		mHeaderViewModel.setOnBackListener(new OnBackListener()
		{

			@Override
			public void onBack()
			{
				mActivity.onKeyDown(KeyEvent.KEYCODE_BACK, new KeyEvent(
						KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));

			}
		});

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

		for (ViewModel viewModel : mViewModels)
		{
			viewModel.onStop();
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		for (ViewModel viewModel : mViewModels)
		{
			viewModel.onDestroy();
		}
	}

	@Override
	public void onResume(Intent intent)
	{
		super.onResume(intent);

		for (ViewModel viewModel : mViewModels)
		{
			viewModel.onResume(null);
		}
		
		mLocationModel.registerLocationUpdates();
	}

	@Override
	public void onPause()
	{
		super.onPause();

		for (ViewModel viewModel : mViewModels)
		{
			viewModel.onPause();
		}
		
		mLocationModel.unregisterLocationUpdates();
	}

	private OnClickListener mOnGoToClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			Log.i(AppConstants.LOG_TAG, "go to my place");
			
			mLocationOverlay.setGoToEnabled(true);
			mLocationOverlay.setZoomEnabled(true);
			mLocationOverlay.setLocationData(GlobalARData.getCurrentBaiduLocation());
			

		}
	};

	private OnClickListener mOnAddClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			Log.i(AppConstants.LOG_TAG, "add new marker");
			
			mMarkersOverlay.initNewMarker();
			mNewMarkerBtn.setVisibility(View.GONE);
			mSaveMarkerBtn.setVisibility(View.VISIBLE);
			
			

		}
	};
	
	private OnClickListener mOnSaveClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			Log.i(AppConstants.LOG_TAG, "save new marker");
			

			
			boolean result = mMarkersOverlay.saveMarkerItemToDb();
			
			if(result)
			{
				Toast.makeText(mActivity, resources.getString(R.string.save_success),Toast.LENGTH_SHORT).show();
				
				mNewMarkerBtn.setVisibility(View.VISIBLE);
				mSaveMarkerBtn.setVisibility(View.GONE);
			}
			else {
				Toast.makeText(mActivity, resources.getString(R.string.save_failed),Toast.LENGTH_SHORT).show();
			}
			
	
			

		}
	};

}
