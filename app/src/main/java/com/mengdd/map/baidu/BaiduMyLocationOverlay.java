package com.mengdd.map.baidu;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class BaiduMyLocationOverlay {
    private MapView mMapView = null;
    private LocationData mLocationData = null;
    private MyLocationOverlay mLocationOverlay = null;
    private MapController mMapController = null;

    private boolean isZoomEnabled = false;

    public boolean isZoomEnabled() {
        return isZoomEnabled;
    }

    public void setZoomEnabled(boolean isZoomEnabled) {
        this.isZoomEnabled = isZoomEnabled;
    }

    private boolean isGoToEnabled = false;

    public boolean isGoToEnabled() {
        return isGoToEnabled;
    }

    public void setGoToEnabled(boolean isGoToEnabled) {
        this.isGoToEnabled = isGoToEnabled;
    }

    public BaiduMyLocationOverlay(MapView mapView) {
        mMapView = mapView;

        initOverlay();

    }

    private void initOverlay() {
        mLocationOverlay = new MyLocationOverlay(mMapView);

        mLocationData = new LocationData();
        mLocationOverlay.setData(mLocationData);
        mMapView.getOverlays().add(mLocationOverlay);
        mLocationOverlay.enableCompass();
        // mMapView.refresh();

        mMapController = mMapView.getController();

    }

    public void setLocationData(BDLocation bdLocation) {
        mLocationData.accuracy = bdLocation.getRadius();
        mLocationData.direction = bdLocation.getDerect();
        mLocationData.latitude = bdLocation.getLatitude();
        mLocationData.longitude = bdLocation.getLongitude();

        mLocationOverlay.setData(mLocationData);

        if (isGoToEnabled) {
            GeoPoint geoPoint = new GeoPoint(
                    (int) (mLocationData.latitude * 1e6),
                    (int) (mLocationData.longitude * 1e6));

            mMapController.animateTo(geoPoint);
            mMapController.setCenter(geoPoint);
        }

        if (isZoomEnabled) {
            int maxZoomLevel = mMapView.getMaxZoomLevel();
            float currentZoomLevel = mMapView.getZoomLevel();
            if (currentZoomLevel < maxZoomLevel) {
                mMapController.zoomIn();
            }
        }

        mMapView.refresh();
    }

    public void goToFixPlaceForTest(LocationData locationData) {
        locationData.longitude = 116.317169;
        locationData.latitude = 39.968176;

        locationData.direction = 2.0f;
        locationData.accuracy = 300;

        mLocationOverlay.setData(locationData);

        mMapView.refresh();
        mMapView.getController().animateTo(
                new GeoPoint((int) (locationData.latitude * 1e6),
                        (int) (locationData.longitude * 1e6)));
    }

}
