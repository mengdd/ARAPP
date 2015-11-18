package com.mengdd.custommarker;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;

public class CustomBottomViewModel extends ViewModel implements OnClickListener {

    private View mRootView;

    private List<Button> mButtons = null;

    public CustomBottomViewModel(Activity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = View.inflate(mActivity, R.layout.bottom_menu_custom, null);

        Button btn1 = (Button) mRootView.findViewById(R.id.custom_map);
        Button btn2 = (Button) mRootView.findViewById(R.id.custom_list);
        Button btn3 = (Button) mRootView.findViewById(R.id.custom_realscene);

        mButtons = new ArrayList<Button>();
        mButtons.add(btn1);
        mButtons.add(btn2);
        mButtons.add(btn3);

        for (Button button : mButtons) {
            button.setOnClickListener(this);
        }

    }

    private OnClickListener mOnClickListener = null;

    public void setOnClickListener(OnClickListener listener) {

        mOnClickListener = listener;

    }

    @Override
    public View getView() {
        return mRootView;
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

    public Button getButton(int index) {
        Button resultButton = mButtons.get(index);

        return resultButton;
    }

}
