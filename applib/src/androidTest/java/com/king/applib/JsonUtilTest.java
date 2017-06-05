package com.king.applib;

import com.king.applib.log.Logger;
import com.king.applib.model.Student;
import com.king.applib.util.ExtendUtil;
import com.king.applib.util.JsonUtil;
import com.king.applib.util.SPUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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
    
    public void testEncode() throws Exception {
        Student student = new Student("VanceKing", 28);
        Logger.i(JsonUtil.encode(student));

        Student decode = JsonUtil.decode("{\"name\":\"VanceKing\",\"age\":28}", Student.class);
        Logger.i(decode.toString());
    }
    
    public void testEncodeListObject() throws Exception {
        List<Student> students = new ArrayList<>();
        students.add(new Student("aaa", 20));
        students.add(new Student("bbb", 28));

        String str = JsonUtil.encode(students);
        Logger.i("str: "+str);
    }

    public void testEncodeListInteger() throws Exception {
        List<Integer> integers = new ArrayList<>();
        integers.add(10);
        integers.add(99);
        integers.add(0);

        String str = JsonUtil.encode(integers);
        Logger.i("str: "+str);
    }
    
    public void testPutObject() throws Exception {
        Student student = new Student("VanceKing", 28);
        SPUtil.putObject("aaa", student);

        Student aaa = SPUtil.getObject("aaa", Student.class);
        Logger.i(aaa.toString());
    }


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
