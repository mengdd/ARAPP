package com.mengdd.location.google;

import android.location.Location;

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
            Double geoLat = location.getLatitude();
            Double geoLng = location.getLongitude();

            str = ("定位成功:(" + geoLng + "," + geoLat + ")" + "\n精    度    :"
                    + location.getAccuracy() + "米" + "\n定位方式:"
                    + location.getProvider() + "\n定位时间:" + location.getTime());
        }

        return str;
    }
}
