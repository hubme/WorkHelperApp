package com.king.applib.builder;

import com.king.applib.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huoguangxu
 * @since 2017/6/5.
 */

public class HttpParamsBuilder {
    public final Map<String, String> PARAMS_MAP;

    private HttpParamsBuilder() {
        PARAMS_MAP = new HashMap<>();
    }

    public static HttpParamsBuilder create() {
        return new HttpParamsBuilder();
    }

    public HttpParamsBuilder addDefaultParams() {
        PARAMS_MAP.put("releaseVersion", "1.0.0");
        PARAMS_MAP.put("source", "10000");
        PARAMS_MAP.put("addressCode", "021");

        final String appId = "";
        final String accessToken = "";

        if (StringUtil.isNoneEmpty(appId, accessToken)) {
            PARAMS_MAP.put("app_id", appId);
            PARAMS_MAP.put("token", accessToken);
        }
        return this;
    }
    
    public HttpParamsBuilder add(String key, String value) {
        if (StringUtil.isNoneEmpty(key, value)) {
            PARAMS_MAP.put(key, value);
        }
        return this;
    }

    public Map<String, String> build() {
        addDefaultParams();
        return PARAMS_MAP;
    }
}
