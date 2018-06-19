package com.king.app.workhelper.activity;

import android.text.TextUtils;
import android.util.Log;

/**
 * @author VanceKing
 * @since 2018/6/19.
 */
public class AAA {
    private static final String TAG = "aaa";
    
    
    private static class AStaticInnerClass {
        private String name;

        public AStaticInnerClass(String name) {
            this.name = name;
        }
    }
    
    private class AInnerClass{
        private static final String TEST = "TEST";

        private void test() {
            Log.i("aaa", TEST);
        }
        
        
    }

    private void method() {
        AStaticInnerClass aInnerClass = new AStaticInnerClass("111");
        AStaticInnerClass aInnerClass2 = new AStaticInnerClass("222");

        AAA aaa = new AAA();
        AInnerClass aInnerClass1 = aaa.new AInnerClass();
    }

    private void methodA(String name) {
        class BBB{
            private void print() {
                if (TextUtils.isEmpty(name)) {
                    Log.i("aaa", name);
                }
            }
        }
    }
}
