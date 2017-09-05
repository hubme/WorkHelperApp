package com.king.app.workhelper.api;

import com.king.app.workhelper.model.EmptyModel;
import com.king.app.workhelper.retrofit.model.HttpResults;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

/**
 * @author huoguangxu
 * @since 2017/9/4.
 */

public interface UserApiService {
    @Multipart
    @POST("/user/uploadIcon.go")
    Observable<HttpResults<EmptyModel>> uploadPhoto(@Part MultipartBody.Part part, @QueryMap Map<String, String> params);

    @Multipart
    @POST("/member/uploadMemberIcon.do")
    Observable<HttpResults<EmptyModel>> uploadMultiPhoto(@Part List<MultipartBody.Part> partList);

}
