package com.king.app.workhelper.webservice;

import com.king.app.workhelper.model.entity.GitHubUser;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * https://api.github.com/users/Guolei1130
 * Created by VanceKing on 2017/1/6 0006.
 */

public interface GitUserService {
    @GET("/users/{user}")
    Observable<GitHubUser> getUser(@Path("user") String user);
}
