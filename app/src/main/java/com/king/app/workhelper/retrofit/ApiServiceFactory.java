package com.king.app.workhelper.retrofit;

import com.king.app.workhelper.api.CommonService;
import com.king.app.workhelper.api.GitHubService;
import com.king.app.workhelper.okhttp.OkHttpProvider;
import com.king.applib.util.JsonUtil;
import com.king.applib.util.StringUtil;

import java.util.HashMap;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author VanceKing
 * @since 2017/5/30 0030.
 */

public class ApiServiceFactory {
    public static final String DEFAULT_BASE_URL = "https://api.github.com";
    //缓存Retrofit
    private final HashMap<String, Retrofit> mRetrofits;
    private static CommonService mCommonApiService;
    private static GitHubService mGitHubService;

    private ApiServiceFactory() {
        mRetrofits = new HashMap<>();
    }

    private static class SingletonHolder {
        private static final ApiServiceFactory INSTANCE = new ApiServiceFactory();
    }

    public static ApiServiceFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public <S> S createService(Class<S> serviceClass) {
        return createService(DEFAULT_BASE_URL, serviceClass);
    }

    public <S> S createService(final String baseUrl, Class<S> serviceClass) {
        return getRetrofit(baseUrl).create(serviceClass);
    }

    public Retrofit getRetrofit(final String baseUrl) {
        if (StringUtil.isNullOrEmpty(baseUrl)) {
            throw new RuntimeException("Base URL required.");
        }
        if (mRetrofits.containsKey(baseUrl)) {
            return mRetrofits.get(baseUrl);
        } else {
            Retrofit retrofit = buildRetrofit(baseUrl);
            mRetrofits.put(baseUrl, retrofit);
            return retrofit;
        }
    }

    private Retrofit buildRetrofit(final String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(OkHttpProvider.getInstance().getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(JsonUtil.getGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static CommonService getCommonApiService() {
        return getCommonApiService(DEFAULT_BASE_URL);
    }

    public static CommonService getCommonApiService(String hostName) {
        if (mCommonApiService == null) {
            mCommonApiService = ApiServiceFactory.getInstance().createService(hostName, CommonService.class);
        }
        return mCommonApiService;
    }

    public static GitHubService getGitHubService() {
        if (mGitHubService == null) {
            mGitHubService = ApiServiceFactory.getInstance().createService(DEFAULT_BASE_URL, GitHubService.class);
        }
        return mGitHubService;
    }
}
