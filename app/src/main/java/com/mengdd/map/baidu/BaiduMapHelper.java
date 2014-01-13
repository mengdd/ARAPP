package com.mengdd.map.baidu;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.mengdd.utils.AppConstants;

/**
 * Basic Module for Baidu Map SDK, keep the API Key and init the BMapManager
 * instance.
 *
 *
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 *
 */
public class BaiduMapHelper {

	// 我的笔记本：E49b553f34eb77132a2ee51e656627f0
	public static final String strKey = "E49b553f34eb77132a2ee51e656627f0";
	// Lab PC:
	// public static final String strKey = "B1e685d5d6e6cd3b6fb4db4a6f2116ba";

	// SG PC:
	// private static final String strKey = "FDa2d5111e0a3487be2e4927075d2629";

	private static boolean mBDKeyRight = true;
	private static BMapManager mBMapManager = null;

	public static BMapManager getMapManager() {
		return mBMapManager;
	}

	public static void initBaiduMapManager(Context context) {
		Log.d(AppConstants.LOG_TAG, "initEngineManager");
		if (null == mBMapManager) {
			mBMapManager = new BMapManager(context);
		}

		if (null == mBMapManager
				|| !mBMapManager.init(strKey, new MyGeneralListener(context))) {
			Toast.makeText(context, "BMapManager Initialization failed!",
					Toast.LENGTH_LONG).show();
		}
	}

	public static void releaseMapManager() {
		if (null != mBMapManager) {
			mBMapManager.destroy();
			mBMapManager = null;
		}
	}

	/**
	 *
	 * Class for EventListener, handle the Registration Errors
	 *
	 */
	static class MyGeneralListener implements MKGeneralListener {

		private Context mContext = null;

		public MyGeneralListener(Context context) {
			mContext = context;
		}

		@Override
		public void onGetNetworkState(int iError) {
			// Return Network error
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(mContext, "Network Connection Error",
						Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(mContext, "Network Data Error",
						Toast.LENGTH_LONG).show();
			}

		}

		@Override
		public void onGetPermissionState(int iError) {
			// Return Authorization validation error
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {

				Toast.makeText(mContext,
						"Please check your API Key for Baidu Map",
						Toast.LENGTH_LONG).show();
				BaiduMapHelper.mBDKeyRight = false;
			}
		}
	}
}
