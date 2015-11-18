package com.mengdd.location.baidu;

import android.location.Location;
import android.location.LocationManager;

import com.baidu.location.BDLocation;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class BaiduLocationHelper {
    /**
     * Convert the Baidu Location Object to a Android Location object.
     * 
     * @param bdLocation
     *            Baidu API Location, ie BDLocation
     * @return Android API Location
     */
    public static Location convertBD2AndroidLocation(BDLocation bdLocation) {
        if (null == bdLocation) {
            throw new IllegalArgumentException("null == bdLocation!");
        }
        Location androidLocation = new Location("network");

        if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
            androidLocation.setProvider(LocationManager.GPS_PROVIDER);
        }
        else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
            androidLocation.setProvider(LocationManager.NETWORK_PROVIDER);
        }
        else {
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
     * 
     * @param androidLocation
     * @return Baidu Location object
     */
    public static BDLocation convertAndroid2BDLocation(Location androidLocation) {
        if (null == androidLocation) {
            throw new IllegalArgumentException("null == androidLocation!");
        }

        BDLocation bdLocation = new BDLocation();

        bdLocation.setLatitude(androidLocation.getLatitude());
        bdLocation.setLongitude(androidLocation.getLongitude());
        bdLocation.setAltitude(androidLocation.getAltitude());
        bdLocation.setRadius(androidLocation.getAccuracy());
        bdLocation.setSpeed(androidLocation.getSpeed());
        String provider = androidLocation.getProvider();

        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            bdLocation.setLocType(BDLocation.TypeGpsLocation);
        }
        else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
            bdLocation.setLocType(BDLocation.TypeNetWorkLocation);
        }
        else {
            bdLocation.setLocType(-1);
        }

        return bdLocation;
    }

    public static GeoPoint getGeoPointFromBDLocation(BDLocation bdLocation) {
        if (null == bdLocation) {
            throw new IllegalArgumentException("bdLocation is null!");
        }
        GeoPoint geoPoint = null;
        geoPoint = new GeoPoint((int) (bdLocation.getLatitude() * 1e6),
                (int) (bdLocation.getLongitude() * 1e6));

        return geoPoint;

    }

    public static String getLocationString(BDLocation location) {
        String str = null;
        if (null != location) {

            StringBuffer sb = new StringBuffer(256);
            sb.append("SDK : ");
            sb.append("Baidu");
            sb.append("\ntime : ");
            sb.append(location.getTime());
            sb.append("\nprovider : ");
            sb.append(analyseLocType(location.getLocType()));
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius() + " 米");
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
            str = sb.toString();
        }
        return str;
    }

    public static String analyseLocType(int locType) {
        // 61 ： GPS定位结果
        // 62 ： 扫描整合定位依据失败。此时定位结果无效。
        // 63 ： 网络异常，没有成功向服务器发起请求。此时定位结果无效。
        // 65 ： 定位缓存的结果。
        // 66 ： 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果
        // 67 ： 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果
        // 68 ： 网络连接失败时，查找本地离线定位时对应的返回结果
        // 161： 表示网络定位结果
        // 162~167： 服务端定位失败。
        String result = null;
        switch (locType) {
        case 61:
            result = "GPS";
            break;
        case 65:
            result = "Cache";
            break;
        case 66:
            result = "Offline";
            break;
        case 161:
            result = "Network";
            break;

        default:
            result = "Failed!";
            break;
        }

        return result;
    }
}
