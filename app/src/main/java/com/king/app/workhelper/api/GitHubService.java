package com.king.app.workhelper.api;

import com.king.app.workhelper.model.entity.Contributor;
import com.king.app.workhelper.model.entity.GitHubUser;
import com.king.app.workhelper.retrofit.model.HttpResults;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

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

    //http://xxx.com/api/group/1024/users?sort=aaa
    @GET("group/{id}/users")
    Single<List<Contributor>> groupList(@Path("id") int groupId, @Query("sort") String sort);

    //http://xxx.com/api/group/1024/users?sort1=aaa&sort2=bbb
    @GET("group/{id}/users")
    Single<List<Contributor>> testQueryMap(@QueryMap() Map<String, String> options);

    @POST("api/users")
    Single<Contributor> createContributor(@Body Contributor contributor);

    @POST()
    @FormUrlEncoded
    Single<Contributor> createContributor(@Field("name") String name, @Field("age") int age);

    @Headers("Content-type:application/x-www-form-urlencoded;charset=UTF-8")
    @POST()
    @FormUrlEncoded
    Single<Contributor> createContributor2(@Header("Content-type") String contentType, @FieldMap Map<String, String> params);

    @Multipart
    @PUT("user/photo")
    Single<Contributor> updateContributor(@Part("photo") RequestBody body, @Part("desc") RequestBody desc);


}
