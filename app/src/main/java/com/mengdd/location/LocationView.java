package com.mengdd.location;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;
import com.mengdd.location.LocationModel.BasicLocationChangedListener;

public class LocationView extends ViewModel implements LocationListener,
        BasicLocationChangedListener {

    private View mRootView = null;

    private TextView mLatitude = null;
    private TextView mLongitude = null;
    private TextView mProvider = null;
    private TextView mAccuracy = null;

    public LocationView(Activity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRootView = mInflater.inflate(R.layout.location_view, null);

        mLatitude = (TextView) mRootView.findViewById(R.id.latitudeValue);
        mLongitude = (TextView) mRootView.findViewById(R.id.longitudeValue);
        mProvider = (TextView) mRootView.findViewById(R.id.providerValue);
        mAccuracy = (TextView) mRootView.findViewById(R.id.accuracyValue);

    }

    @Override
    public View getView() {
        return mRootView;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    // 实现的两个接口中的抽象方法是同一个，因为签名完全一致
    @Override
    public void onLocationChanged(Location location) {
        setLocationInfo(location);

    }

    public void clearLocationInfo() {
        mLongitude.setText("");
        mLatitude.setText("");
        mProvider.setText("");
        mAccuracy.setText("");
    }

    public void setLocationInfo(Location location) {
        if (null != location) {
            mLongitude.setText(String.valueOf(location.getLongitude()));
            mLatitude.setText(String.valueOf(location.getLatitude()));
            mProvider.setText(location.getProvider());
            mAccuracy.setText(String.valueOf(location.getAccuracy()));
        }

    }

}