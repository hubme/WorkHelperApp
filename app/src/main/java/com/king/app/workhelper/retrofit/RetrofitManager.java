package com.king.app.workhelper.retrofit;

import com.king.app.workhelper.okhttp.SimpleOkHttp;
import com.king.app.workhelper.retrofit.exception.ApiException;
import com.king.app.workhelper.retrofit.model.HttpResults;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * retrofit管理类
 *
 * @author VanceKing
 * @since 2017/5/27.
 */

public class RetrofitManager {
    private final Retrofit mRetrofit;

    private RetrofitManager() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .client(SimpleOkHttp.getInstance().getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static RetrofitManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        private static RetrofitManager INSTANCE = new RetrofitManager();
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public <T> void toSubscribe(Observable<HttpResults<T>> wrapper, Observer<T> real) {
        wrapper.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<HttpResults<T>, T>() {
                    @Override public T apply(@NonNull HttpResults<T> httpResults) throws Exception {
                        // TODO: 2017/5/27  apiResponse是否可能为null.
                        if (httpResults.code == HttpResponseCode.SUCCESS) {
                            return httpResults.results;
                        } else {
                            throw new ApiException(httpResults.code, httpResults.desc);
                        }
                    }
                })
                .subscribe(real);
    }

    /**
     * 从网络返回的数据(ApiResponse<T>)中抽取需要的数据(T)
     */
    private class HttpResultsUnpacker<T> implements Function<HttpResults<T>, T> {
        @Override public T apply(@NonNull HttpResults<T> tHttpResults) throws Exception {
            if (tHttpResults.code == HttpResponseCode.SUCCESS) {
                return tHttpResults.results;
            }
            throw new ApiException(tHttpResults.code, tHttpResults.desc);
        }
    }
}
