package com.king.applib;

import com.king.applib.log.Logger;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author VanceKing
 * @since 2018/1/18.
 */

public class PatternUtilTest extends LibBaseAndroidJUnit4Test {

    /*
    group是针对 () 来说的，group(0)就是指的整个串，group(1) 指的是第一个括号里的东西，group(2)指的第二个括号里的东西。
    group(),start(),end()所带的参数就是正则表达式中的子表达式索引（第几个子表达式）
     */
    @Test
    public void testGroup() {
        String str = "Hello,World! in Java.";
        Pattern pattern = Pattern.compile("W(or)(ld!)");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            Logger.i("得到第0组——整个匹配: " + matcher.group());
            Logger.i("得到第一组匹配——与(or)匹配的: " + matcher.group(1));
            Logger.i("得到第二组匹配——与(ld!)匹配的，组也就是子表达式 : " + matcher.group(2));
            Logger.i("总匹配的索引  Start group(): " + matcher.start(0) + " End group(): " + matcher.end(0));
            Logger.i("第一组匹配的索引  Start group(1): " + matcher.start(1) + " End group(1): " + matcher.end(1));
            Logger.i("第二组匹配的索引  Start group(2): " + matcher.start(2) + " End group(2): " + matcher.end(2));
            Logger.i("从总匹配开始索引到第1组匹配的结束索引之间子串——Wor:  " + str.substring(matcher.start(0), matcher.end(1)));
        }
    }
}
