package com.mengdd.custommarker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;
import com.mengdd.db.CustomMarkerTable;
import com.mengdd.utils.AppConstants;

public class ListCustomMarkerViewModel extends ViewModel {
    private View mRootView = null;
    private ListView mListView = null;
    private List<MarkerItem> mMarkerItems = null;
    private List<MarkerItem> mDeleteCache = new ArrayList<MarkerItem>();
    private MarkerItemAdapter mAdapter = null;
    private View mRefreshBtn = null;
    private View mDeleteBtn = null;

    public ListCustomMarkerViewModel(Activity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRootView = mInflater.inflate(R.layout.custom_list, null);
        mListView = (ListView) mRootView.findViewById(R.id.custom_marker_list);

        mMarkerItems = loadAllCustomMarkers();

        mAdapter = new MarkerItemAdapter(mActivity, mMarkerItems);
        mListView.setAdapter(mAdapter);

        mRefreshBtn = mRootView.findViewById(R.id.refreshBtn);
        mRefreshBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                refreshData();

            }
        });

        mDeleteBtn = mRootView.findViewById(R.id.deleteBtn);
        mDeleteBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                deleteCheckedItems();
            }
        });

    }

    @Override
    public View getView() {
        return mRootView;
    }

    private void refreshData() {
        mMarkerItems = loadAllCustomMarkers();
        mAdapter.setMarkerData(mMarkerItems);
        mAdapter.notifyDataSetChanged();

    }

    private void deleteCheckedItems() {
        // delete checked items both in list and database
        if (null != mMarkerItems) {

            mDeleteCache.clear();
            for (int i = 0; i < mMarkerItems.size(); ++i) {
                if (mMarkerItems.get(i).isSelected()) {

                    mDeleteCache.add(mMarkerItems.get(i));
                }
            }
            // delete from list
            mMarkerItems.removeAll(mDeleteCache);
            mAdapter.notifyDataSetChanged();

            // delete items in delete cache from database
            int count = CustomMarkerTable.deleteItems(mDeleteCache);
            Log.w(AppConstants.LOG_TAG, "delete : " + count + " items");
        }

    }

    private List<MarkerItem> loadAllCustomMarkers() {
        Collection<MarkerItem> markerItems = null;
        List<MarkerItem> resultList = null;

        markerItems = CustomMarkerTable.queryAllCustomMarkerItems();

        if (null != markerItems) {
            resultList = new ArrayList<MarkerItem>(markerItems);

        }

        return resultList;

    }

}
