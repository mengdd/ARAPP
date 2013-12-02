package com.mengdd.poi.ui;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.mengdd.custommarker.MarkerItem;
import com.mengdd.location.baidu.BaiduLocationHelper;
import com.mengdd.utils.MathUtils;

import android.graphics.Bitmap;
import android.location.Location;

public class BaiduMarker extends BasicMarker {
    // 百度的POI信息：
    // MKPoiInfo字段：
    // String name poi名称
    // String uid poi id
    // String address poi地址信息
    // String city poi所在城市
    // String phoneNum poi电话信息
    // String postCode poi邮编
    // int ePoiType poi类型，0：普通点，1：公交站，2：公交线路，3：地铁站，4：地铁线路
    // GeoPoint pt poi坐标, 当ePoiType为2或4时，pt 为空
    // boolean hasCaterDetails poi点是否有美食类详情页面

    private GeoPoint mGeoPoint = null;
    private MKPoiInfo mPoiInfo = null;

    private MarkerItem mMarkerItem = null;

    public BaiduMarker(String name, int color, Bitmap icon, MKPoiInfo poiInfo) {
        super();
        mMarkerType = MarkerType.BaiduMarker;
        set(name, color, icon, poiInfo);
    }

    public BaiduMarker(String name, int color, Bitmap icon,
            MarkerItem markerItem) {
        super();
        mMarkerType = MarkerType.BaiduMarker;

        set(name, color, icon, markerItem);

    }

    public synchronized void set(String name, int color, Bitmap icon,
            MKPoiInfo poiInfo) {
        super.set(name, color, icon);

        this.mGeoPoint = poiInfo.pt;
        this.mPoiInfo = poiInfo;
        this.noAltitude = true;

    }

    public synchronized void set(String name, int color, Bitmap icon,
            MarkerItem markerItem) {
        super.set(name, color, icon);

        this.mGeoPoint = markerItem.getPosition();
        this.mMarkerItem = markerItem;
        this.noAltitude = true;

    }

    @Override
    protected void calcRelativePosition(Location origLocation,
            BDLocation origBDLocation) {
        if (null == origBDLocation) {
            throw new IllegalArgumentException("origBDLocation is null!");
        }

        GeoPoint mOrigGeoPoint = BaiduLocationHelper
                .getGeoPointFromBDLocation(origBDLocation);
        // Compute the relative position vector from user position to POI
        // location
        mLocationVector = MathUtils.convGeoPointToVector(mOrigGeoPoint,
                mGeoPoint);

        this.initialY = mLocationVector.getY();

    }

    @Override
    protected void updateDistance(Location origLocation,
            BDLocation origBDLocation) {
        if (null == origBDLocation) {
            throw new IllegalArgumentException("origBDLocation is null!");
        }
        GeoPoint mOrigGeoPoint = BaiduLocationHelper
                .getGeoPointFromBDLocation(origBDLocation);
        mDistance = DistanceUtil.getDistance(mOrigGeoPoint, mGeoPoint);

    }

}
