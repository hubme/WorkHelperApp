package com.king.applib;

import com.king.applib.log.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author VanceKing
 * @since 2017/4/13 0013.
 */

public class ACacheTest extends BaseTestCase {
    public static final String JSON_ARRAY = "[\"求助\",\"讨论\",\"政策\"]";
    public static final String JSON_OBJECT = "{\"name\":[\"aaa\",\"bbb\"],\"age\":\"11\"}";
    private ACache mACache;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
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
        mACache.put("array",array, ACache.TIME_DAY);
        
    }
}
