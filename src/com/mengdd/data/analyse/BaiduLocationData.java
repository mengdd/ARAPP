package com.mengdd.data.analyse;

import java.util.ArrayList;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.mengdd.location.baidu.BaiduLocationHelper;
import com.mengdd.map.baidu.BaiduMapHelper;

import android.app.Activity;
import android.util.Log;

public class BaiduLocationData {

    private static final String TAG = "Baidu Location";
    public static void computeData(Activity activity) {

        if (null == BaiduMapHelper.getMapManager()) {
            BaiduMapHelper.initBaiduMapManager(activity);
        }

        List<BDLocation> locationStandard = generateStandardLocations();
        List<BDLocation> locationMeasure = generateMeasuredLocations();

        for (int i = 0; i < locationMeasure.size(); ++i) {
            double distance = compareLocation(locationMeasure.get(i),
                    locationStandard.get(i));
            Log.i(TAG, "distance: " + "i: " + distance + ", location: "
                    + locationMeasure.get(i).toString());
        }

    }

    public static double compareLocation(BDLocation locationStart,
            BDLocation locationEnd) {

        GeoPoint startGeoPoint = BaiduLocationHelper
                .getGeoPointFromBDLocation(locationStart);
        GeoPoint endGeoPoint = BaiduLocationHelper
                .getGeoPointFromBDLocation(locationEnd);
        double distance = DistanceUtil.getDistance(startGeoPoint, endGeoPoint);

        return distance;
    }

    private static List<BDLocation> generateMeasuredLocations() {
        List<BDLocation> locations = new ArrayList<BDLocation>();

        // 数据A
        BDLocation locationA = new BDLocation();
        locationA.setLatitude(39.970143);
        locationA.setLongitude(116.3169);

        locations.add(locationA);
        // 数据B
        BDLocation locationB = new BDLocation();
        locationB.setLatitude(39.968187);
        locationB.setLongitude(116.316459);

        locations.add(locationB);
        // 数据C
        BDLocation locationC = new BDLocation();
        locationC.setLatitude(39.963982);
        locationC.setLongitude(116.321301);

        locations.add(locationC);
        // 数据D
        BDLocation locationD = new BDLocation();
        locationD.setLatitude(39.965071);
        locationD.setLongitude(116.322896);

        locations.add(locationD);
        // 数据E
        BDLocation locationE = new BDLocation();
        locationE.setLatitude(39.965489);
        locationE.setLongitude(116.328708);

        locations.add(locationE);

        return locations;
    }

    private static List<BDLocation> generateStandardLocations() {
        List<BDLocation> locations = new ArrayList<BDLocation>();

        // 数据A
        BDLocation locationA = new BDLocation();
        locationA.setLatitude(39.970143);
        locationA.setLongitude(116.3169);

        locations.add(locationA);
        // 数据B
        BDLocation locationB = new BDLocation();
        locationB.setLatitude(39.968187);
        locationB.setLongitude(116.316459);

        locations.add(locationB);
        // 数据C
        BDLocation locationC = new BDLocation();
        locationC.setLatitude(39.963982);
        locationC.setLongitude(116.321301);

        locations.add(locationC);
        // 数据D
        BDLocation locationD = new BDLocation();
        locationD.setLatitude(39.965071);
        locationD.setLongitude(116.322896);

        locations.add(locationD);
        // 数据E
        BDLocation locationE = new BDLocation();
        locationE.setLatitude(39.965489);
        locationE.setLongitude(116.328708);

        locations.add(locationE);

        return locations;
    }

}
