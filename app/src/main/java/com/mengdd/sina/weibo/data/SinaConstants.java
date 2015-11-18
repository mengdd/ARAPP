package com.mengdd.sina.weibo.data;

public class SinaConstants {

    public static final int MAX_CHARACTERS_COUNT = 140;
    public static final String LOG_TAG = "meng_log";

    public static final String APP_KEY = "1161687548";
    public static final String APP_SECRET = "312939b75824428ef71386e354fc4bcc";
    // 替换为开发者REDIRECT_URL
    public static final String REDIRECT_URL = "http://www.cnblogs.com/mengdd/";

    // 新支持scope 支持传入多个scope权限，用逗号分隔
    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog";
}
