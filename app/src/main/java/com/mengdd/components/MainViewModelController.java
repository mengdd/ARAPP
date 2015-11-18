package com.mengdd.components;

import android.app.Activity;
import android.content.Intent;
import android.util.SparseArray;
import android.view.KeyEvent;

/**
 * 
 * The MainControll contains a ViewModelManager and some Main View Models for
 * scene management. not used in this project yet.
 * 
 * The codes are adapted from the codes of yangzc
 * 
 * 
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 * 
 */
public class MainViewModelController {
    private ViewModelManager mViewModelManager;
    private SparseArray<ViewModel> mActivedViewModel;
    private Activity mActivity;
    private int mCurrentSceneId = -1;

    public MainViewModelController(Activity activity, int layoutId) {
        mViewModelManager = new ViewModelManager(activity, layoutId);
        this.mActivity = activity;
        initViewModel(activity);// 初始化所有的视图模型
    }

    /**
     * 初始化默认视图
     */
    private void initViewModel(Activity activity) {
        mActivedViewModel = new SparseArray<ViewModel>();

    }

    /**
     * 切换场景
     * 
     * @param sceneId
     * @return
     */
    public boolean switchScene(int sceneId) {
        return switchScene(sceneId, null);
    }

    /**
     * 切换场景
     * 
     * @param sceneId
     * @param intent
     * @return
     */
    public boolean switchScene(int sceneId, Intent intent) {

        ViewModel viewModel = mActivedViewModel.get(sceneId);
        if (viewModel == null) {
            return false;
        }

        boolean suc = mViewModelManager.showViewModel(sceneId + "", viewModel,
                intent, false);
        if (suc) {
            mCurrentSceneId = sceneId;
        }
        return suc;
    }

    /**
     * 分发键盘点击事件
     * 
     * @param keyCode
     * @param event
     * @return
     */
    public boolean dispatchKeyEvent(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            return false;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            boolean handleback = mViewModelManager.handleBack(false);

            return handleback;
        }
        return mViewModelManager.onKeyDown(keyCode, event);
    }

    /**
     * 销毁所有的视图模型
     */
    public void destoryAllViewModel() {
        mViewModelManager.recycle();
    }

    /**
     * 激活当前ViewModel
     */
    public void dispatchResume() {

        mViewModelManager.resumeCurrentViewModel(null);
    }

    /**
     * 分发newIntent
     * 
     * @param intent
     */
    public void dispatchNewIntent(Intent intent) {
        if (onNewIntent(intent))
            return;

        mViewModelManager.onNewIntent(intent);
    }

    /**
     * 各个场景控制部分 修改相应代码
     * 
     * @param intent
     * @return
     */
    public boolean onNewIntent(Intent intent) {

        return false;
    }

    /**
     * 暂停当前ViewModel
     */
    public void dispatchPause() {

        mViewModelManager.pauseCurrentViewModel();
    }

    public void dispatchStop() {
    }

    /**
     * 回退处理
     */
    public void backViewModel() {
        dispatchKeyEvent(KeyEvent.KEYCODE_BACK, new KeyEvent(
                KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
    }

}
