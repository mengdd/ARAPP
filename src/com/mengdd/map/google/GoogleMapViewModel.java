package com.mengdd.map.google;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;
import com.mengdd.map.BasicMapViewModel;

/**
 * The ViewModel to display a Google Map MapView.
 * Developer guide:
 * https://developers.google.com/maps/documentation/android/
 * 
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 * 
 */
public class GoogleMapViewModel extends BasicMapViewModel
{

	private MapView mMapView = null;

	private GoogleMap mGoogleMap = null;

	private boolean isVisible = false;

	public GoogleMapViewModel(Activity activity)
	{
		super(activity);

	}

	@Override
	public void onCreate(Intent intent)
	{
		super.onCreate(intent);

		mRootView = mInflater.inflate(R.layout.google_map_view_root, null);

	}

	private void initMapView()
	{
		mMapView = new MapView(mActivity);
		LinearLayout.LayoutParams layoutParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mMapView.setLayoutParams(layoutParams);
		
	

		mMapView.onCreate(null);

		((LinearLayout) mRootView).addView(mMapView, 0);

		mGoogleMap = mMapView.getMap();

		// Solve the problem of NullPointerException of CameraUpdateFactory
		try
		{
			MapsInitializer.initialize(mActivity);
		}
		catch (GooglePlayServicesNotAvailableException e)
		{
			e.printStackTrace();
		}

	}
	
	@Override
	public GoogleMap getMap()
	{
		if(null == mGoogleMap && null != mMapView)
		{
			mGoogleMap = mMapView.getMap();
		}
		return mGoogleMap;
	}

	@Override
	public void onDestory()
	{

		super.onDestory();
		if (isVisible)
		{
			mMapView.onDestroy();
		}
	}

	@Override
	public void onResume(Intent intent)
	{
		super.onResume(intent);
		if (isVisible)
		{
			mMapView.onResume();
		}
	}

	@Override
	public void onPause()
	{
		super.onPause();
		if (isVisible)
		{
			mMapView.onPause();
		}
	}

	/**
	 * Set the visibility of this whole ViewModel
	 * 
	 * @param visibility
	 */
	public void setVisibility(int visibility)
	{
		if (View.VISIBLE == visibility && false == isVisible)
		{

			isVisible = true;

			initMapView();
			mMapView.onResume();

		}
		else if (View.GONE == visibility && isVisible)
		{

			isVisible = false;

			mMapView.onPause();
			mMapView.onDestroy();

			((LinearLayout) mRootView).removeView(mMapView);

		}

		mRootView.setVisibility(visibility);

	}

	@Override
	public void changeMapCamera(double latitude, double longitude)
	{
		changeMapCamera(latitude, longitude, 0, 0, 12);
	}

	/**
	 * Repositions the camera according to the params.
	 * These parames are used to defined the CameraUpdate object.
	 * 
	 * @param latitude
	 * @param longitude
	 * @param bearing
	 *            orientation
	 * @param tilt
	 *            viewing angle
	 * @param zoom
	 */
	public void changeMapCamera(double latitude, double longitude,
			float bearing, float tilt, float zoom)
	{
		LatLng targetLatLng = new LatLng(latitude, longitude);

		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(targetLatLng).zoom(zoom).bearing(bearing).tilt(tilt)
				.build();

		CameraUpdate cameraUpdate = CameraUpdateFactory
				.newCameraPosition(cameraPosition);

		changeMapCamera(cameraUpdate);

	}

	/**
	 * Repositions the camera according to the instructions defined in the
	 * update.
	 * 
	 * @param cameraUpdate
	 */
	public void changeMapCamera(CameraUpdate cameraUpdate)
	{
		changeMapCamera(cameraUpdate, false, 0, null);
	}

	/**
	 * Change the Map Camera Position, the point looking at the map.
	 * 
	 * @param cameraUpdate
	 * @param isAnimated
	 *            If the camera changes with animation
	 * @param animateDurationMs
	 *            The duration of the animation in milliseconds.
	 * @param animateCallback
	 *            An optional callback to be notified from the main thread when
	 *            the animation stops.
	 */
	public void changeMapCamera(CameraUpdate cameraUpdate, boolean isAnimated,
			int animateDurationMs, CancelableCallback animateCallback)
	{

		if (isAnimated)
		{
			// references page:
			// https://developers.google.com/maps/documentation/android/reference/com/google/android/gms/maps/GoogleMap

			mGoogleMap.animateCamera(cameraUpdate, animateDurationMs,
					animateCallback);
		}
		else
		{
			mGoogleMap.moveCamera(cameraUpdate);
		}
	}

	@Override
	public void addMarker(double latitude, double longitude)
	{
		addMarker(new LatLng(latitude, longitude), "Your current Location");
	}

	public void addMarker(LatLng position, String title)
	{
		mGoogleMap.addMarker(new MarkerOptions().position(position)
				.title(title));
	}


}
