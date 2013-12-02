package com.mengdd.arapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.mengdd.components.ViewModel;

public class FrameHeaderViewModel extends ViewModel {
    private View mRootView;
    private View mSettingView;
    private TextView mTitleView;
    private ImageView mBackView;

    private OnBackListener mOnBackListener = null;
    private OnSettingListener mOnSettingListener = null;

    public FrameHeaderViewModel(Activity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = View.inflate(mActivity, R.layout.frame_header, null);
        mBackView = (ImageView) mRootView.findViewById(R.id.header_back);
        mBackView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mOnBackListener != null) {
                    mOnBackListener.onBack();
                }

            }
        });
        mSettingView = (ImageView) mRootView.findViewById(R.id.header_setting);

        mSettingView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mOnSettingListener != null) {
                    mOnSettingListener.onSetting();
                }
            }
        });

        // 标题
        mTitleView = (TextView) mRootView.findViewById(R.id.header_title);
    }

    @Override
    public View getView() {
        return mRootView;
    }

    public void setTitle(String title) {
        if (mTitleView != null) {
            mTitleView.setText(title);
        }
    }

    public void setSettingVisibility(int visibility) {
        if (mSettingView != null) {
            mSettingView.setVisibility(visibility);
        }
    }

    public void setBackVisibility(int visibility) {
        if (mBackView != null) {
            mBackView.setVisibility(visibility);
        }
    }

    public static interface OnBackListener {
        public void onBack();
    }

    public static interface OnSettingListener {
        public void onSetting();
    }

    public void setOnBackListener(OnBackListener onBackListener) {
        mOnBackListener = onBackListener;
    }

    public void setOnSettingListener(OnSettingListener onSettingListener) {
        mOnSettingListener = onSettingListener;
    }

}
