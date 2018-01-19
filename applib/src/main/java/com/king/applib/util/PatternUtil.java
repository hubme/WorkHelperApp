package com.king.applib.util;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * 正则表达式相关工具类。
 *
 * @author VanceKing
 * @since 2018/1/18.
 */

public class PatternUtil {
    /** 正则表达式需要转义的字符，当试图匹配这些字符时会报 java.util.regex.PatternSyntaxException */
    public static final String[] ESC_CHAR = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"};
    public static final String ESC_TEXT = "\\$()*+.[]?^{}|";
    /** 是否包含正则特殊字符的正则表达式，当试图匹配这些字符时会报 java.util.regex.PatternSyntaxException */
    //1.至少包含一个特殊字符 2. 可以包含其他字符
    public static final String ESC_REGEX = "^.*[\\\\?^|$*+.()\\[\\]{}]+.*$";

    private PatternUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    /** 是否包含正则表达式的特殊字符 */
    public static boolean containESCChar(String keyword) {
        return !StringUtil.isNullOrEmpty(keyword) && keyword.matches(ESC_REGEX);
    }

    /** 当 keyword 包含 {@link #ESC_TEXT} 时会报 PatternSyntaxException。把 keyword 中的特殊字符进行转义(每个特殊字符前添加\\) */
    public static Pattern convertPattern(String keyword) {
        if (keyword == null || TextUtils.isEmpty(keyword.trim())) {
            return null;
        }
        //先判断是否包含特殊字符，避免进入循环查找
        if (containESCChar(keyword)) {
            for (String key : ESC_CHAR) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        try {
            //可能抛出 PatternSyntaxException
            return Pattern.compile(keyword);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
