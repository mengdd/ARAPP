package com.mengdd.arapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;

import com.mengdd.arapp.R;

public class EntryActivity extends Activity {

    private Activity mActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.entry_activity);
        mActivity = this;
        setVersionName();

        // 跑马灯效果
        TextView blogTextView = (TextView) findViewById(R.id.welcome_blog);
        blogTextView.setSelected(true);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(mActivity, MainActivity.class);
                startActivity(intent);

                mActivity.finish();
            }

        }, 2000);
    }

    private void setVersionName() {
        TextView versionTextView = (TextView) findViewById(R.id.welcome_version);
        versionTextView.setText(getVersionName());
    }

    private String getVersionName() {
        String name;
        PackageManager pm = this.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(this.getPackageName(), 0);
            name = info.versionName;
            name = "Version: " + name;
        }
        catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return name;
    }
}
