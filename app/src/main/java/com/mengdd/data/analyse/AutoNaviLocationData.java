package com.mengdd.data.analyse;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;

import android.location.Location;
import android.util.Log;

public class AutoNaviLocationData {

    private static final String TAG = "AutoNavi Location";

    public static void computeData() {
        List<AMapLocation> locationStandard = generateStandardLocations();
        List<AMapLocation> locationMeasure = generateMeasuredLocations();

        for (int i = 0; i < locationMeasure.size(); ++i) {
            double distance = compareLocation(locationMeasure.get(i),
                    locationStandard.get(i));
            Log.i(TAG, "distance: " + "i: " + distance + ", location: "
                    + locationMeasure.get(i).toString());
        }
    }

    public static float compareLocation(AMapLocation locationStart,
            AMapLocation locationEnd) {

        float distance = 0;
        LatLng latLngStart = new LatLng(locationStart.getLatitude(),
                locationStart.getLongitude());
        LatLng latLngEnd = new LatLng(locationEnd.getLatitude(),
                locationEnd.getLongitude());
        distance = AMapUtils.calculateLineDistance(latLngStart, latLngEnd);
        return distance;
    }

    private static List<AMapLocation> generateMeasuredLocations() {
        List<AMapLocation> locations = new ArrayList<AMapLocation>();

        // 数据A
        AMapLocation locationA = new AMapLocation("gps");
        locationA.setLatitude(39.96421);
        locationA.setLongitude(116.31033);

        locations.add(locationA);
        // 数据B
        AMapLocation locationB = new AMapLocation("gps");
        locationB.setLatitude(39.96229);
        locationB.setLongitude(116.30997);

        locations.add(locationB);
        // 数据C
        AMapLocation locationC = new AMapLocation("gps");
        locationC.setLatitude(39.95809);
        locationC.setLongitude(116.31477);

        locations.add(locationC);
        // 数据D
        AMapLocation locationD = new AMapLocation("gps");
        locationD.setLatitude(39.95929);
        locationD.setLongitude(116.31634);

        locations.add(locationD);
        // 数据E
        AMapLocation locationE = new AMapLocation("gps");
        locationE.setLatitude(39.95971);
        locationE.setLongitude(116.32208);
        locations.add(locationE);

        return locations;
    }

    private static List<AMapLocation> generateStandardLocations() {
        List<AMapLocation> locations = new ArrayList<AMapLocation>();

        // 数据A
        AMapLocation locationA = new AMapLocation("gps");
        locationA.setLatitude(39.96421);
        locationA.setLongitude(116.31033);

        locations.add(locationA);
        // 数据B
        AMapLocation locationB = new AMapLocation("gps");
        locationB.setLatitude(39.96229);
        locationB.setLongitude(116.30997);

        locations.add(locationB);
        // 数据C
        AMapLocation locationC = new AMapLocation("gps");
        locationC.setLatitude(39.95809);
        locationC.setLongitude(116.31477);

        locations.add(locationC);
        // 数据D
        AMapLocation locationD = new AMapLocation("gps");
        locationD.setLatitude(39.95929);
        locationD.setLongitude(116.31634);

        locations.add(locationD);
        // 数据E
        AMapLocation locationE = new AMapLocation("gps");
        locationE.setLatitude(39.95971);
        locationE.setLongitude(116.32208);
        locations.add(locationE);

        return locations;
    }

}
