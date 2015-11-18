package com.mengdd.location.google;

import android.location.Location;

import com.mengdd.map.autonavi.AMapUtil;

public class GoogleLocationHelper {

    /**
     * 定位结果分析成一个字符串输出返回
     * 
     * @param location
     * @return
     */
    public static String getLocationString(Location location) {
        String str = null;
        if (null != location) {

            StringBuffer sb = new StringBuffer(256);
            sb.append("SDK : ");
            sb.append("Google");
            sb.append("\ntime : ");

            sb.append(AMapUtil.convertToTime(location.getTime()));
            // the location.getTime() return a long, so autonavi's helper is
            // used here to convert it to a human readable time
            sb.append("\nprovider : ");
            sb.append(location.getProvider());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\naccuracy : ");
            sb.append(location.getAccuracy() + " 米");
            sb.append("\nspeed : ");
            sb.append(location.getSpeed() + "m/s");

            str = sb.toString();

        }

        return str;
    }
}
