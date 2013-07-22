package com.mengdd.arapp;

import com.mengdd.camera.TestCameraActivity;
import com.mengdd.location.baidu.TestBaiduLocationActivity;
import com.mengdd.location.google.TestGoogleLocationActivity;
import com.mengdd.map.baidu.TestBaiduMapActivity;
import com.mengdd.map.google.TestGoogleMapActivity;
import com.mengdd.poi.data.TestPOIDataActivity;
import com.mengdd.poi.ui.TestRadarActivity;
import com.mengdd.sensors.TestCompassActivity;
import com.mengdd.utils.AppConstants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * This is the laucher Activity during development.
 * The list show the entry of Main Project and other tests activities.
 * 
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 * 
 */
public class TestAllActivity extends Activity
{
	private String[] functionsNames = new String[] { AppConstants.MAIN_APP,
			AppConstants.GOOGLE_MAP, AppConstants.BAIDU_MAP,
			AppConstants.LOCALICATION_GOOGLE,AppConstants.LOCALICATION_BAIDU, AppConstants.COMPASS,
			AppConstants.CAMERA, AppConstants.AR_CAMERA, AppConstants.POI_DATA };

	private ListView mListView = null;
	private ArrayAdapter<String> mAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_all);

		mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, functionsNames);

		mListView = (ListView) findViewById(R.id.mainList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(mListItemClickListener);
	}

	private OnItemClickListener mListItemClickListener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id)
		{

			final String itemString = (String) parent
					.getItemAtPosition(position);

			Intent intent = new Intent();

			if (itemString.equals(AppConstants.MAIN_APP))
			{
				intent.setClass(TestAllActivity.this, FirstActivity.class);
			}
			if (itemString.equals(AppConstants.GOOGLE_MAP))
			{
				intent.setClass(TestAllActivity.this,
						TestGoogleMapActivity.class);

			}

			 if (itemString.equals(AppConstants.BAIDU_MAP))
			 {
			 intent.setClass(TestAllActivity.this,
			 TestBaiduMapActivity.class);
			
			 }

			if (itemString.equals(AppConstants.LOCALICATION_GOOGLE))
			{
				intent.setClass(TestAllActivity.this,
						TestGoogleLocationActivity.class);

			}
			
			if (itemString.equals(AppConstants.LOCALICATION_BAIDU))
			{
				intent.setClass(TestAllActivity.this,
						TestBaiduLocationActivity.class);

			}

			if (itemString.equals(AppConstants.COMPASS))
			{
				intent.setClass(TestAllActivity.this, TestCompassActivity.class);

			}

			if (itemString.equals(AppConstants.CAMERA))
			{
				intent.setClass(TestAllActivity.this, TestCameraActivity.class);

			}
			
			 if (itemString.equals(AppConstants.AR_CAMERA))
			 {
			 intent.setClass(TestAllActivity.this,
			 TestRadarActivity.class);
			
			 }
			
			
			 if (itemString.equals(AppConstants.POI_DATA))
			 {
			 intent.setClass(TestAllActivity.this,
			 TestPOIDataActivity.class);
			
			 }

			try
			{
				TestAllActivity.this.startActivity(intent);
				Toast.makeText(TestAllActivity.this, itemString,
						Toast.LENGTH_SHORT).show();

			}
			catch (Exception e)
			{
				e.printStackTrace();
				Toast.makeText(TestAllActivity.this, R.string.no_activity,
						Toast.LENGTH_SHORT).show();
			}

		}
	};

}
