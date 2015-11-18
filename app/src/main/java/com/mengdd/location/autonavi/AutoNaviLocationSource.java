package com.mengdd.location.autonavi;

import android.app.Activity;

import com.amap.api.maps.LocationSource;

/**
 * LocationSource可以set给AMap对象，然后就不需要自己显式注册和取消定位了，改为由地图控制
 * 
 * @author Dandan Meng
 * 
 */
public class AutoNaviLocationSource extends AutoNaviLocationModel implements
        LocationSource {

    public AutoNaviLocationSource(Activity activity) {
        super(activity);
    }

    @Override
    public void activate(OnLocationChangedListener listener) {

        setOnLocationChangedListener(listener);

        registerLocationUpdates();
    }

    @Override
    public void deactivate() {

        setOnLocationChangedListener(null);
        unregisterLocationUpdates();

        onDestroy();
    }

}
