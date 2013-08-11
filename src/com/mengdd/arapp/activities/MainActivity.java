package com.mengdd.arapp.activities;

import java.util.Arrays;
import java.util.List;

import com.mengdd.arapp.FrameHeaderViewModel;
import com.mengdd.arapp.R;
import com.mengdd.arapp.FrameHeaderViewModel.OnSettingListener;
import com.mengdd.db.CustomMarkerTable;
import com.mengdd.db.DatabaseManager;
import com.mengdd.tests.TestAllActivity;
import com.mengdd.tests.TestBottomMenuActivity;
import com.mengdd.utils.AppConstants;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity
{
	private DrawerLayout mDrawerLayout = null;

	private FrameHeaderViewModel mHeaderViewModel = null;

	private ListView mDrawerMenuList = null;
	private List<String> mDrawerItems = null;

	// scene id
	private static final int SCENE_MAIN_DEFALUT = 0;
	private static final int SCENE_USER_LOGIN = 1;
	private static final int SCENE_FRIENDS = 2;
	private static final int SCENE_GOOGLE_MAP = 3;
	private static final int SCENE_BAIDU_MAP = 4;
	private static final int SCENE_SEARCH = 5;
	private static final int SCENE_WHERE = 6;
	private static final int SCENE_REAL = 7;
	private static final int SCENE_MAP_COMPARE = 8;
	private static final int SCENE_ADD_MARKER = 9;
	private static final int SCENE_SETTINGS = 10;

	private int mCurrentSceneId = SCENE_MAIN_DEFALUT;

	private Resources resources = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		// init the database
		DatabaseManager.initInstance(this);
		if (!CustomMarkerTable.isTableExist())
		{
			CustomMarkerTable.createTable();
		}

		setContentView(R.layout.main_activity);

		resources = getResources();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

		// header
		mHeaderViewModel = new FrameHeaderViewModel(this);
		mHeaderViewModel.onCreate(null);
		mHeaderViewModel.setBackVisibility(View.GONE);
		mHeaderViewModel.setTitle(resources.getString(R.string.main_title));
		ViewGroup headerGourp = (ViewGroup) findViewById(R.id.main_title);
		headerGourp.addView(mHeaderViewModel.getView(), 0);

		// header control drawer
		mHeaderViewModel.setOnSettingListener(new OnSettingListener()
		{

			@Override
			public void onSetting()
			{
				mDrawerLayout.openDrawer(Gravity.RIGHT);

			}
		});

		// bottom

		initDrawerList();

	}

	private void initDrawerList()
	{
		mDrawerMenuList = (ListView) findViewById(R.id.drawer_list);

		String[] strings = resources.getStringArray(R.array.drawer_menu_items);

		mDrawerItems = Arrays.asList(strings);

		mDrawerMenuList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_item, mDrawerItems));

		mDrawerMenuList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				mDrawerLayout.closeDrawer(Gravity.RIGHT);
				Log.i(AppConstants.LOG_TAG, "onItemClick: " + view
						+ ",position: " + position + ",id: " + id);

				changeScene(position + 1);
			}
		});

	}

	private void changeScene(int sceneId)
	{

		mCurrentSceneId = sceneId;

		Intent intent = new Intent();

		switch (sceneId)
		{
			case SCENE_MAIN_DEFALUT:
				// viewModel = new WifiShareEntryViewMode(mActivity,this);
				// ((WifiShareEntryViewMode)viewModel).setOnWifiShareStartListener(mOnWifiShareStartListener);
				// ((WifiShareEntryViewMode)viewModel).setOnBackListener(mOnBackListener);
				break;
			case SCENE_USER_LOGIN:
				intent.setClass(MainActivity.this, LoginActivity.class);
				break;
			case SCENE_FRIENDS:
				// viewModel = new WifiShareMusicListViewMode(mActivity,this);
				// ((WifiShareMusicListViewMode)viewModel).setOnCancelRequestListener(mOnCancelRequestListener);
				// ((WifiShareMusicListViewMode)viewModel).setOnBackListener(mOnBackListener);
				// ((WifiShareMusicListViewMode)viewModel).setOnSearchListener(mOnSearchListener);
				// ((WifiShareMusicListViewMode)viewModel).setOnSettingListener(mOnSettingListener);
				break;
			case SCENE_GOOGLE_MAP:
				intent.setClass(MainActivity.this, GMapActivity.class);
				break;
			case SCENE_BAIDU_MAP:
				intent.setClass(MainActivity.this, BDMapActivity.class);
				break;
			case SCENE_SEARCH:

				intent.setClass(MainActivity.this, SearchActivity.class);
				break;
			case SCENE_WHERE:
				// viewModel = new WifiShareSettingViewModel(mActivity,this);
				// ((WifiShareSettingViewModel)viewModel).setOnExitServerListener(mExitServerListener);
				// ((WifiShareSettingViewModel)viewModel).setOnBackListener(mOnBackListener);
				break;
			case SCENE_REAL:
				// viewModel = new WifiShareSearchViewModel(mActivity, this);
				// ((WifiShareSearchViewModel)viewModel).setOnBackListener(mOnBackListener);
				// ((WifiShareSearchViewModel)viewModel).setOnItemSelectListener(mOnItemSelectListener);
				break;
			case SCENE_MAP_COMPARE:
				// viewModel = new WifiShareSettingViewModel(mActivity,this);
				// ((WifiShareSettingViewModel)viewModel).setOnExitServerListener(mExitServerListener);
				// ((WifiShareSettingViewModel)viewModel).setOnBackListener(mOnBackListener);
				break;

			case SCENE_ADD_MARKER:

				intent.setClass(MainActivity.this, CustomMarkerActivity.class);
				break;

			case SCENE_SETTINGS:
				intent.setClass(MainActivity.this, TestAllActivity.class);
				break;

			default:

		}

		try
		{
			MainActivity.this.startActivity(intent);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			Toast.makeText(MainActivity.this, R.string.no_activity,
					Toast.LENGTH_SHORT).show();
		}

	}

}
