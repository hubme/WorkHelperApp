package com.king.applib;

import com.king.applib.log.Logger;

import org.json.JSONArray;

/**
 * @author VanceKing
 * @since 2017/4/13 0013.
 */

public class ACacheTest extends BaseTestCase {
    public static final String JSON_ARRAY = "[\"求助\",\"讨论\",\"政策\"]";
    private ACache mACache;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mACache = ACache.get();
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
