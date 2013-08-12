package com.mengdd.custommarker;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;
import com.mengdd.db.CustomMarkerTable;

public class ListCustomMarkerViewModel extends ViewModel
{
	private View mRootView = null;
	private ListView mListView = null;
	private List<MarkerItem> mMarkerItems = null;
	private MarkerItemAdapter mAdapter = null;
	private Button mRefreshBtn = null;
	
	public ListCustomMarkerViewModel(Activity activity)
	{
		super(activity);
	}
	
	@Override
	public void onCreate(Intent intent)
	{
		super.onCreate(intent);
		
		mRootView = mInflater.inflate(R.layout.custom_list, null);
		mListView = (ListView)mRootView.findViewById(R.id.custom_marker_list);
		

		mMarkerItems = loadAllCustomMarkers();
		
		mAdapter = new MarkerItemAdapter(mActivity, mMarkerItems);
		mListView.setAdapter(mAdapter);
		
		mRefreshBtn = (Button)mRootView.findViewById(R.id.refreshBtn);
		mRefreshBtn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				refreshData();
				
			}
		});
		
	}
	
	
	
	@Override
	public View getView()
	{
		return mRootView;
	}
	
	private void refreshData()
	{
		mMarkerItems = loadAllCustomMarkers();
		mAdapter.setMarkerData(mMarkerItems);
		mAdapter.notifyDataSetChanged();
		
	}
	private List<MarkerItem> loadAllCustomMarkers()
	{
		Collection<MarkerItem> markerItems = null;
		List<MarkerItem> resultList = null;
		
		markerItems = CustomMarkerTable.queryAllCustomMarkerItems();
		
		if(null != markerItems)
		{
			resultList = new ArrayList<MarkerItem>(markerItems);
			
		}
		
		return resultList;
		
	}

}
