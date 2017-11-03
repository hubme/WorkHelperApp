package com.king.applib;

import com.king.applib.log.Logger;
import com.king.applib.model.Student;
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
    public static final String JSON_ARRAY = "[\"北京\",\"上海\",\"广州\"]";

    @Override protected void setUp() throws Exception {
        super.setUp();
    }

    public static class JsonTestModel {
        public int code;
        public float codeFloat;
        public double codeDouble;
        public String uniqueId;
        public String results;
        public String desc;
        public Student student;

        @Override public String toString() {
            return "JsonTestModel{" +
                    "code=" + code +
                    ", codeFloat=" + codeFloat +
                    ", codeDouble=" + codeDouble +
                    ", uniqueId='" + uniqueId + '\'' +
                    ", results='" + results + '\'' +
                    ", desc='" + desc + '\'' +
                    ", student=" + student +
                    '}';
        }
    }


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
        Logger.i("str: " + str);
    }

    public void testEncodeListInteger() throws Exception {
        List<Integer> integers = new ArrayList<>();
        integers.add(10);
        integers.add(99);
        integers.add(0);

        String str = JsonUtil.encode(integers);
        Logger.i("str: " + str);
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
        printList(strings);

    }

    public void testFormatPrettyJson() throws Exception {
        final String uglyJSONString = "{\"code\":1,\"results\":{\"nearbyOrgList\":[{\"corgname\":\"上海市\",\"ccitycode\":\"021\"}],\"list\":[{\"sectiontitle\":\"安徽省\",\"sectionorgs\":[{\"corgname\":\"安徽省直\",\"ccitycode\":\"05512\"},{\"corgname\":\"安徽省直\",\"ccitycode\":\"05512\"}]}]},\"desc\":\"查询城市列表成功\"}";
        String prettyJson = JsonUtil.formatPrettyJson(uglyJSONString);
        Logger.i("prettyJson: " + prettyJson);
    }

    public void testNullTest() throws Exception {
        String text = "{\"code\":1,\"codeFloat\":1024,\"codeDouble\":1024.1024,\"results\":\"results\",\"desc\":\"desc\",\"uniqueId\":\"aaabbbccc\",\"student\":null}";
        JsonTestModel model = JsonUtil.decode(text, JsonTestModel.class);
        Logger.i("results: " + model.toString());

        String text2 = "{\"code\":null,\"codeFloat\":null,\"codeDouble\":null,\"results\":null,\"desc\":null,\"uniqueId\":null,\"student\":null}";
        JsonTestModel model1 = JsonUtil.decode(text2, JsonTestModel.class);
        Logger.i("results: " + model1.toString());
    }
}
