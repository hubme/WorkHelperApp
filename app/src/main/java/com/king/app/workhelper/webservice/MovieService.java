package com.king.app.workhelper.webservice;

import com.king.app.workhelper.model.entity.Movie;

import retrofit2.Call;
import retrofit2.http.Query;

/**
 * Created by VanceKing on 2016/12/6 0006.
 */

public interface MovieService {
    Call<Movie> getTopMovie(@Query("start") int start, @Query("count") int count);
}
