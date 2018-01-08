package com.king.app.workhelper.constant;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorRes;
import android.support.annotation.IntDef;
import android.support.annotation.RequiresPermission;
import android.support.annotation.StringDef;

import com.annotation.sample.PrintMe;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Android Annotation是以Support Library的形式给我们提供的，从安卓19.1开始出现.
 * 安卓注解有8种类型，分别是Nullness注解、资源类型注解、线程注解、
 * 变量限制注解、权限注解、结果检查注解、CallSuper注解、枚举注解(IntDef和StringDef)。
 * Created by VanceKing on 2016/10/16 0016.
 */
@PrintMe
public class AnnotationSample {
    @PrintMe
    private static final String TAG = "AnnotationSample";

    @IntDef({SORT_TYPE.ASC, SORT_TYPE.DESC})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SORT_TYPE {
        int ASC = 0;
        int DESC = 1;
    }

    private int day;

    public static final int MON = 1;
    public static final int TUE = 2;
    public static final int WED = 3;
    public static final int THU = 4;
    public static final int FRI = 5;
    public static final int SAT = 6;
    public static final int SUN = 7;

    @IntDef({MON, TUE, WED, THU, FRI, SAT, SUN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface WeakDay {

    }

    public static final String POWER_SERVICE = "power";
    public static final String WINDOW_SERVICE = "window";
    public static final String LAYOUT_INFLATER_SERVICE = "layout_inflater";

    @StringDef({POWER_SERVICE, WINDOW_SERVICE, LAYOUT_INFLATER_SERVICE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ServiceName {
    }

    public void getSystemService(@ServiceName String name) {

    }

    @PrintMe
    @WeakDay
    public int getColor() {
        return MON;
    }

    public void setColor(@WeakDay int day) {
        this.day = day;
    }

    @MethodInfo(author = "Vance", date = "2018.1.5")
    public void setCurColor(Context context, @ColorRes int color) {
        context.getResources().getColor(color);
    }

    //权限注解
    @RequiresPermission(Manifest.permission.SET_WALLPAPER)
    public void setWallPaper(Bitmap bitmap) throws Exception {

    }

    //CallSuper说明所有重写onCreate()方法的方法都要有super.onCreate();
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {

    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @Inherited
    public @interface MethodInfo {
        String author() default "VanceKing";

        String date();

        int version() default 1;
    }
}
