package com.king.app.workhelper.api;

import com.king.app.workhelper.model.entity.MovieEntity;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * retrofit参数类型
 * Created by VanceKing on 2016/12/6 0006.
 */

public interface MovieService {
    //https://api.douban.com/v2/movie/top250?start=0&count=10
    @GET("top250")
    Call<MovieEntity> getTopMovie(@Query("start") int start, @Query("count") int count);

    @FormUrlEncoded @POST("/")
    Call<MovieEntity> postSample(@Field("name") String name, @Field("age") String age);

    //文件上传
    @Multipart @POST("upload")
    Call<MovieEntity> uploadFileSample(@Part("name") RequestBody name, @Part MultipartBody.Part file);
}
