package com.mengdd.poi.baidu;

import java.util.ArrayList;

import android.app.Activity;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PoiOverlay;
import com.baidu.mapapi.search.MKPoiInfo;

public class BaiduPOIOverlay
{
	private MapView mMapView = null;
	private PoiOverlay mPoiOverlay = null;
	
	public BaiduPOIOverlay(Activity activity, MapView mapView)
	{
		mMapView = mapView;
		
		initOverlay(activity,mapView);
	}
	
	private void initOverlay(Activity activity, MapView mapView)
	{
		mPoiOverlay = new PoiOverlay(activity,mapView);
		
		mMapView.getOverlays().add(mPoiOverlay);
	}
	
	public void setData(ArrayList<MKPoiInfo> poiData)
	{
		mPoiOverlay.setData(poiData);
		
		mMapView.refresh();
		
	}

}
