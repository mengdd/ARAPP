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
public class BaiduMapHelper
{

	private static final String strKey = "3BDBC2CD0C6C1F6CB163ED66F26012C7F7CCECC8";
	private static boolean mBDKeyRight = true;
	private static BMapManager mBMapManager = null;
	public static BMapManager getMapManager()
	{
		return mBMapManager;
	}

	/*
	// Singleton Pattern
	// Volatile修饰的成员变量在每次被线程访问时，都强迫从共享内存中重读该成员变量的值。
	// 即阻止线程为了优化性能而保有变量的私有拷贝
	private volatile static BaiduMapHelper mInstance = null;

	public static BaiduMapHelper getInstance()
	{
		if (null == mInstance)
		{
			synchronized (BaiduMapHelper.class)
			{
				if (null == mInstance)
				{
					mInstance = new BaiduMapHelper();
				}

			}

		}

		return mInstance;
	}

	// Singleton Pattern: use a private constructor so instance can only be
	// created inside this class
	private BaiduMapHelper()
	{

	}
	
	*/

	public static void initBaiduMapManager(Context context)
	{
		Log.d(AppConstants.LOG_TAG, "initEngineManager");
		if (null == mBMapManager)
		{
			mBMapManager = new BMapManager(context);
		}

		if (null == mBMapManager
				|| !mBMapManager.init(strKey, new MyGeneralListener(context)))
		{
			Toast.makeText(context, "BMapManager Initialization failed!",
					Toast.LENGTH_LONG).show();
		}
	}

	public static void releaseMapManager()
	{
		if(null != mBMapManager)
		{
			mBMapManager.destroy();
			mBMapManager = null;
		}
	}
	/**
	 * 
	 * Class for EventListener, handle the Registration Errors
	 * 
	 */
	static class MyGeneralListener implements MKGeneralListener
	{

		private Context mContext = null;

		public MyGeneralListener(Context context)
		{
			mContext = context;
		}

		@Override
		public void onGetNetworkState(int iError)
		{
			// Return Network error
			if (iError == MKEvent.ERROR_NETWORK_CONNECT)
			{
				Toast.makeText(mContext, "Network Connection Error",
						Toast.LENGTH_LONG).show();
			}
			else if (iError == MKEvent.ERROR_NETWORK_DATA)
			{
				Toast.makeText(mContext, "Network Data Error",
						Toast.LENGTH_LONG).show();
			}

		}

		@Override
		public void onGetPermissionState(int iError)
		{
			// Return Authorization validation error
			if (iError == MKEvent.ERROR_PERMISSION_DENIED)
			{

				Toast.makeText(mContext,
						"Please check your API Key for Baidu Map",
						Toast.LENGTH_LONG).show();
				BaiduMapHelper.mBDKeyRight = false;
			}
		}
	}
}
