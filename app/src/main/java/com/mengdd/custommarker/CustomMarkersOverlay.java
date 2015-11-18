package com.mengdd.custommarker;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.mengdd.arapp.R;
import com.mengdd.db.CustomMarkerTable;

/**
 * Custom Markers overlay, this is an outside wrapper class. See MyOverlay for
 * more details. Here I'm considering to refract to join these two classes.
 * 
 * @author meng
 * 
 */
public class CustomMarkersOverlay {
    private MapView mMapView = null;
    private MyOverlay mMyOverlay = null;
    private Resources mResources = null;

    public CustomMarkersOverlay(Activity activity, MapView mapView) {
        mMapView = mapView;

        initOverlay(activity, mapView);
    }

    private void initOverlay(Activity activity, MapView mapView) {
        mResources = activity.getResources();
        Drawable defaultMarker = mResources.getDrawable(R.drawable.icon_marka);
        mMyOverlay = new MyOverlay(activity, defaultMarker, mapView);

        // initTest();

    }

    private void initTest() {
        /**
         * 准备overlay 数据
         */
        /**
         * overlay 位置坐标
         */
        double mLon1 = 116.400244;
        double mLat1 = 39.963175;
        double mLon2 = 116.369199;
        double mLat2 = 39.942821;
        double mLon3 = 116.425541;
        double mLat3 = 39.939723;
        double mLon4 = 116.401394;
        double mLat4 = 39.906965;

        GeoPoint p1 = new GeoPoint((int) (mLat1 * 1E6), (int) (mLon1 * 1E6));
        OverlayItem item1 = new OverlayItem(p1, "覆盖物1", "");
        /**
         * 设置overlay图标，如不设置，则使用创建ItemizedOverlay时的默认图标.
         */
        item1.setMarker(mResources.getDrawable(R.drawable.icon_marka));

        GeoPoint p2 = new GeoPoint((int) (mLat2 * 1E6), (int) (mLon2 * 1E6));
        OverlayItem item2 = new OverlayItem(p2, "覆盖物2", "");
        item2.setMarker(mResources.getDrawable(R.drawable.icon_markb));

        GeoPoint p3 = new GeoPoint((int) (mLat3 * 1E6), (int) (mLon3 * 1E6));
        OverlayItem item3 = new OverlayItem(p3, "覆盖物3", "");
        item3.setMarker(mResources.getDrawable(R.drawable.icon_markc));

        GeoPoint p4 = new GeoPoint((int) (mLat4 * 1E6), (int) (mLon4 * 1E6));
        OverlayItem item4 = new OverlayItem(p4, "覆盖物4", "");
        item4.setMarker(mResources.getDrawable(R.drawable.icon_markd));

        mMyOverlay.addItem(item1);
        mMyOverlay.addItem(item2);

        mMyOverlay.addItem(item3);
        mMyOverlay.addItem(item4);

    }

    public void initNewMarker() {
        GeoPoint geoPoint = mMapView.getMapCenter();
        String title = "New Marker";
        String snippet = "snippet";
        MarkerItem newItem = new MarkerItem(geoPoint, title, snippet,
                R.drawable.icon_gcoding, R.drawable.nav_turn_via_1);
        newItem.getDrawables(mResources);

        mMyOverlay.addNewItem(newItem);
    }

    public MarkerItem getMarkerItemInEdit() {
        MarkerItem item = null;
        item = mMyOverlay.getMovingItem();

        return item;
    }

    public boolean saveMarkerItemToDb(MarkerItem markerItem) {
        boolean success = false;

        success = mMyOverlay.saveMarkerToDb(markerItem);
        return success;

    }

    public void addDBMarkerToOverlay(MarkerItem marker) {
        marker.getDrawables(mResources);
        marker.setFixed(true);
        mMyOverlay.addNewItem(marker);

    }

    public void clearMarkers() {
        mMyOverlay.clearItems();
    }

}
