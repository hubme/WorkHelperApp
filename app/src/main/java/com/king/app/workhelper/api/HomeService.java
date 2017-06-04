package com.king.app.workhelper.api;

import com.king.app.workhelper.model.ServiceModel;
import com.king.app.workhelper.retrofit.model.HttpResults;

import io.reactivex.Flowable;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author VanceKing
 * @since 2017/6/3 0003.
 */

public interface HomeService {

    @FormUrlEncoded @POST("/gjj/getAllServicesUiConfig.go")
    Single<HttpResults<ServiceModel>> getBanners(@Field("skin") int skinType);

    @FormUrlEncoded @POST("/gjj/getAllServicesUiConfig.go")
    Flowable<HttpResults<ServiceModel>> getHomeBanners(@Field("skin") int skinType);
}
