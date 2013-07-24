package com.mengdd.location.baidu;

import android.location.Location;
import android.location.LocationManager;

import com.baidu.location.BDLocation;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class BaiduLocationHelper
{
	/**
	 * Convert the Baidu Location Object to a Android Location object.
	 * 
	 * @param bdLocation
	 *            Baidu API Location, ie BDLocation
	 * @return Android API Location
	 */
	public static Location convertBD2AndroidLocation(BDLocation bdLocation)
	{
		if (null == bdLocation)
		{
			throw new IllegalArgumentException("null == bdLocation!");
		}
		Location androidLocation = new Location("network");

		if (bdLocation.getLocType() == BDLocation.TypeGpsLocation)
		{
			androidLocation.setProvider(LocationManager.GPS_PROVIDER);
		}
		else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation)
		{
			androidLocation.setProvider(LocationManager.NETWORK_PROVIDER);
		}
		else
		{
			androidLocation.setProvider("unknown type");
		}

		androidLocation.setLatitude(bdLocation.getLatitude());
		androidLocation.setLongitude(bdLocation.getLongitude());
		androidLocation.setAltitude(bdLocation.getAltitude());
		androidLocation.setAccuracy(bdLocation.getRadius());
		androidLocation.setSpeed(bdLocation.getSpeed());

		return androidLocation;
	}

	/**
	 * Convert Baidu Locaiton to Android Location
	 * @param androidLocation
	 * @return Baidu Location object
	 */
	public static BDLocation convertAndroid2BDLocation(Location androidLocation)
	{
		if (null == androidLocation)
		{
			throw new IllegalArgumentException("null == androidLocation!");
		}

		BDLocation bdLocation = new BDLocation();

		bdLocation.setLatitude(androidLocation.getLatitude());
		bdLocation.setLongitude(androidLocation.getLongitude());
		bdLocation.setAltitude(androidLocation.getAltitude());
		bdLocation.setRadius(androidLocation.getAccuracy());
		bdLocation.setSpeed(androidLocation.getSpeed());
		String provider = androidLocation.getProvider();

		if (provider.equals(LocationManager.GPS_PROVIDER))
		{
			bdLocation.setLocType(BDLocation.TypeGpsLocation);
		}
		else if (provider.equals(LocationManager.NETWORK_PROVIDER))
		{
			bdLocation.setLocType(BDLocation.TypeNetWorkLocation);
		}
		else
		{
			bdLocation.setLocType(-1);
		}

		return bdLocation;
	}
	
	
	public static GeoPoint getGeoPointFromBDLocation(BDLocation bdLocation)
	{
		if(null == bdLocation)
		{
			throw new IllegalArgumentException("bdLocation is null!");
		}
		GeoPoint geoPoint = null;
		geoPoint = new GeoPoint(
				(int) (bdLocation.getLatitude() * 1e6),
				(int) (bdLocation.getLongitude() * 1e6));
		
		return geoPoint;
		
	}

}
