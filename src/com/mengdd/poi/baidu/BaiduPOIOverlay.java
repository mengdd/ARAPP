package com.mengdd.poi.baidu;

import java.util.ArrayList;

import android.app.Activity;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PoiOverlay;
import com.baidu.mapapi.search.MKPoiInfo;

public class BaiduPOIOverlay extends PoiOverlay {
    private MapView mMapView = null;

    public BaiduPOIOverlay(Activity activity, MapView mapView) {
        super(activity, mapView);
        mMapView = mapView;

        mMapView.getOverlays().add(this);
    }

    @Override
    public void setData(ArrayList<MKPoiInfo> poiData) {
        super.setData(poiData);
        mMapView.refresh();
    }

    public interface PoiTapListener {
        public void onPoiTap(int index, MKPoiInfo poiInfo);
    }

    private PoiTapListener mPoiTapListener = null;

    public void setPoiTabListener(PoiTapListener listener) {
        this.mPoiTapListener = listener;
    }

    @Override
    protected boolean onTap(int index) {
        boolean ret = super.onTap(index);// super onTap里估计是调用了Toast显示poi的name

        if (null != mPoiTapListener) {
            MKPoiInfo info = getPoi(index);
            mPoiTapListener.onPoiTap(index, info);
        }
        return ret;
    }
}
