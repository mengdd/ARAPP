package com.mengdd.components;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

/**
 * ViewModel is a Module for easy control and combination of components.
 * Activity can use it as a View and add it in layouts. ViewModel also has
 * lifecycle method to invoke at corresponding Activity lifecycle.
 *
 * The codes are adapted from the codes of yangzc
 *
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 *
 */
public abstract class ViewModel {

    protected Activity mActivity;
    protected LayoutInflater mInflater;

    protected ViewModel(Activity activity) {
        this.mActivity = activity;
        mInflater = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // 是否被创建了
    private boolean mInited = false;

    public void onCreate(Bundle savedInstanceState) {
        mInited = true;
    }

    public void onStop() {

    }

    public void onDestroy() {
        mInited = false;
    }

    public void onSaveInstanceState(Bundle outState) {

    }

    public boolean IsInited() {
        return mInited;
    }

    // 是否正在运行
    protected boolean mIsRunning = true;

    public void onResume(Intent intent) {
        mIsRunning = true;
    }

    public void onPause() {
        mIsRunning = false;
    }

    public boolean isActived() {
        return mIsRunning;
    }

    public abstract View getView();

    public void onAttachToWindow() {
    }

    public void onDetachWithWindow() {
    }

    public void onError() {
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    public void onNewIntent(Intent intent) {
    }

    private String mTag;

    public void setTag(String tag) {
        this.mTag = tag;
    }

    public String getTag() {
        return mTag;
    }

    public void onConfigurationChanged(Configuration newConfig) {
    }
}
