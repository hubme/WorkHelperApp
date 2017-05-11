package com.king.applib;

import com.king.applib.log.Logger;
import com.king.applib.util.ExtendUtil;
import com.king.applib.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static com.king.applib.util.JsonUtil.decodeToList;
import static com.king.applib.util.JsonUtil.getJsonObject;

/**
 * @author huoguangxu
 * @since 2017/2/14.
 */

public class JsonUtilTest extends BaseTestCase {
    public static final String JSON_ARRAY = "[\"求助\",\"讨论\",\"政策\"]";
    public static final String AAA = "{\n" +
            "  \"code\": 1,\n" +
            "  \"results\": {\n" +
            "    \"loanidnex\": [\n" +
            "      {\n" +
            "        \"logoUrl\": \"http://192.168.1.51:9007/gjj/image/home/skin/chexian/gjj_banner20170210_daijiao.png\",\n" +
            "        \"largestAmount\": \"50万\",\n" +
            "        \"supplierCode\": 1,\n" +
            "        \"supplierName\": \"有鱼贷款\",\n" +
            "        \"description\": [\n" +
            "          \"额度高\",\n" +
            "          \"放款快\",\n" +
            "          \"利息低\"\n" +
            "        ],\n" +
            "        \"h5Url\": \"http://www.baidu.com\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"creditidnex\": [\n" +
            "      {\n" +
            "        \"cardUrl\": \"http://192.168.1.51:9007/gjj/image/home/skin/chexian/gjj_banner20170210_daijiao.png\",\n" +
            "        \"largestAmount\": \"30万\",\n" +
            "        \"applyAmount\": \"1200\",\n" +
            "        \"cardCode\": 1,\n" +
            "        \"cardName\": \"浦发银联梦之白金卡\",\n" +
            "        \"h5Url\": \"http://www.baidu.com\",\n" +
            "        \"description\": [\n" +
            "          \"大额现金分期\",\n" +
            "          \"白金卡\",\n" +
            "          \"卡组织\"\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"desc\": \"ok\"\n" +
            "}";


    public void testGetJsonObject() throws Exception {
        String jsonText = "{\"code\":1,\"desc\":\"ok\",\"results\":{\"individual\":{\"haha\":{\"providentfund\":\"1024\",\"employment\":\"employment\"}}}}";
        JSONObject jsonObject = new JSONObject(jsonText);

        JSONObject jsonObj = getJsonObject(jsonObject, "results", "individual", "haha");
        if (jsonObj != null) {
            Logger.i(jsonObj.optString("employment"));
        } else {
            Logger.i("null");
        }
    }

    public void testDecodeToListArray() throws Exception {
        JSONArray jsonArray = new JSONArray(JSON_ARRAY);
        List<String> strings = decodeToList(jsonArray, String.class);
        ExtendUtil.printList(strings);

    }

    public void testAAA() throws Exception {
        JSONObject jsonObject = new JSONObject(AAA);
        JSONObject results = JsonUtil.getJsonObject(jsonObject, "results");
        JSONArray loanidnex = JsonUtil.getJsonArray(results, "loanidnex");
        JSONObject item = (JSONObject) loanidnex.get(0);
        JSONArray description = JsonUtil.getJsonArray(item, "description");
        List<String> texts = JsonUtil.decodeToList(description, String.class);
        ExtendUtil.printList(texts);
    }
}
