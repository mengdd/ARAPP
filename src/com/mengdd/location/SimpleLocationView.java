package com.mengdd.location;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;

public class SimpleLocationView extends ViewModel {

    private TextView mRootView = null;

    public SimpleLocationView(Activity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRootView = (TextView) mActivity.getLayoutInflater().inflate(
                R.layout.simple_location_view, null);

    }

    @Override
    public View getView() {
        return mRootView;
    }

    public void setLocationInfo(String info) {
        mRootView.setText(info);

    }
}
