package com.mengdd.search;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;

public class SearchBottomViewModel extends ViewModel implements OnClickListener {
    private View mRootView;

    private List<Button> mButtons = null;

    public SearchBottomViewModel(Activity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRootView = View.inflate(mActivity, R.layout.bottom_menu_search, null);

        Button btn1 = (Button) mRootView.findViewById(R.id.search_menu_ui);
        Button btn2 = (Button) mRootView.findViewById(R.id.search_menu_list);
        Button btn3 = (Button) mRootView.findViewById(R.id.search_menu_map);
        Button btn4 = (Button) mRootView.findViewById(R.id.search_menu_real);

        mButtons = new ArrayList<Button>();
        mButtons.add(btn1);
        mButtons.add(btn2);
        mButtons.add(btn3);
        mButtons.add(btn4);

        for (Button btn : mButtons) {
            btn.setOnClickListener(this);
        }

    }

    public Button getButton(int index) {
        Button resultButton = mButtons.get(index);

        return resultButton;
    }

    private OnClickListener mOnClickListener = null;

    public void setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
    }

    @Override
    public void onClick(View v) {

        for (Button btn : mButtons) {
            if (btn == v) {
                btn.setSelected(true);
            } else {
                btn.setSelected(false);
            }
        }
        if (null != mOnClickListener) {
            mOnClickListener.onClick(v);
        }

    }

    @Override
    public View getView() {
        return mRootView;
    }

}
