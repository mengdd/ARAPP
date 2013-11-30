package com.mengdd.sina.weibo.api;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.mengdd.sina.weibo.data.SinaConstants;
import com.mengdd.sina.weibo.utils.AccessTokenKeeper;
import com.mengdd.utils.LogUtils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

public class AuthorizeSSOHelper {

    private Activity mActivity = null;
    private final WeiboAuth mWeiboAuth;

    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private final SsoHandler mSsoHandler;

    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
    private Oauth2AccessToken mAccessToken;

    public AuthorizeSSOHelper(Activity activity) {
        mActivity = activity;
        mWeiboAuth = new WeiboAuth(mActivity, SinaConstants.APP_KEY,
                SinaConstants.REDIRECT_URL, SinaConstants.SCOPE);
        mSsoHandler = new SsoHandler(mActivity, mWeiboAuth);
    }

    public void getToken() {
        mSsoHandler.authorize(new AuthListener());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    /**
     * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
     * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
     * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
     * SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {

                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.keepAccessToken(mActivity, mAccessToken);

                Toast.makeText(mActivity, "success", Toast.LENGTH_SHORT).show();
                LogUtils.i("success");
            }
            else {
                // 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
                String code = values.getString("code");
                String message = "failed";
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
                LogUtils.i(message);
            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(mActivity, "cancel", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(mActivity, "Auth exception : " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
}
