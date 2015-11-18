package com.mengdd.sina.weibo.api;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;

public class SinaWeiboAPI {
    /**
     * 访问微博服务接口的地址
     */
    public static final String API_SERVER = "https://api.weibo.com/2";
    /**
     * post请求方式
     */
    public static final String HTTPMETHOD_POST = "POST";
    /**
     * get请求方式
     */
    public static final String HTTPMETHOD_GET = "GET";

    private static Oauth2AccessToken oAuth2accessToken;
    private static String accessToken;

    public static Oauth2AccessToken getOAuth2accessToken() {
        return oAuth2accessToken;
    }

    public static void setOAuth2accessToken(Oauth2AccessToken oAuth2accessToken) {
        SinaWeiboAPI.oAuth2accessToken = oAuth2accessToken;
        SinaWeiboAPI.accessToken = oAuth2accessToken.getToken();
    }

    protected static void request(final String url,
            final WeiboParameters params, final String httpMethod,
            RequestListener listener) {
        params.add("access_token", accessToken);
        AsyncWeiboRunner.request(url, params, httpMethod, listener);
    }

    public enum FEATURE {
        ALL, ORIGINAL, PICTURE, VIDEO, MUSICE
    }

    public enum AUTHOR_FILTER {
        ALL, ATTENTIONS, STRANGER
    }

    public enum SRC_FILTER {
        ALL, WEIBO, WEIQUN
    }

    public enum TYPE_FILTER {
        ALL, ORIGAL
    }

    public enum TYPE {
        STATUSES, COMMENTS, MESSAGE
    }

    public enum COMMENTS_TYPE {
        NONE, CUR_STATUSES, ORIGAL_STATUSES, BOTH
    }

    public enum EMOTION_TYPE {
        FACE, ANI, CARTOON
    }

    public enum LANGUAGE {
        cnname, twname
    }
}
