package com.mengdd.location.autonavi;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.LocationSource.OnLocationChangedListener;
import com.mengdd.arapp.GlobalARData;
import com.mengdd.location.LocationModel;
import com.mengdd.map.autonavi.AMapUtil;
import com.mengdd.utils.LogUtils;

/**
 * Location Model using AutoNavi location SDK
 * 
 * Introduction: http://code.autonavi.com/location/index
 * 
 * Reference : http://code.autonavi.com/Public/reference/LocSDK/
 * 
 * @author Dandan Meng
 * 
 */
public class AutoNaviLocationModel extends LocationModel implements
        AMapLocationListener {

    private final String LOG_TAG = "AutoNavi";
    private LocationManagerProxy mAMapLocationManager;

    public AutoNaviLocationModel(Activity activity) {
        super(activity);
        init();
    }

    private void init() {
        mAMapLocationManager = LocationManagerProxy.getInstance(mActivity);
    }

    private int mLocationMode = 2;

    /**
     * 设定定位模式
     * 
     * @param provider
     *            0代表高德LBS网络定位， 1代表GPS定位， 2代表混合定位
     */
    public void setLocationProvider(int provider) {

        mLocationMode = provider;
    }

    @Override
    public void registerLocationUpdates() {
        LogUtils.i(LOG_TAG, "register");
        /*
         * mAMapLocManager.setGpsEnable(false);//
         * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
         * API定位采用GPS和网络混合定位方式
         * ，第一个参数是定位provider，第二个参数时间最短是5000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
         */

        String provider = null;
        switch (mLocationMode) {
        case 0:
            provider = LocationProviderProxy.AMapNetwork;
            mAMapLocationManager.setGpsEnable(false);// 此值默认是true

            break;
        case 1:
            provider = LocationManagerProxy.GPS_PROVIDER;
            break;
        case 2:
            provider = LocationProviderProxy.AMapNetwork;
            mAMapLocationManager.setGpsEnable(true);
            break;

        default:
            break;
        }
        mAMapLocationManager.requestLocationUpdates(provider, 500, 0, this);

    }

    @Override
    public void unregisterLocationUpdates() {
        LogUtils.i(LOG_TAG, "unregister");
        if (null != mAMapLocationManager) {
            mAMapLocationManager.removeUpdates(this);
        }

    }

    @Override
    public void onDestroy() {

        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destory();
        }
        mAMapLocationManager = null;
        super.onDestroy();
    }

    private AMapLocationListener mOnLocationChangedListener = null;

    public void setAMapOnLocationChangedListener(AMapLocationListener listener) {
        mOnLocationChangedListener = listener;
    }

    private OnLocationChangedListener mOnLocationChangedListener2 = null;

    public void setOnLocationChangedListener(OnLocationChangedListener listener) {
        mOnLocationChangedListener2 = listener;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /**
     * 此方法已经废弃
     */
    @Override
    public void onLocationChanged(Location location) {
        LogUtils.i(LOG_TAG, "onLocationChanged 1");

    }

    @Override
    public void onLocationChanged(AMapLocation location) {
        LogUtils.i(LOG_TAG, "onLocationChanged 2");
        if (location != null) {
            Double geoLat = location.getLatitude();
            Double geoLng = location.getLongitude();
            String str = ("定位成功:(" + geoLng + "," + geoLat + ")"
                    + "\n精    度    :" + location.getAccuracy() + "米"
                    + "\n定位方式:" + location.getProvider() + "\n定位时间:" + AMapUtil
                    .convertToTime(location.getTime()));
            LogUtils.i(LOG_TAG, str);

            // 存储全局数据
            GlobalARData.setCurrentAutoNaviLocation(location);

            // 外部的listener
            if (null != mOnLocationChangedListener) {
                mOnLocationChangedListener.onLocationChanged(location);
            }

            // 有两种类型的Listener（我也觉得混乱）这种类型是LocationSource的回调传入的
            if (null != mOnLocationChangedListener2) {
                mOnLocationChangedListener2.onLocationChanged(location);
            }

            // 基类的Listener
            if (null != mBasicLocationChangedListener) {
                mBasicLocationChangedListener.onLocationChanged(location);
            }

        }
    }

    public String getLocationString(AMapLocation location) {
        String str = null;
        if (null != location) {
            Double geoLat = location.getLatitude();
            Double geoLng = location.getLongitude();
            String cityCode = "";
            String desc = "";
            Bundle locBundle = location.getExtras();
            if (locBundle != null) {
                cityCode = locBundle.getString("citycode");
                desc = locBundle.getString("desc");
            }
            str = ("定位成功:(" + geoLng + "," + geoLat + ")" + "\n精    度    :"
                    + location.getAccuracy() + "米" + "\n定位方式:"
                    + location.getProvider() + "\n定位时间:"
                    + AMapUtil.convertToTime(location.getTime()) + "\n城市编码:"
                    + cityCode + "\n位置描述:" + desc + "\n省:"
                    + location.getProvince() + "\n市:" + location.getCity()
                    + "\n区(县):" + location.getDistrict() + "\n城市编码:"
                    + location.getCityCode() + "\n区域编码:" + location.getAdCode());
        }

        return str;
    }

}
