package com.king.app.workhelper.okhttp.converter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 自定义上传文件请求转换器。
 *
 * @author VanceKing
 * @since 2022/3/10
 */
public class FileRequestBodyConverterFactory extends Converter.Factory {
    @Nullable
    @Override
    public Converter<File, RequestBody> requestBodyConverter(@NonNull Type type, @NonNull Annotation[] parameterAnnotations,
                                                             @NonNull Annotation[] methodAnnotations, @NonNull Retrofit retrofit) {

        return new FileRequestBodyConverter();
    }

    private static class FileRequestBodyConverter implements Converter<File, RequestBody> {

        @Nullable
        @Override
        public RequestBody convert(@NonNull File value) throws IOException {
            return RequestBody.create(MediaType.parse("application/otcet-stream"), value);
        }
    }
}
