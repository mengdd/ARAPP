package com.mengdd.data.analyse;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import android.util.Log;

public class GoogleLocationData {

    private static final String TAG = "Google Location";

    public static void computeData() {

        List<Location> locationStandard = generateStandardLocations();
        List<Location> locationMeasure = generateMeasuredLocations();

        for (int i = 0; i < locationMeasure.size(); ++i) {
            double distance = compareLocation(locationMeasure.get(i),
                    locationStandard.get(i));
            Log.i(TAG, "distance: " + "i: " + distance + ", location: "
                    + locationMeasure.get(i).toString());
        }
    }

    public static float compareLocation(Location locationStart,
            Location locationEnd) {
        float results[] = new float[1];
        Location.distanceBetween(locationStart.getLatitude(),
                locationStart.getLongitude(), locationEnd.getLatitude(),
                locationEnd.getLongitude(), results);

        return results[0];
    }

    private static List<Location> generateMeasuredLocations() {
        List<Location> locations = new ArrayList<Location>();

        // 数据A
        Location locationA = new Location("gps");
        locationA.setLatitude(39.96421);
        locationA.setLongitude(116.31033);

        locations.add(locationA);
        // 数据B
        Location locationB = new Location("gps");
        locationB.setLatitude(39.96229);
        locationB.setLongitude(116.30997);

        locations.add(locationB);
        // 数据C
        Location locationC = new Location("gps");
        locationC.setLatitude(39.95809);
        locationC.setLongitude(116.31477);

        locations.add(locationC);
        // 数据D
        Location locationD = new Location("gps");
        locationD.setLatitude(39.95929);
        locationD.setLongitude(116.31634);

        locations.add(locationD);
        // 数据E
        Location locationE = new Location("gps");
        locationE.setLatitude(39.95971);
        locationE.setLongitude(116.32208);

        locations.add(locationE);

        return locations;
    }

    private static List<Location> generateStandardLocations() {
        List<Location> locations = new ArrayList<Location>();

        // 数据A
        Location locationA = new Location("gps");
        locationA.setLatitude(39.96421);
        locationA.setLongitude(116.31033);

        locations.add(locationA);
        // 数据B
        Location locationB = new Location("gps");
        locationB.setLatitude(39.96229);
        locationB.setLongitude(116.30997);

        locations.add(locationB);
        // 数据C
        Location locationC = new Location("gps");
        locationC.setLatitude(39.95809);
        locationC.setLongitude(116.31477);

        locations.add(locationC);
        // 数据D
        Location locationD = new Location("gps");
        locationD.setLatitude(39.95929);
        locationD.setLongitude(116.31634);

        locations.add(locationD);
        // 数据E
        Location locationE = new Location("gps");
        locationE.setLatitude(39.95971);
        locationE.setLongitude(116.32208);
        locations.add(locationE);

        return locations;
    }

}
