package com.king.applib;

import com.king.applib.log.Logger;
import com.king.applib.util.StringUtil;

import java.util.Arrays;
import java.util.Locale;
import java.util.StringJoiner;

/**
 * StringUtils测试类
 * Created by VanceKing on 2016/11/30.
 */

public class StringUtilsTest extends BaseTestCase {

    public void testStringformat() throws Exception {
        Logger.i(String.format(Locale.US, "%,.2f", 1234526.123f));
    }

    public void testArraysCopy() throws Exception {
        String[] texts = new String[]{"aaa", "bbb", "ccc"};
        String[] textss = Arrays.copyOf(texts, texts.length);
        printArray(textss);
    }

    public void testStringLength() throws Exception {
        Logger.i(String.valueOf("".length()));
        Logger.i(String.valueOf("哈".length()));
        Logger.i(String.valueOf("h".length()));
        Logger.i(String.valueOf("哈哈".length()));
        Logger.i(String.valueOf("hh".length()));
        Logger.i(String.valueOf(" ".length()));
        Logger.i(String.valueOf("0".length()));
        Logger.i(String.valueOf(".".length()));
        Logger.i(String.valueOf("&".length()));
    }

    public void testIsNotEmpty() throws Exception {
        String text = null;
        Logger.i(String.valueOf(StringUtil.isNotEmpty(text)));
        Logger.i(String.valueOf(StringUtil.isNotEmpty("")));
        Logger.i(String.valueOf(StringUtil.isNotEmpty(" ")));
        Logger.i(String.valueOf(StringUtil.isNotEmpty("000")));
    }

    public void testNoneEmpty() throws Exception {
        String text = null;
        Logger.i(String.valueOf(StringUtil.isNoneEmpty()));
        Logger.i(String.valueOf(StringUtil.isNoneEmpty("")));
        Logger.i(String.valueOf(StringUtil.isNoneEmpty(" ")));
        Logger.i(String.valueOf(StringUtil.isNoneEmpty(text)));
        Logger.i(String.valueOf(StringUtil.isNoneEmpty("0a")));
        Logger.i(String.valueOf(StringUtil.isNoneEmpty("0a", "")));
        Logger.i(String.valueOf(StringUtil.isNoneEmpty("0a", text)));
        Logger.i(String.valueOf(StringUtil.isNoneEmpty("0a", "ab")));
    }

    public void testStringBuffer() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("aaa").append("bbb");
        Logger.i(sb.toString());

        sb.delete(0, sb.length());
        Logger.i("results: " + sb.toString());

        sb.append("111");
        Logger.i(sb.toString());
    }

    public void testConcat() throws Exception {
        String text = "";
        Logger.i(text.concat("aaa").concat("bbb"));
//        Logger.i(text.concat(null).concat("bbb"));
        String text2 = null;
        Logger.i(text2 + "uuu");

        Logger.i(StringUtil.concat(false, "0", " ", "a", null, " b c"));
        Logger.i(StringUtil.concat(true, "0", "     ", "a", null, " b c "));
    }


    public void testStringFormatter() throws Exception {
        float aFloat = 0;
        Logger.i(String.format(Locale.US, "%,.2f", aFloat));
    }

    public void testTrimAllSpace() throws Exception {
        Logger.i("result: " + StringUtil.trimAllSpace(null));
        Logger.i("result: " + StringUtil.trimAllSpace(""));
        Logger.i("result: " + StringUtil.trimAllSpace(" a "));
        Logger.i("result: " + StringUtil.trimAllSpace(" a a "));
        Logger.i("result: " + StringUtil.trimAllSpace("0 1  a\tb\rc\nd "));
    }
    
    public void testReplaceAllSpace() throws Exception {
        Logger.i(StringUtil.replaceAllSpace("0 1", "&"));
        Logger.i(StringUtil.replaceAllSpace("0 1    2\r3\n4", "&"));
    }
    
    //String#trim() 返回新的字符串对象，原字符串对象不受影响
    public void testStringTrim() throws Exception {
        String text = " AAA ";
        Logger.i(text);
        Logger.i(text.trim());
        Logger.i(text);
    }
    
    //java 1.8+
    public void testStringJoiner() throws Exception {
        StringJoiner stringJoiner = new StringJoiner("|", "#", "$");
        stringJoiner
                .add("Hello")
                .add("Vance")
                .add("King");
        Logger.i(stringJoiner.toString());
    }
}
