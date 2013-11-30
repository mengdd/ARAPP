package com.mengdd.custommarker;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;

public class CustomBottomViewModel extends ViewModel {

    private View mRootView;

    private Button btn1;
    private Button btn2;
    private Button btn3;

    public CustomBottomViewModel(Activity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Intent intent) {
        super.onCreate(intent);
        mRootView = View.inflate(mActivity, R.layout.bottom_menu_custom, null);

        btn1 = (Button) mRootView.findViewById(R.id.custom_map);
        btn2 = (Button) mRootView.findViewById(R.id.custom_list);
        btn3 = (Button) mRootView.findViewById(R.id.custom_realscene);

    }

    public void setOnClickListener(OnClickListener listener) {
        if (null == listener) {
            throw new IllegalArgumentException("listener is null!");
        }

        btn1.setOnClickListener(listener);
        btn2.setOnClickListener(listener);
        btn3.setOnClickListener(listener);

    }

    @Override
    public View getView() {
        return mRootView;
    }

}
