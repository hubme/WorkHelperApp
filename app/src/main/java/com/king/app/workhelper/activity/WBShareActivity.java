package com.king.app.workhelper.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.applib.log.Logger;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;

import java.text.SimpleDateFormat;

import butterknife.OnClick;

/**
 * 该类演示了第三方应用如何通过微博客户端.
 * 执行流程： 从本应用->微博->本应用
 *
 * @author VanceKing
 * @since 2017/3/14 0014.
 */

public class WBShareActivity extends AppBaseActivity implements IWeiboHandler.Response {
    private static final String APP_KEY = "1667532223";
    /**
     * 当前应用的回调页，第三方应用可以使用自己的回调页。
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static final String REDIRECT_URL = "http://www.sina.com";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
    private static final int SHARE_CLIENT = 1;
    private static final int SHARE_ALL_IN_ONE = 2;

    private int mShareType = SHARE_CLIENT;

    /** 微博微博分享接口实例 */
    private IWeiboShareAPI mWeiBoShareAPI;
    private SsoHandler mSsoHandler;
    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
    private Oauth2AccessToken mAccessToken;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_share;
    }

    @Override
    protected void initData() {
        super.initData();

        // 创建微博分享接口实例
        mWeiBoShareAPI = WeiboShareSDK.createWeiboAPI(this, APP_KEY);


        AuthInfo mAuthInfo = new AuthInfo(this, APP_KEY, REDIRECT_URL, SCOPE);
        mSsoHandler = new SsoHandler(this, mAuthInfo);
        mSsoHandler.authorize(new AuthListener());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiBoShareAPI.handleWeiboResponse(intent, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
    }

    @OnClick(R.id.tv_register_app)
    public void onRegisterAppClick() {
        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
        //NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
        boolean registerSuccess = mWeiBoShareAPI.registerApp();
        showToast(registerSuccess ? "注册成功" : "注册失败");
    }

    @OnClick(R.id.tv_share_text)
    public void onShareTextClick() {
        sendMultiMessage(true, false);
    }

    @OnClick(R.id.tv_share_pic)
    public void onSharePicClick() {
        sendMultiMessage(false, true);
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        if (baseResponse == null) {
            return;
        }
        switch (baseResponse.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                showToast("分享成功");
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                showToast("取消分享");
                break;
            case WBConstants.ErrorCode.ERR_FAIL://认证失败很可能是APP_KEY和包名不对应的问题。第一次分享失败，以后分享成功和授权有关.
                showToast("分享失败" + "Error Message: " + baseResponse.errMsg);
                break;
        }
    } 

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     */
    private void sendMultiMessage(boolean hasText, boolean hasImage) {
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (hasText) {
            weiboMessage.textObject = getTextObj();
        }
        if (hasImage) {
            weiboMessage.imageObject = getImageObj();
        }
        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        // 3. 发送请求消息到微博，唤起微博分享界面
        if (mShareType == SHARE_CLIENT) {
            mWeiBoShareAPI.sendRequest(this, request);
        } else if (mShareType == SHARE_ALL_IN_ONE) {
            AuthInfo authInfo = new AuthInfo(this, APP_KEY, REDIRECT_URL, SCOPE);
            Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(getApplicationContext());
            String token = "";
            if (accessToken != null) {
                token = accessToken.getToken();
            }
            mWeiBoShareAPI.sendRequest(this, request, authInfo, token, new WeiboAuthListener() {

                @Override
                public void onWeiboException(WeiboException arg0) {
                }

                @Override
                public void onComplete(Bundle bundle) {
                    Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                    AccessTokenKeeper.writeAccessToken(getApplicationContext(), newToken);
                    showToast("onAuthorizeComplete token = " + newToken.getToken());
                }

                @Override
                public void onCancel() {
                }
            });
        }
    }

    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = "@大屁老师，这是一个很漂亮的小狗，朕甚是喜欢-_-!!";
        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj() {
        ImageObject imageObject = new ImageObject();
        //设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_4073);
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     * 该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    private class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(final Bundle values) {
            // 从 Bundle 中解析 Token
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAccessToken = Oauth2AccessToken.parseAccessToken(values);
                    //从这里获取用户输入的 电话号码信息
                    String phoneNum = mAccessToken.getPhoneNum();
                    if (mAccessToken.isSessionValid()) {
                        // 显示 Token
                        updateTokenView(false);

                        // 保存 Token 到 SharedPreferences
                        AccessTokenKeeper.writeAccessToken(WBShareActivity.this, mAccessToken);
                        showToast("授权成功");
                    } else {
                        // 以下几种情况，您会收到 Code：
                        // 1. 当您未在平台上注册的应用程序的包名与签名时；
                        // 2. 当您注册的应用程序包名与签名不正确时；
                        // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                        String code = values.getString("code");
                        String message = "授权失败";
                        if (!TextUtils.isEmpty(code)) {
                            message = message + "\nObtained the code: " + code;
                        }
                        Toast.makeText(WBShareActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                }
            });

        }

        @Override
        public void onCancel() {
            showToast("取消授权");
        }

        @Override
        public void onWeiboException(WeiboException e) {
            showToast("Auth exception : " + e.getMessage());
        }
    }

    /**
     * 显示当前 Token 信息。
     *
     * @param hasExisted 配置文件中是否已存在 token 信息并且合法
     */
    private void updateTokenView(boolean hasExisted) {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                new java.util.Date(mAccessToken.getExpiresTime()));
        String format = "Token：%1$s \\n有效期：%2$s";
        Logger.i("Token: " + String.format(format, mAccessToken.getToken(), date));

        String message = String.format(format, mAccessToken.getToken(), date);
        if (hasExisted) {
            message = "Token 仍在有效期内，无需再次登录。\n" + message;
        }
        Logger.i("message: " + message);
    }
}
