package com.king.app.workhelper.okhttp;

import android.os.Environment;

import com.king.app.workhelper.BuildConfig;
import com.king.app.workhelper.okhttp.interceptor.LogInterceptor;
import com.king.applib.log.Logger;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * OkHttpClient提供者
 *
 * @author VanceKing
 * @since 2017/5/30.
 */

public class OkHttpProvider {
    public static final long DEFAULT_CONNECT_TIMEOUT = 10;
    public static final long DEFAULT_WRITE_TIMEOUT = 20;
    public static final long DEFAULT_READ_TIMEOUT = 10;

    private OkHttpClient mOkHttpClient;

    private OkHttpProvider() {
        initOkHttp();
    }

    private static class SingletonHolder {
        private static final OkHttpProvider INSTANCE = new OkHttpProvider();
    }

    public static OkHttpProvider getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private void initOkHttp() {
        // 自动刷新 token
        Authenticator mAuthenticator = new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) {
                Logger.i("自动刷新 token 开始");
                String accessToken = "";
                /*TokenService tokenService = mRetrofit.create(TokenService.class);
                try {
                    if (null != mCacheUtil.getToken()) {
                        Call<Token> call = tokenService.refreshToken(OAuth.client_id,
                                OAuth.client_secret, OAuth.GRANT_TYPE_REFRESH,
                                mCacheUtil.getToken().getRefresh_token());
                        retrofit2.Response<Token> tokenResponse = call.execute();
                        Token token = tokenResponse.body();
                        if (null != token) {
                            mCacheUtil.saveToken(token);
                            accessToken = token.getAccess_token();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                Logger.i("自动刷新 token 结束：" + accessToken);
                return response.request().newBuilder()
                        .addHeader("KEY_TOKEN", accessToken)
                        .build();
            }
        };

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .cache(new Cache(new File(Environment.getExternalStorageDirectory().getPath() + "/000test/cache"), 5 * 1024 * 1024L))
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .authenticator(mAuthenticator);
        if (BuildConfig.LOG_DEBUG) {
            builder.addInterceptor(new LogInterceptor());
        }
        mOkHttpClient = builder.build();
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }
}
