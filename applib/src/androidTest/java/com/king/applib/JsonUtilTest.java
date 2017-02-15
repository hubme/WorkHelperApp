package com.king.applib;

import com.king.applib.log.Logger;
import com.king.applib.util.JsonUtil;

import org.json.JSONObject;

/**
 * @author huoguangxu
 * @since 2017/2/14.
 */

public class JsonUtilTest extends BaseTestCase{
    public void testGetJsonObject() throws Exception {
        String jsonText = "{\"code\":1,\"desc\":\"ok\",\"results\":{\"individual\":{\"haha\":{\"providentfund\":\"1024\",\"employment\":\"employment\"}}}}";
        JSONObject jsonObject = new JSONObject(jsonText);

        JSONObject jsonObj = JsonUtil.getJsonObject(jsonObject, "results", "individual", "haha");
        if (jsonObj != null) {
            Logger.i(jsonObj.optString("employment"));
        } else {
            Logger.i("null");
        } 
    }
}
