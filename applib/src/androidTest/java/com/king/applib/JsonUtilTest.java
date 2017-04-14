package com.king.applib;

import com.king.applib.log.Logger;
import com.king.applib.util.ExtendUtil;
import com.king.applib.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * @author huoguangxu
 * @since 2017/2/14.
 */

public class JsonUtilTest extends BaseTestCase{
    public static final String JSON_ARRAY = "[\"求助\",\"讨论\",\"政策\"]";
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
    
    public void testDecodeToListArray() throws Exception {
        JSONArray jsonArray = new JSONArray(JSON_ARRAY);
        List<String> strings = JsonUtil.decodeToList(jsonArray, String.class);
        ExtendUtil.printList(strings);
        
    }
}
