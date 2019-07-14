package com.king.applib;

import com.king.applib.log.Logger;
import com.king.applib.model.Student;
import com.king.applib.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VanceKing
 * @since 2017/4/13 0013.
 */

public class ACacheTest extends BaseTestCase {
    public static final String JSON_ARRAY = "[\"求助\",\"讨论\",\"政策\"]";
    public static final String JSON_OBJECT = "{\"name\":[\"aaa\",\"bbb\"],\"age\":\"11\"}";
    private ACache mACache;

    @Before
    public void init() {
        mACache = ACache.get();
    }

    public void testPutJsonObject() throws Exception {
        JSONObject jsonObject = new JSONObject(JSON_OBJECT);
        ACache.get().put("aaabbb", jsonObject, 60);
    }

    public void testGetJsonObject() throws Exception {
        JSONObject aaabbb = ACache.get().getAsJSONObject("aaabbb");
        if (aaabbb != null) {
            Logger.i(aaabbb.getString("name"));
        }
    }

    public void testPutString() throws Exception {
        mACache.put("aaa", "aaa");
    }

    public void testGetString() throws Exception {
        Logger.i(mACache.getAsString("aaa"));
    }

    public void testPutStringWithTimeout() throws Exception {
        mACache.put("aaa", "aaa", 5);
    }

    public void testGetStringWithTimeout() throws Exception {
        Logger.i(mACache.getAsString("aaa"));
    }

    public void testPutJsonArray() throws Exception {
        JSONArray array = new JSONArray(JSON_ARRAY);
        mACache.put("array", array, ACache.TIME_DAY);
    }

    public void testPutObject() throws Exception {
        mACache.put("object", new Student("VanceKing", 28), ACache.TIME_HOUR);
    }

    public void testGetObject() throws Exception {
        Student student = (Student) mACache.getAsObject("object");
        Logger.i(student.toString());
    }

    public void testPutListObject() throws Exception {
        List<Student> students = new ArrayList<>();
        students.add(new Student("aaa", 20));
        students.add(new Student("bbb", 28));

        String str = JsonUtil.encode(students);
        ACache.get().put("obj_list", str, ACache.TIME_HOUR);

        String aaa = ACache.get().getAsString("obj_list");
        List<Student> students1 = JsonUtil.decodeToList(aaa, Student.class);
        printList(students1);
        
    }
}
