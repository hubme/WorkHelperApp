package com.king.app.workhelper.model.entity;

/**
 * @author huoguangxu
 * @since 2017/3/1.
 */

public class Point {
    private float x;

    private float y;

    public Point() {
    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
