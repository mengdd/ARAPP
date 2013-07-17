package com.mengdd.utils;


import com.mengdd.arapp.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

public class DialogUtils
{
	/**
	 * 
	 * interface for the real action the dialog do after confirmation
	 * That is, when the user choose Yes, execute this interface's method to do the work
	 *
	 */
	public interface OnShowLocationSetttingsListener
	{
		public void onShowLocationSettings();
	}
	
	/**
	 * The dialog object for Location Settings 
	 */
	private static AlertDialog mLocationSetDialog = null;
	
	public static void showIfLocationSettingDialog(Context context, OnShowLocationSetttingsListener onShowListener, String providerName)
	{
		//get the layout view
		View view = View.inflate(context, R.layout.dialog_single_tip, null);
		//new the dialog listener
		ShowLocationSettingDialogListener dialogListener = new ShowLocationSettingDialogListener(context,onShowListener,  view, providerName);
		
		//dismiss a dialog before if it exist
		if(null != mLocationSetDialog)
		{
			mLocationSetDialog.dismiss();
		}
		
		//construct and show the dialog
		mLocationSetDialog= UIUtils.showDialog(context, dialogListener, R.string.dialog_title_setting_location, R.string.alert_confirm, -1, R.string.alert_cancel, view);
	}
	
	/**
	 * 
	 * The listener for the Location Settings Dialog
	 * It handles the user's choice and do the responding work
	 * 
	 *
	 */
	private static class ShowLocationSettingDialogListener implements DialogInterface.OnClickListener
	{
		Context mContext;

		OnShowLocationSetttingsListener mOnShowLocationSetttingsListener = null; 

		public ShowLocationSettingDialogListener(Context context,OnShowLocationSetttingsListener listener,View view,  String providerName)
		{
			mContext = context;
			
			mOnShowLocationSetttingsListener = listener;
			
			initView(view, providerName);
		}
		
		private void initView(View view, String providerName) {
			TextView mTipsTextView = (TextView) view.findViewById(R.id.single_tip);
			
			//Set the TextView content of this dialog
			String firstString = mContext.getResources().getString(R.string.dialog_provider_not_enabled,providerName);
			String secondString = mContext.getResources().getString(R.string.dialog_set_location_src);
			mTipsTextView.setText(firstString + secondString);
		}

		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			if(DialogInterface.BUTTON_POSITIVE == which)
			{
				if(null != mOnShowLocationSetttingsListener)
				{
					mOnShowLocationSetttingsListener.onShowLocationSettings();
				}
			}
			
			if(null != mLocationSetDialog)
			{
				mLocationSetDialog.dismiss();
				mLocationSetDialog = null;
			}
			
		}
		
	}

}
