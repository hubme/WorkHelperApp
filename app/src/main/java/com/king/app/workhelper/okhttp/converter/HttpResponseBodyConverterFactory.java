package com.king.app.workhelper.okhttp.converter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 自定义响应转换器。
 *
 * @author VanceKing
 * @since 2022/3/10
 */
public class HttpResponseBodyConverterFactory extends Converter.Factory {
    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(@NonNull Type type, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {
        return new ArbitraryResponseBodyConverter();
    }

    private static class ArbitraryResponseBodyConverter implements Converter<ResponseBody, Result> {

        @Nullable
        @Override
        public Result convert(@NonNull ResponseBody value) throws IOException {
            RawResult rawResult = new Gson().fromJson(value.toString(), RawResult.class);
            return new Result(rawResult.err, rawResult.content, rawResult.message);
        }
    }

    private static class Result {
        private final int code;
        private final String content;
        private final String message;

        public Result(int code, String content, String message) {
            this.code = code;
            this.content = content;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getContent() {
            return content;
        }

        public String getMessage() {
            return message;
        }
    }

    private static class RawResult {
        private int err;
        private String content;
        private String message;
    }
}
