package com.mengdd.custommarker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.mapapi.map.MapView;
import com.mengdd.arapp.GlobalARData;
import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;
import com.mengdd.db.CustomMarkerTable;
import com.mengdd.location.baidu.BaiduLocationModel;
import com.mengdd.map.BasicMapViewModel;
import com.mengdd.map.baidu.BaiduMapViewModel;
import com.mengdd.map.baidu.BaiduMyLocationOverlay;
import com.mengdd.utils.AppConstants;
import com.mengdd.utils.DialogUtils;
import com.mengdd.utils.DialogUtils.OnSaveCustomMarkerListener;

public class MapCustomMarkerViewModel extends ViewModel {

    private View mRootView = null;

    private Resources resources = null;

    private List<ViewModel> mViewModels = null;

    private BasicMapViewModel mMapViewModel = null;
    private MapView mapView = null;
    private CustomMarkersOverlay mMarkersOverlay = null;

    private BaiduLocationModel mLocationModel = null;// location model
    private BaiduMyLocationOverlay mLocationOverlay = null;

    private View mGoButton = null;
    private View mNewMarkerBtn = null;
    private View mSaveMarkerBtn = null;
    private View mLoadMarkersBtn = null;

    public MapCustomMarkerViewModel(Activity activity) {
        super(activity);
        resources = activity.getResources();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRootView = mInflater.inflate(R.layout.custom_map, null);

        mMapViewModel = new BaiduMapViewModel(mActivity);
        mViewModels = new ArrayList<ViewModel>();
        mViewModels.add(mMapViewModel);

        for (ViewModel viewModel : mViewModels) {
            viewModel.onCreate(null);
        }

        RelativeLayout mainLayout = (RelativeLayout) mRootView
                .findViewById(R.id.main_content);
        mainLayout.addView(mMapViewModel.getView(), 0);

        mGoButton = mRootView.findViewById(R.id.goto_my);
        mGoButton.setOnClickListener(mOnGoToClickListener);

        mNewMarkerBtn = mRootView.findViewById(R.id.new_marker);
        mNewMarkerBtn.setOnClickListener(mOnAddClickListener);

        mSaveMarkerBtn = mRootView.findViewById(R.id.save_marker);
        mSaveMarkerBtn.setOnClickListener(mOnSaveClickListener);
        mSaveMarkerBtn.setVisibility(View.GONE);

        mLoadMarkersBtn = mRootView.findViewById(R.id.load_all_markers);
        mLoadMarkersBtn.setOnClickListener(mLoadClickListener);

        mapView = (MapView) mMapViewModel.getMap();
        // marker overlay
        mMarkersOverlay = new CustomMarkersOverlay(mActivity, mapView);

        // my location overlay
        mLocationModel = new BaiduLocationModel(mActivity);
        mLocationOverlay = new BaiduMyLocationOverlay(mapView);

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

        mLocationModel.registerLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();

        for (ViewModel viewModel : mViewModels) {
            viewModel.onPause();
        }

        mLocationModel.unregisterLocationUpdates();
    }

    private OnClickListener mOnGoToClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.i(AppConstants.LOG_TAG, "go to my place");

            mLocationOverlay.setGoToEnabled(true);
            mLocationOverlay.setZoomEnabled(true);
            mLocationOverlay.setLocationData(GlobalARData
                    .getCurrentBaiduLocation());

        }
    };

    private OnClickListener mOnAddClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.i(AppConstants.LOG_TAG, "add new marker");

            mMarkersOverlay.initNewMarker();
            mNewMarkerBtn.setVisibility(View.GONE);
            mSaveMarkerBtn.setVisibility(View.VISIBLE);

        }
    };

    private OnClickListener mOnSaveClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.i(AppConstants.LOG_TAG, "save new marker");

            MarkerItem markerItem = mMarkersOverlay.getMarkerItemInEdit();

            if (null != markerItem) {
                DialogUtils.showSaveMarkerDialog(mActivity,
                        onSaveCustomMarkerListener, markerItem);
            }

        }
    };

    private OnSaveCustomMarkerListener onSaveCustomMarkerListener = new OnSaveCustomMarkerListener() {

        @Override
        public void onSaveMarker(MarkerItem markerItem) {
            boolean result = mMarkersOverlay.saveMarkerItemToDb(markerItem);

            if (result) {
                Log.i(AppConstants.LOG_TAG, "save to db successfully");
                Toast.makeText(mActivity,
                        resources.getString(R.string.save_success),
                        Toast.LENGTH_SHORT).show();

            } else {
                Log.i(AppConstants.LOG_TAG, "save to db failed");
                Toast.makeText(mActivity,
                        resources.getString(R.string.save_failed),
                        Toast.LENGTH_SHORT).show();
            }

            mNewMarkerBtn.setVisibility(View.VISIBLE);
            mSaveMarkerBtn.setVisibility(View.GONE);

        }
    };

    public OnClickListener mLoadClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            loadAllCustomMarkers();

        }
    };

    public void setVisibility(int visibility) {
        if (View.GONE == visibility) {
            mRootView.setVisibility(View.GONE);

        } else if (View.VISIBLE == visibility) {

            mRootView.setVisibility(View.VISIBLE);

        }
    }

    public void loadAllCustomMarkers() {
        Collection<MarkerItem> markerItems = CustomMarkerTable
                .queryAllCustomMarkerItems();

        mMarkersOverlay.clearMarkers();
        mNewMarkerBtn.setVisibility(View.VISIBLE);
        mSaveMarkerBtn.setVisibility(View.GONE);
        if (null != markerItems) {

            for (MarkerItem item : markerItems) {
                mMarkersOverlay.addDBMarkerToOverlay(item);
            }
        } else {
            Toast.makeText(mActivity, "no marker!", Toast.LENGTH_SHORT).show();
        }
    }
}
