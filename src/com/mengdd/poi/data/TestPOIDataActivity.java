package com.mengdd.poi.data;

import java.util.List;

import com.mengdd.ar.ui.Marker;
import com.mengdd.arapp.GlobalARData;
import com.mengdd.arapp.R;
import com.mengdd.location.LocationModel;
import com.mengdd.location.google.GoogleLocationModel;
import com.mengdd.utils.AppConstants;


import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class TestPOIDataActivity extends Activity implements LocationListener
{
	private POIViewModel mPOIViewModel = null;
	private LocationModel mLocationModel = null;
	private TextView mTextView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_poi_data);
		mTextView= (TextView)findViewById(R.id.myTextView);
		mLocationModel = new GoogleLocationModel(this);
		mPOIViewModel = new POIViewModel();

	}

	@Override
	protected void onResume()
	{
		super.onResume();

		mLocationModel.registerLocationUpdates();
		GlobalARData.addLocationListener(mPOIViewModel);
		GlobalARData.addLocationListener(this);
	
	}

	@Override
	protected void onPause()
	{
		super.onPause();

		mLocationModel.unregisterLocationUpdates();
		GlobalARData.removeLocationListener(mPOIViewModel);
		GlobalARData.removeLocationListener(this);
	}

	@Override
	public void onLocationChanged(Location location)
	{
		StringBuffer sBuffer = new StringBuffer();

		List<Marker> markersList = GlobalARData.getMarkers();
		if(null == markersList)
		{
			Log.i(AppConstants.LOG_TAG, "markerList is null");
		}
		if (null != markersList)
		{
			Log.i(AppConstants.LOG_TAG, "markerList count: " + markersList.size());
			int i = 1;
			for (Marker marker : markersList)
			{
				sBuffer.append("The " + i + " marker");
				sBuffer.append("\nName: " + marker.getName());
				sBuffer.append("\nLocation: " + marker.getLocation());

			}
		}
		
		mTextView.setText(sBuffer.toString());

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider)
	{
		// TODO Auto-generated method stub

	}

}
