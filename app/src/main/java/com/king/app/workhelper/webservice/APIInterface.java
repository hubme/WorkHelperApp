package com.king.app.workhelper.webservice;

import com.king.app.workhelper.model.entity.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by VanceKing on 2017/1/6 0006.
 */

public interface APIInterface {
    @GET("/users/{user}")
    Call<User> getUser(@Path("user") String user);
}
