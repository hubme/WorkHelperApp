package com.king.app.workhelper.webservice;

import com.king.app.workhelper.model.entity.MovieEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * https://api.douban.com/v2/movie/top250?start=0&count=10
 * Created by VanceKing on 2016/12/6 0006.
 */

public interface MovieService {
    @GET("top250")
    Call<MovieEntity> getTopMovie(@Query("start") int start, @Query("count") int count);
}
