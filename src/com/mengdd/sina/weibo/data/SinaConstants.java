package com.mengdd.sina.weibo.data;

public class SinaConstants {

    public static final int MAX_CHARACTERS_COUNT = 140;
    public static final String LOG_TAG = "meng_log";

    public static final String APP_KEY = "1993728786";
    public static final String APP_SECRET = "55717a34e9e05183993147c3beb03d02";
    // 替换为开发者REDIRECT_URL
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

    // 新支持scope 支持传入多个scope权限，用逗号分隔
    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog";
}
