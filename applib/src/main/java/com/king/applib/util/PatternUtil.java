package com.king.applib.util;

import android.text.TextUtils;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author VanceKing
 * @since 2018/1/18.
 */

public class PatternUtil {
    /** 正则表达式需要转义的字符，当试图匹配这些字符时会报 java.util.regex.PatternSyntaxException */
    public static final String[] ESC_CHAR = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
    public static final String ESC_REGX = "[\\\\$()*+.\\[\\]?^{}|]";

    private PatternUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    /** 返回转义后的 Pattern，避免 PatternSyntaxException */
    public static Pattern convertPattern(String keyword) {
        if (keyword == null || TextUtils.isEmpty(keyword.trim())) {
            return null;
        }
        String temp = keyword;
        if (containPatternChar(temp)) {
            for (String key : ESC_CHAR) {
                if (temp.contains(key)) {
                    temp = temp.replace(key, "\\" + key);
                }
            }
        }
        try {
            return Pattern.compile(temp);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /** 是否包含转义字符 */
    public static boolean containPatternChar(String keyword) {
        if (StringUtil.isNullOrEmpty(keyword)) {
            return false;
        }
        Pattern compile = Pattern.compile(ESC_REGX);
        return compile.matcher(keyword).matches();
    }

    public static Pattern convertPattern2(String keyword) {
        if (StringUtil.isNullOrEmpty(keyword)) {
            return null;
        }
        Pattern compile = Pattern.compile(ESC_REGX);
        Matcher matcher = compile.matcher(keyword);
        String temp = keyword;
        while (matcher.find()) {
            String group = matcher.group();
            Log.i("aaa", "group(): " + group);
            temp = temp.replace(group, "\\" + group);
        }
        return Pattern.compile(temp);
    }
}
