package com.mengdd.location.baidu;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.mengdd.arapp.GlobalARData;
import com.mengdd.location.LocationModel;
import com.mengdd.utils.AppConstants;

public class BaiduLocationModel extends LocationModel
{

	private LocationClient mLocationClient = null;
	private BDLocationListener mLocationListener = new MyLocationListener();
	private BDLocation mBDLocation = null;
	public BDLocation getBDLocaiton()
	{
		return mBDLocation;
	}
	

	public BaiduLocationModel(Activity activity)
	{
		super(activity);
	}

	private void initLocationClient()
	{
		mLocationClient = new LocationClient(mActivity);

		mLocationClient.registerLocationListener(mLocationListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(5000);
		mLocationClient.setLocOption(option);

		mLocationClient.start();
		

	}

	@Override
	public void registerLocationUpdates()
	{
		initLocationClient();
		if (null != mLocationClient && mLocationClient.isStarted())
		{
			mLocationClient.requestLocation();
		}
		else
		{
			Log.d(AppConstants.LOG_TAG, "locClient is null or not started");
		}

	}

	@Override
	public void unregisterLocationUpdates()
	{
		if(null != mLocationClient)
		{
			mLocationClient.unRegisterLocationListener(mLocationListener);
			mLocationClient.stop();
			mLocationClient = null;
		}

	}

	private class MyLocationListener implements BDLocationListener
	{
		@Override
		public void onReceiveLocation(BDLocation location)
		{
			if (null == location)
			{
				return;
			}

			mBDLocation = location;
			mCurrentLocation = BaiduLocationHelper.convertBD2AndroidLocation(location);
			
			//set to Global data class to keep
			GlobalARData.setCurrentGoogleLocation(mCurrentLocation);

			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation)
			{
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			}
			else if (location.getLocType() == BDLocation.TypeNetWorkLocation)
			{
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}

			Log.i(AppConstants.LOG_TAG, "Location info: " + sb.toString());
			// logMsg(sb.toString());
		}

		

		public void onReceivePoi(BDLocation poiLocation)
		{
			if (null == poiLocation)
			{
				return;
			}
			StringBuffer sb = new StringBuffer(256);
			sb.append("Poi time : ");
			sb.append(poiLocation.getTime());
			sb.append("\nerror code : ");
			sb.append(poiLocation.getLocType());
			sb.append("\nlatitude : ");
			sb.append(poiLocation.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(poiLocation.getLongitude());
			sb.append("\nradius : ");
			sb.append(poiLocation.getRadius());
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation)
			{
				sb.append("\naddr : ");
				sb.append(poiLocation.getAddrStr());
			}
			if (poiLocation.hasPoi())
			{
				sb.append("\nPoi:");
				sb.append(poiLocation.getPoi());
			}
			
			
			else
			{
				sb.append("noPoi information");
			}
			// logMsg(sb.toString());
		}
	}

}
