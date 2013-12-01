package com.mengdd.search.keywords;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;
import com.mengdd.search.keywords.CategoryView.KeywordListener;

public class KeywordsNaviViewModel extends ViewModel {

    private View mRootView = null;

    private List<CategoryData> mDatas = null;

    private LinearLayout mMainListLayout = null;

    public KeywordsNaviViewModel(Activity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = mInflater.inflate(R.layout.keywords_navi_layout, null);

        mMainListLayout = (LinearLayout) mRootView.findViewById(R.id.main_list);

        initData(mActivity);

        if (null != mDatas) {
            for (CategoryData data : mDatas) {
                CategoryView newView = new CategoryView(mActivity);
                newView.setTitle(data.getName());

                // add items according to their counts
                newView.addItemViews(data.getItems());

                // use the listener in this class to listen for all categories'
                // keywords
                newView.setKeywordListener(mKeywordReceiverListener);
                // then the mKeywordReceiverListener will report result to
                // outside listener

                mMainListLayout.addView(newView);
            }
        }

    }

    @Override
    public View getView() {
        return mRootView;

    }

    private void initData(Context context) {
        // 初始化数据

        // 根据资源中的字符串进行数据初始化
        Resources resources = context.getResources();
        String[] categories = resources
                .getStringArray(R.array.keywords_categories);
        mDatas = new ArrayList<CategoryData>();

        for (int i = 0; null != categories && i < categories.length; ++i) {
            CategoryData categoryData = new CategoryData(categories[i]);

            addCategoryData(i, categoryData, resources);

            mDatas.add(categoryData);
        }

    }

    private void addCategoryData(int index, CategoryData categoryData,
            Resources resources) {
        String[] contents = null;
        switch (index) {
        case 0:
            contents = resources.getStringArray(R.array.restaurant);
            break;
        case 1:
            contents = resources.getStringArray(R.array.traffic);
            break;
        case 2:
            contents = resources.getStringArray(R.array.shopping);
            break;
        case 3:
            contents = resources.getStringArray(R.array.life);
            break;
        case 4:
            contents = resources.getStringArray(R.array.entertainment);
            break;
        default:
            break;
        }

        categoryData.addItems(contents);
    }

    private KeywordListener mKeywordListener = null;

    public void setKeywordListener(KeywordListener listener) {
        mKeywordListener = listener;
    }

    // this listener receives keywords from all category views
    // and report the result to other listeners outside the view model
    private KeywordListener mKeywordReceiverListener = new KeywordListener() {

        @Override
        public void onKeywordSelected(String keyword) {
            if (null != mKeywordListener) {
                mKeywordListener.onKeywordSelected(keyword);
            }
        }
    };
}
