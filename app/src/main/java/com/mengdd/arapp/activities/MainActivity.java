package com.mengdd.arapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.mengdd.arapp.FrameHeaderViewModel;
import com.mengdd.arapp.FrameHeaderViewModel.OnSettingListener;
import com.mengdd.arapp.R;
import com.mengdd.data.analyse.AutoNaviLocationData;
import com.mengdd.data.analyse.BaiduLocationData;
import com.mengdd.data.analyse.GoogleLocationData;
import com.mengdd.db.CustomMarkerTable;
import com.mengdd.db.DatabaseManager;
import com.mengdd.location.baidu.BaiduLocationModel;
import com.mengdd.sina.weibo.data.WeiboAppConfig;
import com.mengdd.tests.TestAugmentedPOIActivity;
import com.mengdd.tests.TestAutoMapWithLocationModel;
import com.mengdd.tests.TestAutoNaviMapActivity;
import com.mengdd.tests.TestBaiduMapWithLocationModel;
import com.mengdd.tests.TestCameraActivity;
import com.mengdd.tests.TestCompassActivity;
import com.mengdd.tests.TestGoogleMapWithLocationModel;
import com.mengdd.tests.TestMin3dActivity;
import com.mengdd.tests.TestNaviUIActivity;
import com.mengdd.tests.TestSsoAuthorActivity;
import com.mengdd.tests.TestUserAuthorizeActivity;
import com.mengdd.utils.AppConstants;

public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout = null;
    private FrameHeaderViewModel mHeaderViewModel = null;
    private ListView mDrawerMenuList = null;
    private static Sample[] mSamples;
    private Resources resources = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        initDatabase();
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
        mHeaderViewModel.setOnSettingListener(new OnSettingListener() {
            @Override
            public void onSetting() {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
        // bottom
        initDrawerList();

        initSNSData();

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                doDistanceAnalyze();
            }
        });

    }

    private void initDrawerList() {
        mDrawerMenuList = (ListView) findViewById(R.id.drawer_list_right);

        // Instantiate the list of samples.
        mSamples = new Sample[] {
                // new Sample(R.string.login, LoginActivity.class),
                new Sample(R.string.search, SearchActivity.class),
                new Sample(R.string.custom_marker_title,
                        CustomMarkerActivity.class),
                // new Sample(R.string.google_map, GoogleMapActivity.class),
                // new Sample(R.string.baidu_map, BaiduMapActivity.class),
                // new Sample(R.string.search_navi, TestNaviUIActivity.class),
                new Sample(R.string.test_realscene, RealSceneActivity.class),
                new Sample(R.string.test_compass, TestCompassActivity.class),
//                new Sample(R.string.test_markers,
//                        TestAugmentedPOIActivity.class),
                        new Sample(R.string.test_markers,
                                TestMin3dActivity.class),
                // new Sample(R.string.test_login,
                // TestUserAuthorizeActivity.class),
                // new Sample(R.string.test_login_sso,
                // TestSsoAuthorActivity.class),
                // new Sample(R.string.test_autonavi_map,
                // TestAutoNaviMapActivity.class),
                new Sample(R.string.compare_map, MapCompareActivity.class),
                // new Sample(R.string.baidumap_title,
                // TestBaiduMapWithLocationModel.class),
                // new Sample(R.string.googlemap_title,
                // TestGoogleMapWithLocationModel.class),
                // new Sample(R.string.autonavimap_title,
                // TestAutoMapWithLocationModel.class),
                new Sample(R.string.test_camera, TestCameraActivity.class)

        };

        mDrawerMenuList.setAdapter(new ArrayAdapter<Sample>(this,
                R.layout.drawer_item, R.id.text, mSamples));

        mDrawerMenuList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                Log.i(AppConstants.LOG_TAG, "onItemClick: " + view
                        + ",position: " + position + ",id: " + id);

                // Launch the sample associated with this list position.
                startActivity(new Intent(MainActivity.this,
                        mSamples[position].activityClass));
            }
        });

    }

    // 私有类，List中的每一个例子
    private class Sample {
        private final CharSequence title;
        private final Class<? extends Activity> activityClass;

        public Sample(int titleResId, Class<? extends Activity> activityClass) {
            this.activityClass = activityClass;
            this.title = getResources().getString(titleResId);
        }

        @Override
        public String toString() {
            return title.toString();
        }
    }

    private void initDatabase() {
        // init the database
        DatabaseManager.initInstance(this);
        if (!CustomMarkerTable.isTableExist()) {
            CustomMarkerTable.createTable();
        }

    }

    private void initSNSData() {
        if (!WeiboAppConfig.isInited) {
            WeiboAppConfig.loadConfig(this);
        }
    }

    private void doDistanceAnalyze() {
        BaiduLocationData.computeData(this);
        GoogleLocationData.computeData();
        AutoNaviLocationData.computeData();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
