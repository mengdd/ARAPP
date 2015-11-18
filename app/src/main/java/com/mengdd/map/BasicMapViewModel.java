package com.mengdd.map;

import android.app.Activity;
import android.view.View;

import com.mengdd.components.ViewModel;

/**
 * Basic MapViewModel class,provider uniform interfaces for MapViewModel. Its
 * child classes include BaiduMapViewModel and GoogleMapViewModel.
 * 
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 * 
 */
public abstract class BasicMapViewModel extends ViewModel {
    protected View mRootView = null;

    protected BasicMapViewModel(Activity activity) {
        super(activity);
    }

    @Override
    public final View getView() {
        return mRootView;
    }

    public abstract Object getMap();

    public abstract void changeMapCamera(double latitude, double longitude);

    public abstract void addMarker(double latitude, double longitude);

}
