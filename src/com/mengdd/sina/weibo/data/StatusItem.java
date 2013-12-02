package com.mengdd.sina.weibo.data;

import org.json.JSONObject;

import android.util.Log;

public class StatusItem {

    private static final String TAG = "StatusItem";

    private UserInfo mUserInfo = null;
    private GeoInfo mGeoInfo = null;
    private StatusItem mRetweetedStatus = null;

    private String created_at = null;
    private long id = 0;
    private String text = null;
    private String source = null;

    private int reposts_count = 0;
    private int comments_count = 0;
    private int attitudes_count = 0;

    private String thumbnail_pic = null;
    private String bmiddle_pic = null;
    private String original_pic = null;

    public StatusItem(JSONObject jsonObject) {

        initFromJSONObject(jsonObject);

    }

    private void initFromJSONObject(JSONObject jsonObject) {
        if (null == jsonObject) {

            Log.e(TAG, "object is null in init!");
            return;
        }
        created_at = jsonObject.optString("created_at");
        id = jsonObject.optLong("id");
        text = jsonObject.optString("text");
        source = jsonObject.optString("source");

        reposts_count = jsonObject.optInt("reposts_count");
        comments_count = jsonObject.optInt("comments_count");
        attitudes_count = jsonObject.optInt("attitudes_count");

        thumbnail_pic = jsonObject.optString("thumbnail_pic");
        bmiddle_pic = jsonObject.optString("bmiddle_pic");
        original_pic = jsonObject.optString("original_pic");

        mUserInfo = new UserInfo(jsonObject.optJSONObject("user"));
        mGeoInfo = new GeoInfo(jsonObject.optJSONObject("geo"));
        mRetweetedStatus = new StatusItem(
                jsonObject.optJSONObject("retweeted_status"));

    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public String getText() {
        return text;
    }

}
