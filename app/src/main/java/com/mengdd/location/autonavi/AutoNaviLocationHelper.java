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
            StringBuffer sb = new StringBuffer();
            sb.append("SDK : ");
            sb.append("AutoNavi");
            sb.append("\ntime : ");
            sb.append(AMapUtil.convertToTime(location.getTime()));
            sb.append("\nprovider : ");
            sb.append(location.getProvider());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\naccuracy : ");
            sb.append(location.getAccuracy() + " 米");

            String cityCode = "";
            String desc = "";
            Bundle locBundle = location.getExtras();
            if (locBundle != null) {
                cityCode = locBundle.getString("citycode");
                desc = locBundle.getString("desc");
            }
            sb.append("\n城市编码  : ");
            sb.append(cityCode);
            sb.append("\n位置描述 : ");
            sb.append(desc);
            sb.append("\n省 : ");
            sb.append(location.getProvince());
            sb.append("\n市 : ");
            sb.append(location.getCity());
            sb.append("\n区(县) : ");
            sb.append(location.getDistrict());
            sb.append("\n城市编码 : ");
            sb.append(location.getCityCode());
            sb.append("\n区域编码 : ");
            sb.append(location.getAdCode());

            str = sb.toString();
        }

        return str;
    }
}
