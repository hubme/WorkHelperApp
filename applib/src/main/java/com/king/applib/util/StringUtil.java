package com.king.applib.util;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Pattern;

/**
 * 字符串工具类
 * Created by HuoGuangxu on 2016/9/29.
 */

public class StringUtil {
    private StringUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    /**
     * 判断字符串是否是null或者empty(""," ")
     */
    public static boolean isNullOrEmpty(final String string) {
        return string == null || string.trim().isEmpty();
    }

    /**
     * 判断字符串是否 not null或者not empty(""," ")
     */
    public static boolean isNotEmpty(final String string) {
        return !isNullOrEmpty(string);
    }

    /**
     * 如果可变字符串中包含null或empty,返回true;否则返回false.
     */
    public static boolean isAnyEmpty(final String... strings) {
        if (strings == null || strings.length < 1) {
            return true;
        }
        for (String str : strings) {
            if (isNullOrEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 字符串都不为null或""返回true,否则返回false.
     */
    public static boolean isNoneEmpty(final String... strings) {
        return !isAnyEmpty(strings);
    }

    /**
     * 判断字符串是否为数字
     * @param str 传入的字符串
     * @return 是整数返回true, 否则返回false
     */
    public static boolean isNumber(final String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 创建一个Spannable对象
     */
    public static Spannable createSpannable(String string) {
        return new SpannableString(string);
    }

    /**
     * FOR: textview.setText(WordtoSpan)
     * Textview文字设置俩种不同颜色
     * @param tag 从哪个索引开始分割
     */
    private static Spannable setTextColor(Context context, String string, int tag, int colorFirstId, int colorSecondId) {
        Spannable WordtoSpan = createSpannable(string);
        WordtoSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(colorFirstId)), 0, tag, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        WordtoSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(colorSecondId)), tag, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return WordtoSpan;
    }

    /**
     * 设置字符串中部分串的字体颜色
     */
    public static void setTextColor(Spannable sp, int color, int start, int end) {
        if (sp != null) {
            sp.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    /**
     * 设置字符串中部分串的字体大小
     */
    public static void setTextSize(Spannable sp, int textSize, int start, int end) {
        if (sp != null) {
            //设置字体大小,第二个参数boolean dip，如果为true，表示前面的字体大小单位为dip，否则为像素，
            sp.setSpan(new AbsoluteSizeSpan(textSize, true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    /**
     * 设置不同的样式
     */
    public static void setTextStyle(Spannable sp, int textStyle, int start, int end) {
        if (sp != null) {
            sp.setSpan(new StyleSpan(textStyle), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    /**
     * FOR:
     * SpannableString spStr = new SpannableString(str);
     * SpannableUtils.setTextClickable(tv_1, spStr, start, end);
     * tv_1.setText(spStr);
     * 设置字符串的可点击部分
     */
    public static void setTextClickable(TextView textView, Spannable sp, int start, int end) {
        if (sp != null) {
            sp.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.BLUE);       //设置文件颜色
                    ds.setUnderlineText(true);      //设置下划线
                }

                @Override
                public void onClick(View widget) {
                    // do somthing...
                }
            }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
            //textView.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
        }
    }

    /**
     * 设置中划线
     */
    public static void setTextMiddleLine(Spannable sp, int start, int end) {
        if (sp != null) {
            StrikethroughSpan stSpan = new StrikethroughSpan();  //设置删除线样式
            sp.setSpan(stSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
    }
}
