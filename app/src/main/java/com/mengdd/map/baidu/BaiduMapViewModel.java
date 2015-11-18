package com.mengdd.map.baidu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.mengdd.arapp.R;
import com.mengdd.map.BasicMapViewModel;
import com.mengdd.utils.AppConstants;

/**
 * The ViewModel to display a Baidu Map MapView. Baidu Map SDK, Developer guide:
 * http://developer.baidu.com/map/sdk-android.htm
 * 
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 * 
 */
public class BaiduMapViewModel extends BasicMapViewModel {

    /**
     * MapView is the main UI for map
     */
    private MapView mMapView = null;
    /**
     * MapController control the map
     */
    private MapController mMapController = null;

    private BMapManager mBMapManager = null;

    @Override
    public MapView getMap() {
        return mMapView;
    }

    public BaiduMapViewModel(Activity activity) {
        super(activity);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // the init work should be done before setContentView

        if (null == BaiduMapHelper.getMapManager()) {
            BaiduMapHelper.initBaiduMapManager(mActivity);
        }

        mBMapManager = BaiduMapHelper.getMapManager();
        if (null == mBMapManager) {
            Log.i(AppConstants.LOG_TAG, "init failed");
            return;
        }

        mRootView = mInflater.inflate(R.layout.baidu_map_view_root, null);

        mMapView = (MapView) mRootView.findViewById(R.id.bMapView);
        mMapController = mMapView.getController();

        initMap();
        mMapView.regMapViewListener(mBMapManager, mMapListener);

    }

    @Override
    public void onResume(Intent intent) {
        super.onResume(intent);

        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        mMapView.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mMapView.destroy();
    }

    private void initMap() {
        GeoPoint centerpt = mMapView.getMapCenter();
        int maxLevel = mMapView.getMaxZoomLevel();
        float zoomlevel = mMapView.getZoomLevel();
        boolean isTraffic = mMapView.isTraffic();
        boolean isSatillite = mMapView.isSatellite();
        boolean isDoubleClick = mMapView.isDoubleClickZooming();

        mMapView.setLongClickable(true);
        mMapView.setDoubleClickZooming(true);
        mMapView.setOnTouchListener(null);
        // mMapController.setMapClickEnable(true);
        // mMapView.setSatellite(false);

        // enable the map click event
        mMapController.enableClick(true);
        mMapController.setZoom(15);
        // mMapView.setTraffic(true);
        // mMapView.setSatellite(true);

    }

    /**
     * MKMapViewListener is used to handle map events callback
     */
    private final MKMapViewListener mMapListener = new MKMapViewListener() {

        @Override
        public void onMapMoveFinish() {

        }

        @Override
        public void onClickMapPoi(MapPoi mapPoiInfo) {

            String title = "";
            if (mapPoiInfo != null) {
                title = mapPoiInfo.strText;
                Toast.makeText(mActivity, title, Toast.LENGTH_SHORT).show();
                mMapController.animateTo(mapPoiInfo.geoPt);
            }
        }

        @Override
        public void onGetCurrentMap(Bitmap b) {

            // Log.d("test", "test" + "onGetCurrentMap");
            // File file = new File("/mnt/sdcard/test.png");
            // FileOutputStream out;
            // try
            // {
            // out = new FileOutputStream(file);
            // if (b.compress(Bitmap.CompressFormat.PNG, 70, out))
            // {
            // out.flush();
            // out.close();
            // }
            // }
            // catch (FileNotFoundException e)
            // {
            // e.printStackTrace();
            // }
            // catch (IOException e)
            // {
            // e.printStackTrace();
            // }
        }

        @Override
        public void onMapAnimationFinish() {

        }

        @Override
        public void onMapLoadFinish() {

        }
    };

    public void zoomIn() {
        mMapController.zoomIn();
    }

    public void zoomOut() {
        mMapController.zoomOut();
    }

    @Override
    public void changeMapCamera(double latitude, double longitude) {
        GeoPoint geoPoint = new GeoPoint((int) (latitude * 1E6),
                (int) (longitude * 1E6));
        mMapController.setCenter(geoPoint);
        mMapController.animateTo(geoPoint);

    }

    @Override
    public void addMarker(double latitude, double longitude) {

    }
}
