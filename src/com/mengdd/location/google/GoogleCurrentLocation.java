package com.mengdd.location.google;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;
import com.mengdd.utils.AppConstants;
import com.mengdd.utils.DialogUtils;
import com.mengdd.utils.DialogUtils.OnShowLocationSetttingsListener;

/**
 * Location ViewModel for finding current location information.
 * 
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 * 
 */
public class GoogleCurrentLocation extends ViewModel {
    private LocationManager mLocationManager;
    private List<String> allProviders;

    private LocationProvider mCurrentLocationProvider = null;
    private Location mCurrentLocation = null;

    public Location getCurrentLocaitonLocation() {
        return mCurrentLocation;
    }

    private TextView latitudeValue;
    private TextView longitudeValue;
    private TextView providerValue;
    private TextView accuracyValue;
    private TextView timeToFixValue;

    // the location provider Spinner
    private Spinner mProviderSpinner;

    private View mRootView;

    private long mLastFixTime = 0;
    private long mCurrentFixTime = 0;

    // UI
    private TextView allProvidersValueTextView;

    public GoogleCurrentLocation(Activity activity) {
        super(activity);
    }

    @Override
    public View getView() {
        return mRootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationManager = (LocationManager) mActivity
                .getSystemService(Context.LOCATION_SERVICE);

        mRootView = mInflater.inflate(R.layout.cur_location_view_model, null);

        allProvidersValueTextView = (TextView) mRootView
                .findViewById(R.id.allProvidersValue);

        latitudeValue = (TextView) mRootView.findViewById(R.id.latitudeValue);
        longitudeValue = (TextView) mRootView.findViewById(R.id.longitudeValue);
        providerValue = (TextView) mRootView.findViewById(R.id.providerValue);
        accuracyValue = (TextView) mRootView.findViewById(R.id.accuracyValue);
        timeToFixValue = (TextView) mRootView.findViewById(R.id.timeToFixValue);

        allProviders = findAllProviders();

        setUpProviderSpinner();

        Button settingBtn = (Button) mRootView.findViewById(R.id.settingsBtn);
        settingBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                enableLocationSettings();
            }
        });

    }

    private void setUpProviderSpinner() {
        mProviderSpinner = (Spinner) mRootView
                .findViewById(R.id.providerSpinner);
        // Create an ArrayAdapter using the string array and a default spinner
        // layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                mActivity, R.array.location_providers,
                android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mProviderSpinner.setAdapter(adapter);

        mProviderSpinner
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                            View view, int position, long id) {
                        Toast.makeText(
                                mActivity,
                                "position: " + position + ", "
                                        + parent.getSelectedItem(),
                                Toast.LENGTH_LONG).show();
                        switch (position) {
                        case 0:

                            mCurrentLocationProvider = openProvider(
                                    mLocationManager,
                                    LocationManager.GPS_PROVIDER);

                            break;
                        case 1:

                            mCurrentLocationProvider = openProvider(
                                    mLocationManager,
                                    LocationManager.NETWORK_PROVIDER);

                            break;
                        case 2:

                            mCurrentLocationProvider = openProvider(
                                    mLocationManager,
                                    LocationManager.PASSIVE_PROVIDER);

                            break;

                        case 3:

                            mCurrentLocationProvider = findBestProvider(mLocationManager);

                            break;

                        default:
                            break;
                        }

                        // requestion location updates
                        registerNewLocationUpdates(mLocationManager,
                                mCurrentLocationProvider, true);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
    }

    public void requestNewLocationUpdates() {
        // requestion location updates
        registerNewLocationUpdates(mLocationManager, mCurrentLocationProvider,
                true);
    }

    @Override
    public void onResume(Intent intent) {
        super.onResume(intent);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != mLocationManager) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            Log.i(AppConstants.LOG_TAG, "onLocationChanged: " + location);

            mCurrentFixTime = SystemClock.uptimeMillis();

            mCurrentLocation = location;

            latitudeValue.setText(String.valueOf(location.getLatitude()));
            longitudeValue.setText(String.valueOf(location.getLongitude()));
            providerValue.setText(String.valueOf(location.getProvider()));
            accuracyValue.setText(String.valueOf(location.getAccuracy()));

            timeToFixValue.setText(String
                    .valueOf((mCurrentFixTime - mLastFixTime) / 1000));

            mLastFixTime = mCurrentFixTime;

            mRootView.findViewById(R.id.timeToFixUnits).setVisibility(
                    View.VISIBLE);
            mRootView.findViewById(R.id.accuracyUnits).setVisibility(
                    View.VISIBLE);

        }
    };

    /**
     * Get All locaiton Providers
     * 
     * @return A List contains all location providers' name
     */
    private List<String> findAllProviders() {
        List<String> providersList = null;
        if (null != mLocationManager) {
            // Criteria criteria = new Criteria();
            // criteria.setAccuracy(Criteria.ACCURACY_LOW);

            // providersList = locationManager.getProviders(criteria, true);

            providersList = mLocationManager.getAllProviders();

            StringBuffer stringBuffer = new StringBuffer();
            if (providersList.isEmpty()) {
                Toast.makeText(mActivity, "no providers available!",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                for (String enabledProvider : providersList) {
                    stringBuffer.append(enabledProvider).append(",");
                }

                // remove the last comma
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);

                if (null != allProvidersValueTextView) {

                    allProvidersValueTextView.setText(stringBuffer);
                }
            }
        }

        return providersList;
    }

    /**
     * Try to find the best provider now can use. First, try GPS, next, network
     * provider and then passive provider.
     * 
     * @param locationManager
     * @return the best provider now can use or return null if none exists
     */
    private LocationProvider findBestProvider(LocationManager locationManager) {
        LocationProvider bestProvider = null;

        if (null != locationManager) {
            // 1.try GPS provider
            LocationProvider gpsProvider = openProvider(locationManager,
                    LocationManager.GPS_PROVIDER);

            if (null != gpsProvider
                    && locationManager
                            .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                bestProvider = gpsProvider;
            }
            else {
                // 2.try network provider
                LocationProvider networkProvider = openProvider(
                        locationManager, LocationManager.NETWORK_PROVIDER);

                if (null != networkProvider
                        && locationManager
                                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    bestProvider = networkProvider;
                }
                else {
                    // 3.try passive provider
                    LocationProvider passiveProvider = openProvider(
                            locationManager, LocationManager.PASSIVE_PROVIDER);

                    if (null != passiveProvider
                            && locationManager
                                    .isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
                        bestProvider = passiveProvider;
                    }

                }
            }

        }

        if (null != bestProvider) {
            Toast.makeText(mActivity,
                    "Location Provider: " + bestProvider.getName(),
                    Toast.LENGTH_LONG).show();
        }
        return bestProvider;
    }

    /**
     * Try to get the Location Provider by the specific name. If the provider is
     * not enabled, show a dialog to lead user to settings activity
     * 
     * @param locationManager
     * @param providerName
     * @return the provider of the specific provider name, or null if the
     *         provider doesn't exist
     */
    private LocationProvider openProvider(LocationManager locationManager,
            String providerName) {

        LocationProvider locationProvider = null;

        if (null != locationManager) {
            locationProvider = locationManager.getProvider(providerName);

            if (null != locationProvider) {// there is a provider called this
                                           // name

                boolean providerEnabled = locationManager
                        .isProviderEnabled(providerName);

                // if the provider isn't enabled, try to set it
                if (!providerEnabled) {

                    // set the location provider to null if it is not enalbed

                    // show a dialog to see if the user willing to set the
                    // location source
                    DialogUtils.showIfLocationSettingDialog(mActivity,
                            mOnShowLocationSetttingsListener, providerName);

                }

            }
        }

        return locationProvider;

    }

    private OnShowLocationSetttingsListener mOnShowLocationSetttingsListener = new OnShowLocationSetttingsListener() {

        @Override
        public void onShowLocationSettings() {
            enableLocationSettings();
        }
    };

    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        mActivity.startActivity(settingsIntent);
    }

    private void registerNewLocationUpdates(LocationManager locationManager,
            LocationProvider newLocationProvider, boolean removeOthers) {
        if (null == newLocationProvider || null == locationManager) {
            return;
        }

        if (removeOthers) {
            locationManager.removeUpdates(mLocationListener);
        }

        Log.i(AppConstants.LOG_TAG, "Location Provider in register: "
                + newLocationProvider.getName());

        locationManager.requestLocationUpdates(newLocationProvider.getName(),
                0, 0, mLocationListener);
        Log.i(AppConstants.LOG_TAG, "register New Location Updates Finished!");

        mLastFixTime = SystemClock.uptimeMillis();

    }
}
