package com.king.app.workhelper.fragment;

import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.api.CommonService;
import com.king.app.workhelper.api.GitHubService;
import com.king.app.workhelper.api.MovieService;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.model.entity.GitHubUser;
import com.king.app.workhelper.model.entity.MovieEntity;
import com.king.app.workhelper.retrofit.ApiServiceFactory;
import com.king.app.workhelper.retrofit.subscriber.HttpResultSubscriber;
import com.king.app.workhelper.retrofit.subscriber.ResultSubscriberManger;
import com.king.app.workhelper.rx.RxUtil;
import com.king.applib.log.Logger;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;

import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit.
 * http://mp.weixin.qq.com/s?__biz=MzI5NjQxNDE3Ng==&mid=2247483665&idx=1&sn=c87127b2617e11fe52e36d7144a613ee&mpshare=1&scene=1&srcid=1010ClfxSMqG3vMfgRd8mOcD#rd
 * Created by VanceKing on 2016/11/26.
 */

public class RetrofitSampleFragment extends AppBaseFragment {

    @Override
    protected int getContentLayout() {
        return R.layout.activity_sample_retrofit;
    }

    @OnClick(R.id.tv_common_retrofit)
    public void retrofitRequest() {
        final String baseUrl = "https://api.douban.com/v2/movie/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        MovieService movieService = retrofit.create(MovieService.class);
        Call<MovieEntity> call = movieService.getTopMovie(0, 10);
        call.enqueue(new Callback<MovieEntity>() {
            @Override
            public void onResponse(Call<MovieEntity> call, Response<MovieEntity> response) {
                Logger.i("results: " + response.body().toString());
            }

            @Override
            public void onFailure(Call<MovieEntity> call, Throwable t) {
                Logger.i("onFailure");
            }
        });
    }

    @OnClick(R.id.tv_rx_retrofit)
    public void retrofitSample(final TextView textView) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com").client(OkHttpUtils.getInstance().getOkHttpClient()).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        final GitHubService service = retrofit.create(GitHubService.class);
        service.getUser("Guolei1130").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableObserver<GitHubUser>() {
            @Override
            public void onNext(GitHubUser gitHubUser) {
                Logger.i("GitUserService->" + gitHubUser.toString());
                textView.setText(gitHubUser.login);
            }

            @Override
            public void onError(Throwable e) {
                Logger.i("GitUserService->onError");
            }

            @Override
            public void onComplete() {
                Logger.i("GitUserService->onCompleted");
            }
        });

    }

    @OnClick(R.id.tv_rx_retrofit_package)
    public void aaa() {
        GitHubService gitHubService = ApiServiceFactory.getInstance().createService(GitHubService.class);
        gitHubService.getUserEx("Guolei1130")
                .compose(RxUtil.defaultSingleSchedulers())
                .subscribe(ResultSubscriberManger.create(new HttpResultSubscriber<GitHubUser>() {
                    @Override public void onSuccess(GitHubUser gitHubUser, String msg) {
                        Logger.i(gitHubUser.toString());
                    }

                    @Override public void onFailure(int errorCode, String msg) {
                        Logger.i("errorCode: " + errorCode + ";msg: " + msg);
                    }
                }));
    }

    @OnClick(R.id.tv_common_service)
    public void onCommonServiceClick() {
        final String URL_PUBLIC = "https://publicobject.com/helloworld.txt/";
        CommonService commonService = ApiServiceFactory.getInstance().createService(URL_PUBLIC, CommonService.class);
        commonService.loadUrl(URL_PUBLIC)
                .compose(RxUtil.defaultSingleSchedulers())
                .subscribe(new DisposableSingleObserver<ResponseBody>() {
                    @Override
                    public void onSuccess(@NonNull ResponseBody responseBody) {
                        try {
                            Logger.i(responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Logger.e(e.toString());
                    }
                });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
