package com.king.app.workhelper.api;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 通过完整的url访问
 *
 * @author VanceKing
 * @since 2017/5/30.
 */
public interface CommonService {

    @GET
    Single<ResponseBody> loadUrl(@Url String url);

    @GET
    @Streaming
    Single<ResponseBody> download(@Url String url);
}
