package com.mengdd.sina.weibo.data;

import org.json.JSONObject;

import android.util.Log;

public class GeoInfo {

    private static final String TAG = "GeoInfo";
    private String longitude = null;
    private String latitude = null;
    private String city = null;
    private String province = null;
    private String city_name = null;
    private String province_name = null;
    private String address = null;
    private String pinyin = null;
    private String more = null;

    public GeoInfo(JSONObject jsonObject) {
        initFromJSONObject(jsonObject);
    }

    private void initFromJSONObject(JSONObject jsonObject) {
        if (null == jsonObject) {

            Log.e(TAG, "object is null in init!");
            return;
        }
        longitude = jsonObject.optString("longitude");
        latitude = jsonObject.optString("latitude");
        city = jsonObject.optString("city");
        province = jsonObject.optString("province");
        city_name = jsonObject.optString("city_name");
        province_name = jsonObject.optString("province_name");
        address = jsonObject.optString("address");
        pinyin = jsonObject.optString("pinyin");
        more = jsonObject.optString("more");
    }
}
