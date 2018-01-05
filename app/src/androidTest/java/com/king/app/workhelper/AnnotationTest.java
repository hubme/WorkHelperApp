package com.king.app.workhelper;

import com.king.app.workhelper.constant.AnnotationSample;
import com.king.applib.log.Logger;

import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @author VanceKing
 * @since 2018/1/5.
 */

public class AnnotationTest extends BaseAndroidJUnit4Test{
    @Test
    public void testMethodAnnotation() {
        try {
            Class<?> aClass = Class.forName("com.king.app.workhelper.constant.AnnotationSample");
            for (Method method : aClass.getMethods()) {
                AnnotationSample.MethodInfo methodInfo = method.getAnnotation(AnnotationSample.MethodInfo.class);
                if (methodInfo != null) {
                    Logger.i("author: "+methodInfo.author());
                    Logger.i("date: "+methodInfo.date());
                    Logger.i("version: "+methodInfo.version());
                }
            }
        } catch (Exception e) {
            Logger.e(e.toString());
        }
    }
}
