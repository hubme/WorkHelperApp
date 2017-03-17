package com.king.app.workhelper.fragment;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.model.entity.GitHubUser;
import com.king.app.workhelper.model.entity.MovieEntity;
import com.king.app.workhelper.webservice.GitUserService;
import com.king.app.workhelper.webservice.MovieService;
import com.king.applib.log.Logger;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.schedulers.Schedulers;

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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
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
    public void retrofitSample() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .client(OkHttpUtils.getInstance().getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        final GitUserService service = retrofit.create(GitUserService.class);
        service.getUser("Guolei1130").subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GitHubUser>() {
                    @Override public void onCompleted() {
                        Logger.i("GitUserService->onCompleted");
                    }

                    @Override public void onError(Throwable e) {
                        Logger.i("GitUserService->onError");

                    }

                    @Override public void onNext(GitHubUser gitHubUser) {
                        Logger.i("GitUserService->" + gitHubUser.toString());
                    }
                });

    }
}
