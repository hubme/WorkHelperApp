package com.king.app.workhelper.retrofit.model;

/**
 * 统一接口返回格式
 *
 * @author VanceKing
 * @since 2017/5/27.
 */

public class HttpResults<T> {
    public int code;
    public String desc;
    public T results;
}
