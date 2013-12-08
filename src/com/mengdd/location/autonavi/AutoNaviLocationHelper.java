package com.mengdd.location.autonavi;

import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.mengdd.map.autonavi.AMapUtil;

public class AutoNaviLocationHelper {

    /**
     * 从AMapLocation类型的对象中得到必要的信息，并组成一个字符串返回
     * 
     * @param location
     * @return
     */
    public static String getLocationString(AMapLocation location) {
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
