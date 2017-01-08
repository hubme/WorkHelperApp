package com.king.app.workhelper.fragment;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.model.entity.Movie;
import com.king.app.workhelper.model.entity.User;
import com.king.app.workhelper.webservice.APIInterface;
import com.king.app.workhelper.webservice.MovieService;
import com.king.applib.log.Logger;

import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
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

    @OnClick(R.id.tv_retrofit_dou_ban)
    public void retrofitRequest() {
        final String baseUrl = "https://api.douban.com/v2/movie/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MovieService movieService = retrofit.create(MovieService.class);
        Call<Movie> call = movieService.getTopMovie(0, 10);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                Logger.i("results: " + response.body().toString());
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Logger.i("onFailure");
            }
        });
    }

    @OnClick(R.id.tv_retrofit_sample)
    public void retrofitSample() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIInterface service = retrofit.create(APIInterface.class);
        Call<User> user = service.getUser("Guolei1130");
        user.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Logger.i("results: " + response.body().toString());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Logger.i("onFailure");
            }
        });
    }
}
