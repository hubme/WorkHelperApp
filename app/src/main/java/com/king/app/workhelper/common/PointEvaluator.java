package com.king.app.workhelper.common;

import android.animation.TypeEvaluator;

import com.king.app.workhelper.model.entity.Point;

/**
 * 自定义属性动画差值器.
 * see also: http://blog.csdn.net/guolin_blog/article/details/43816093
 * @author huoguangxu
 * @since 2017/3/1.
 */

public class PointEvaluator implements TypeEvaluator {
    @Override public Object evaluate(float fraction, Object startValue, Object endValue) {
        Point startPoint = (Point) startValue;
        Point endPoint = (Point) endValue;
        float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());
        float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());
        return new Point(x, y);
    }
}
