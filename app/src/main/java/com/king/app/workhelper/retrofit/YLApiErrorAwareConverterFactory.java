package com.king.app.workhelper.retrofit;

import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author huoguangxu
 * @since 2017/11/1.
 */

public class YLApiErrorAwareConverterFactory extends Converter.Factory {

    private final Converter.Factory mDelegateFactory;

    public YLApiErrorAwareConverterFactory(Converter.Factory delegateFactory) {
        mDelegateFactory = delegateFactory;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return mDelegateFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        final Converter<ResponseBody, ?> apiErrorConverter = mDelegateFactory.responseBodyConverter(YLApiError.class, annotations, retrofit);
        final Converter<ResponseBody, ?> delegateConverter = mDelegateFactory.responseBodyConverter(type, annotations, retrofit);
        return new Converter<ResponseBody, Object>() {
            @Override public Object convert(ResponseBody value) throws IOException {
                // read them all, then create a new ResponseBody for ApiError
                // because the response body is wrapped, 
                // we can't clone the ResponseBody correctly
                MediaType mediaType = value.contentType();
                String stringBody = value.string();
                try {
                    Object apiError = apiErrorConverter.convert(ResponseBody.create(mediaType, stringBody));
                    if (apiError instanceof YLApiError && ((YLApiError) apiError).isApiError()) {
                        throw (YLApiError) apiError;                  // 4
                    }
                } catch (JsonSyntaxException notApiError) {
                    //
                }
                // then create a new ResponseBody for normal body
                return delegateConverter.convert(ResponseBody.create(mediaType, stringBody));
            }
        };
    }
}