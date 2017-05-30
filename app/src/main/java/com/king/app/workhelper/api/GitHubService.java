package com.king.app.workhelper.api;

import com.king.app.workhelper.model.entity.Contributor;
import com.king.app.workhelper.model.entity.GitHubUser;
import com.king.app.workhelper.retrofit.model.HttpResults;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * https://api.github.com/users/Guolei1130
 * Created by VanceKing on 2017/1/6 0006.
 */

public interface GitHubService {

    /** See https://developer.github.com/v3/users/ */
    @GET("/users/{user}")
    Observable<GitHubUser> getUser(@Path("user") String user);
    Observable<HttpResults<GitHubUser>> getUserAAA(@Path("user") String user);

    @GET("/users/{user}")
    Single<HttpResults<GitHubUser>> getUserEx(@Path("user") String user);

    @GET("/repos/{owner}/{repo}/contributors")
    List<Contributor> getContributors(@Path("owner") String owner, @Path("repo") String repo);

    /**
     * https://api.github.com/repos/square/retrofit/contributors
     * See https://developer.github.com/v3/repos/#list-contributors
     */
    @GET("/repos/{owner}/{repo}/contributors")
    Observable<List<Contributor>> contributors(@Path("owner") String owner, @Path("repo") String repo);
}
