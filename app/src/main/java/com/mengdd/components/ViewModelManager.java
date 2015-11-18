package com.mengdd.components;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * The ViewModelManager controls several ViewModels. These ViewModels are
 * overlays. not used in this project yet.
 * 
 * The codes are adapted from the codes of yangzc
 * 
 * 
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 * 
 */
public class ViewModelManager {
    private Hashtable<String, ViewModel> mViewModels = null;

    private Stack<ViewModel> mBackStack = null;

    public static final int DEFAULT_MODE = 0;
    public static final int SINGLE_TOP_MODE = 1;
    private final int mLaunchMode = DEFAULT_MODE;

    private ViewGroup mContentViewGroup = null;
    private ViewModel mCurrentViewModel = null;

    private Activity mActivity = null;

    public ViewModelManager(Activity activity, int layoutId) {
        init(activity);
        if (layoutId <= 0) {
            throw new IllegalArgumentException("layoutId must be positive!");
        }
        mContentViewGroup = (ViewGroup) mActivity.findViewById(layoutId);
    }

    public ViewModelManager(Activity activity, ViewGroup contentViewGroup) {
        init(activity);
        if (contentViewGroup == null) {
            throw new IllegalArgumentException(
                    "contentViewGroup can not be null!");
        }
        mContentViewGroup = contentViewGroup;

    }

    private void init(Activity activity) {
        mActivity = activity;
        mViewModels = new Hashtable<String, ViewModel>();
        mBackStack = new Stack<ViewModel>();

    }

    /**
     * ��ʾViewModel
     * 
     * @param tag
     * @param viewModel
     * @param intent
     * @param destoryLast
     *            whether to destroy the last ViewModel
     * @return
     */
    public boolean showViewModel(String tag, ViewModel viewModel,
            Intent intent, boolean destoryLast) {
        return addViewModel(tag, viewModel, intent, destoryLast);
    }

    public boolean showViewModel(String tag, ViewModel viewModel, Intent intent) {
        return addViewModel(tag, viewModel, intent, true);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (null != mCurrentViewModel) {
            return mCurrentViewModel.onKeyDown(keyCode, event);
        }
        return false;
    }

    public boolean handleBack(boolean isDestory) {
        try {
            if (null != mCurrentViewModel
                    && mCurrentViewModel.onKeyDown(KeyEvent.KEYCODE_BACK,
                            new KeyEvent(KeyEvent.ACTION_DOWN,
                                    KeyEvent.KEYCODE_BACK))) {
                return true;
            }

            if (mBackStack.size() == 0) {
                return false;
            }

            ViewModel lastModel = mBackStack.pop();
            lastModel.onPause();

            if (isDestory) {
                lastModel.onDestroy();
            }

            if (mBackStack.size() > 0) {
                ViewModel viewModel = mBackStack.pop();
                // ���¼�����˵�ViewModel
                addViewModelToView(viewModel);
                if (!viewModel.IsInited()) {
                    viewModel.onCreate(null);
                }

                viewModel.onResume(null);
                mCurrentViewModel = viewModel;
                return true;
            }
            else {
                mCurrentViewModel = null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean addViewModel(String tag, ViewModel viewModel,
            Intent intent, boolean destoryLast) {

        if (mContentViewGroup == null) {
            throw new IllegalArgumentException(
                    "ContentViewGroup can not be null!");
        }
        if (viewModel == null) {
            throw new IllegalArgumentException("ViewModel can not be null!");
        }

        try {

            boolean isAdd = true;

            if (!viewModel.IsInited()) {
                viewModel.onCreate(intent.getExtras());
            }

            viewModel.onResume(intent);

            isAdd = addViewModelToView(viewModel);

            if (isAdd) {
                if (null != mCurrentViewModel && mCurrentViewModel != viewModel) {
                    if (mCurrentViewModel.isActived()) {
                        mCurrentViewModel.onPause();
                    }

                    if (destoryLast && mCurrentViewModel.IsInited()) {
                        mCurrentViewModel.onDestroy();
                    }
                }

                if (false == mViewModels.containsKey(tag)) {
                    mViewModels.put(tag, viewModel);
                }

                if (mLaunchMode == SINGLE_TOP_MODE
                        && mBackStack.contains(viewModel)) {
                    ViewModel backModel = null;
                    while ((backModel = mBackStack.pop()) != viewModel) {

                        if (backModel.isActived()) {
                            backModel.onPause();
                        }

                        if (destoryLast && viewModel.IsInited()) {
                            backModel.onDestroy();
                            backModel = null;
                        }
                    }

                }
                if (mCurrentViewModel != viewModel) {
                    mBackStack.push(viewModel);
                }

                mCurrentViewModel = viewModel;
                return true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean addViewModelToView(ViewModel viewModel) {

        if (null == viewModel || null == mContentViewGroup) {
            throw new IllegalArgumentException(
                    "contentPanel or viewModel can not by null!");
        }

        View view = viewModel.getView();

        if (null == view) {
            return false;
        }

        // remove currentViewModel from ContentViewGroup
        mContentViewGroup.removeView(mCurrentViewModel.getView());
        if (null != mCurrentViewModel) {
            mCurrentViewModel.onDetachWithWindow();
        }

        // remove new ViewModel from its parent
        if (null != view.getParent()) {
            if (view.getParent() instanceof ViewGroup) {
                // ��Ҫ���ص�View�ĸ��������Ƴ�view
                ((ViewGroup) view.getParent()).removeView(view);
            }
        }

        // ������������ĸ�������Ƴ�
        // UIUtils.detachViewFromParent(mContentViewGroup);

        // �����µ�View
        mContentViewGroup.addView(view, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        viewModel.onAttachToWindow();

        // mContentViewGroup.focusableViewAvailable(view);

        return true;

    }

    /**
     * Remove destroyed ViewModels from Hashtable
     */
    private void recycleDestoryViewModel() {
        // the key to destroy
        List<String> destoryKey = new ArrayList<String>();
        if (null != mViewModels) {
            Iterator<String> keyIterator = mViewModels.keySet().iterator();
            while (keyIterator.hasNext()) {
                String tag = keyIterator.next();
                ViewModel viewModel = mViewModels.get(tag);
                if (!viewModel.IsInited()) {
                    destoryKey.add(tag);
                }
            }
        }

        for (String key : destoryKey) {
            mViewModels.remove(key);
        }
    }

    /**
     * Call all ViewModels' onDestroy and clear Hashtable
     */
    public void recycle() {
        Iterator<String> keyIterator = mViewModels.keySet().iterator();
        while (keyIterator.hasNext()) {
            String tag = keyIterator.next();
            ViewModel viewModel = mViewModels.get(tag);
            if (viewModel.IsInited()) {
                viewModel.onDestroy();
            }
        }
        mViewModels.clear();

        mCurrentViewModel = null;
    }

    /**
     * 销毁当前的ViewModel
     */
    public void destoryCurrentViewModel() {
        if (mCurrentViewModel != null) {
            mCurrentViewModel.onDestroy();
        }
    }

    /**
     * 重新激活ViewModel
     * 
     * @param intent
     */
    public void resumeCurrentViewModel(Intent intent) {
        if (mCurrentViewModel != null && !mCurrentViewModel.IsInited()) {
            mCurrentViewModel.onCreate(intent.getExtras());
        }
        if (mCurrentViewModel != null && !mCurrentViewModel.isActived()) {
            mCurrentViewModel.onResume(intent);
        }
    }

    /**
     * 暂停当前ViewModel
     */
    public void pauseCurrentViewModel() {
        if (mCurrentViewModel != null && mCurrentViewModel.isActived()) {
            mCurrentViewModel.onPause();
        }
    }

    /**
     * newIntent时触发
     * 
     * @param intent
     */
    public void onNewIntent(Intent intent) {
        if (mCurrentViewModel != null) {
            mCurrentViewModel.onNewIntent(intent);
        }
    }

    /**
     * 根据TAG获取ViewModel
     * 
     * @param tag
     * @return
     */
    public ViewModel findViewModelByTag(String tag) {
        return mViewModels.get(tag);
    }
}
