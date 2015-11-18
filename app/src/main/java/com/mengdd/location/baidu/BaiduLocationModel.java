package com.mengdd.location.baidu;

import android.app.Activity;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.mengdd.arapp.GlobalARData;
import com.mengdd.location.LocationModel;
import com.mengdd.map.baidu.BaiduMapHelper;
import com.mengdd.utils.AppConstants;

public class BaiduLocationModel extends LocationModel implements
        BDLocationListener {
    // 我的笔记本：E49b553f34eb77132a2ee51e656627f0
    private static String strKey = "E49b553f34eb77132a2ee51e656627f0";
    // Lab PC:
    // private static final String strKey = "B1e685d5d6e6cd3b6fb4db4a6f2116ba";

    private LocationClient mLocationClient = null;

    private BDLocation mBDLocation = null;

    public BDLocation getBDLocaiton() {
        return mBDLocation;
    }

    public BaiduLocationModel(Activity activity) {
        super(activity);
    }

    private void initLocationClient() {
        mLocationClient = new LocationClient(mActivity);

        // 定位SDK4.0加入了Key

        strKey = BaiduMapHelper.strKey;
        mLocationClient.setAK(strKey);

        mLocationClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1001);// 定位时间间隔
        option.setAddrType("all");// 返回地址信息
        option.setPriority(LocationClientOption.GpsFirst);// GPS优先
        mLocationClient.setLocOption(option);

        mLocationClient.start();

    }

    @Override
    public void registerLocationUpdates() {

        initLocationClient();
        Log.d(AppConstants.LOG_TAG, "Register Baidu Location Model");

        if (null != mLocationClient && mLocationClient.isStarted()) {
            mLocationClient.requestLocation();
        }
        else {
            Log.d(AppConstants.LOG_TAG, "locClient is null or not started");
        }

    }

    @Override
    public void unregisterLocationUpdates() {
        Log.d(AppConstants.LOG_TAG, "Unregister Baidu Location Model");

        if (null != mLocationClient) {
            mLocationClient.unRegisterLocationListener(this);
            mLocationClient.stop();
            mLocationClient = null;
        }

    }

    private BDLocationListener mBaiduLocationListener = null;

    public void setBDLocationListener(BDLocationListener listener) {
        mBaiduLocationListener = listener;

    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (null == location) {
            return;
        }

        mBDLocation = location;
        mCurrentLocation = BaiduLocationHelper
                .convertBD2AndroidLocation(location);

        // set to Global data class to keep
        // GlobalARData.setCurrentGoogleLocation(mCurrentLocation);
        GlobalARData.setCurrentBaiduLocation(mBDLocation);

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
        if (location.getLocType() == BDLocation.TypeGpsLocation) {
            sb.append("\nspeed : ");
            sb.append(location.getSpeed());
            sb.append("\nsatellite : ");
            sb.append(location.getSatelliteNumber());
        }
        else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());
        }

        Log.i(AppConstants.LOG_TAG, "Location info: " + sb.toString());
        // logMsg(sb.toString());

        // 基类的Listener
        if (null != mBasicLocationChangedListener) {
            mBasicLocationChangedListener.onLocationChanged(BaiduLocationHelper
                    .convertBD2AndroidLocation(location));
        }

        // 外部的lister
        if (null != mBaiduLocationListener) {
            mBaiduLocationListener.onReceiveLocation(location);
        }

    }

    @Override
    public void onReceivePoi(BDLocation poiLocation) {
        if (null == poiLocation) {
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
        if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
            sb.append("\naddr : ");
            sb.append(poiLocation.getAddrStr());
        }
        if (poiLocation.hasPoi()) {
            sb.append("\nPoi:");
            sb.append(poiLocation.getPoi());
        }

        else {
            sb.append("noPoi information");
        }
        // logMsg(sb.toString());

        // 外部的lister
        if (null != mBaiduLocationListener) {
            mBaiduLocationListener.onReceivePoi(poiLocation);
        }

    }

}
