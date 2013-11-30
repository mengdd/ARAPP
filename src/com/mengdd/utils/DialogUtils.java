package com.mengdd.utils;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import com.mengdd.arapp.R;
import com.mengdd.custommarker.MarkerItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class DialogUtils {
    /**
     * 
     * interface for the real action the dialog do after confirmation That is,
     * when the user choose Yes, execute this interface's method to do the work
     * 
     */
    public interface OnShowLocationSetttingsListener {
        public void onShowLocationSettings();
    }

    /**
     * The dialog object for Location Settings
     */
    private static AlertDialog mLocationSetDialog = null;

    public static void showIfLocationSettingDialog(Context context,
            OnShowLocationSetttingsListener onShowListener, String providerName) {
        // get the layout view
        View view = View.inflate(context, R.layout.dialog_single_tip, null);
        // new the dialog listener
        ShowLocationSettingDialogListener dialogListener = new ShowLocationSettingDialogListener(
                context, onShowListener, view, providerName);

        // dismiss a dialog before if it exist
        if (null != mLocationSetDialog) {
            mLocationSetDialog.dismiss();
        }

        // construct and show the dialog
        mLocationSetDialog = UIUtils.showDialog(context, dialogListener,
                R.string.dialog_title_setting_location, R.string.alert_confirm,
                -1, R.string.alert_cancel, view);
    }

    /**
     * 
     * The listener for the Location Settings Dialog It handles the user's
     * choice and do the responding work
     * 
     * 
     */
    private static class ShowLocationSettingDialogListener implements
            DialogInterface.OnClickListener {
        Context mContext;

        OnShowLocationSetttingsListener mOnShowLocationSetttingsListener = null;

        public ShowLocationSettingDialogListener(Context context,
                OnShowLocationSetttingsListener listener, View view,
                String providerName) {
            mContext = context;

            mOnShowLocationSetttingsListener = listener;

            initView(view, providerName);
        }

        private void initView(View view, String providerName) {
            TextView mTipsTextView = (TextView) view
                    .findViewById(R.id.single_tip);

            // Set the TextView content of this dialog
            String firstString = mContext.getResources().getString(
                    R.string.dialog_provider_not_enabled, providerName);
            String secondString = mContext.getResources().getString(
                    R.string.dialog_set_location_src);
            mTipsTextView.setText(firstString + secondString);
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (DialogInterface.BUTTON_POSITIVE == which) {
                if (null != mOnShowLocationSetttingsListener) {
                    mOnShowLocationSetttingsListener.onShowLocationSettings();
                }
            }

            if (null != mLocationSetDialog) {
                mLocationSetDialog.dismiss();
                mLocationSetDialog = null;
            }

        }

    }

    /**
     * 
     * interface for the real action the dialog do after confirmation That is,
     * when the user choose Yes, execute this interface's method to do the work
     * 
     */
    public interface OnSaveCustomMarkerListener {
        public void onSaveMarker(MarkerItem markerItem);
    }

    /**
     * The dialog object for Save custom marker
     */
    private static AlertDialog mSaveMarkerDialog = null;

    public static void showSaveMarkerDialog(Context context,
            OnSaveCustomMarkerListener onSaveListener, MarkerItem markerItem) {
        // get the layout view
        View view = View.inflate(context, R.layout.dialog_save_marker, null);
        // new the dialog listener
        ShowSaveMarkerDialogListener dialogListener = new ShowSaveMarkerDialogListener(
                context, onSaveListener, view, markerItem);

        // dismiss a dialog before if it exist
        if (null != mSaveMarkerDialog) {
            mSaveMarkerDialog.dismiss();
        }

        // construct and show the dialog
        mSaveMarkerDialog = UIUtils.showDialog(context, dialogListener,
                R.string.dialog_title_save_marker, R.string.alert_confirm, -1,
                R.string.alert_cancel, view);

    }

    private static class ShowSaveMarkerDialogListener implements
            DialogInterface.OnClickListener {
        private Context mContext = null;
        private OnSaveCustomMarkerListener mSaveListener = null;
        private MarkerItem mMarkerItem = null;

        private EditText mNameEditText;
        private EditText mDesEditText;
        private String mNewName = null;
        private String mNewDescripton = null;

        public ShowSaveMarkerDialogListener(Context context,
                OnSaveCustomMarkerListener onSaveCustomMarkerListener,
                View view, MarkerItem markerItem) {

            mContext = context;
            mSaveListener = onSaveCustomMarkerListener;
            mMarkerItem = markerItem;

            initView(view);
        }

        private void initView(View view) {
            Log.i(AppConstants.LOG_TAG, "initView in saveDialog");
            mNameEditText = (EditText) view.findViewById(R.id.marker_name);
            mNameEditText.setText(mMarkerItem.getName());
            mNameEditText.addTextChangedListener(mNameTextWatcher);
            mNewName = mNameEditText.getText().toString();

            mDesEditText = (EditText) view.findViewById(R.id.marker_des);
            mDesEditText.setText(mMarkerItem.getDescription());
            mDesEditText.addTextChangedListener(mDesTextWatcher);
            mNewDescripton = mDesEditText.getText().toString();
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            boolean undismiss = false;
            if (DialogInterface.BUTTON_POSITIVE == which) {
                // check the paramters
                if (TextUtils.isEmpty(mNewName)) {
                    UIUtils.showToast(mContext, "the name is empty!", false);
                    mNameEditText.setText("");
                    undismiss = true;
                }

                if (null != mSaveListener) {
                    mMarkerItem.setName(mNewName);
                    mMarkerItem.setDescription(mNewDescripton);

                    mMarkerItem.setCreateDate(DataUtils.getCurrentTime());

                    mSaveListener.onSaveMarker(mMarkerItem);
                }

            }

            if (null != mSaveMarkerDialog && !undismiss) {
                mSaveMarkerDialog.dismiss();
                mSaveMarkerDialog = null;
            }

        }

        private TextWatcher mNameTextWatcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                if (!TextUtils.isEmpty(s)) {
                    mNewName = s.toString().trim();
                }
                else {
                    mNewName = null;
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateStringLength(mContext, mNameEditText, s.toString(),
                        MAX_MARKER_NAME_LEN);
            }
        };

        private TextWatcher mDesTextWatcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                if (!TextUtils.isEmpty(s)) {
                    mNewDescripton = s.toString().trim();
                }
                else {
                    mNewDescripton = null;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                validateStringLength(mContext, mDesEditText, s.toString(),
                        MAX_MARKER_DES_LEN);
            }
        };

    }

    private static final int MAX_MARKER_NAME_LEN = 20;
    private static final int MAX_MARKER_DES_LEN = 100;

    private static void validateStringLength(Context context,
            EditText editText, String inputString, int maxLength) {
        if (null == inputString) {
            return;
        }
        inputString = inputString.trim();
        int len = 0;
        try {
            len = inputString.getBytes("gbk").length;
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (len > maxLength) {
            int num = inputString.length();
            String valid = inputString.substring(0, num - 1);
            editText.setText(valid);
            editText.setSelection(editText.getText().length());// 光标定位到末尾
            UIUtils.showToast(context,
                    context.getResources().getString(R.string.dialog_too_long),
                    false);
        }
    }
}
