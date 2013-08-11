package com.mengdd.custommarker;


import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;

public class ListCustomMarkerViewModel extends ViewModel
{
	private View mRootView = null;
	
	public ListCustomMarkerViewModel(Activity activity)
	{
		super(activity);
	}
	
	@Override
	public void onCreate(Intent intent)
	{
		super.onCreate(intent);
		
		mRootView = mInflater.inflate(R.layout.custom_list, null);
	}
	
	@Override
	public View getView()
	{
		return mRootView;
	}

}
