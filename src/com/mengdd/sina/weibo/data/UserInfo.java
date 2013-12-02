package com.mengdd.sina.weibo.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.mengdd.utils.SharedPrefUtil;

public class UserInfo {

    private static final String TAG = "UserInfo";
    private long id = 0;
    private String name = null;
    private String location = null;
    private String description = null;
    private String icon_url = null;

    private int statusesCount = 0;
    private int followersCount = 0;
    private int friendsCount = 0;

    public UserInfo(Context context) {

        loadFromPref(context);
    }

    public UserInfo(String json) {

        try {
            JSONObject object = new JSONObject(json);
            initFromJSONObject(object);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public UserInfo(JSONObject jsonObject) {

        initFromJSONObject(jsonObject);
    }

    private void initFromJSONObject(JSONObject object) {
        if (null == object) {

            Log.e(TAG, "object is null in init!");
            return;
        }
        id = Long.parseLong(object.optString("id"));
        name = object.optString("name");
        location = object.optString("location");
        description = object.optString("description");
        icon_url = object.optString("profile_image_url");

        statusesCount = Integer.parseInt(object.optString("statuses_count"));
        followersCount = Integer.parseInt(object.optString("followers_count"));
        friendsCount = Integer.parseInt(object.optString("friends_count"));

    }

    public void saveToPref(Context context) {
        SharedPrefUtil.saveLong(context, "id", id);
        SharedPrefUtil.saveString(context, "name", name);
        SharedPrefUtil.saveString(context, "location", location);
        SharedPrefUtil.saveString(context, "description", description);
        SharedPrefUtil.saveString(context, "profile_image_url", icon_url);

        SharedPrefUtil.saveInt(context, "statuses_count", statusesCount);
        SharedPrefUtil.saveInt(context, "followers_count", followersCount);
        SharedPrefUtil.saveInt(context, "friends_count", friendsCount);
    }

    public void loadFromPref(Context context) {
        id = SharedPrefUtil.getLong(context, "id", id);
        name = SharedPrefUtil.getString(context, "name", name);
        location = SharedPrefUtil.getString(context, "location", location);
        description = SharedPrefUtil.getString(context, "description",
                description);
        icon_url = SharedPrefUtil.getString(context, "profile_image_url",
                icon_url);

        statusesCount = SharedPrefUtil.getInt(context, "statuses_count",
                statusesCount);
        followersCount = SharedPrefUtil.getInt(context, "followers_count",
                followersCount);
        friendsCount = SharedPrefUtil.getInt(context, "friends_count",
                friendsCount);

    }

    @Override
    public String toString() {
        return "UserInfo [id=" + id + ", name=" + name + ", location="
                + location + ", description=" + description + ", icon_url="
                + icon_url + ", statusesCount=" + statusesCount
                + ", followersCount=" + followersCount + ", friendsCount="
                + friendsCount + "]";
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public int getStatusesCount() {
        return statusesCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

}
