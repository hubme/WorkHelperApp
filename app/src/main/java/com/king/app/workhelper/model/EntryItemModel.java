package com.king.app.workhelper.model;

/**
 * @author VanceKing
 * @since 2017/6/3.
 */

public class EntryItemModel {
    /** 动作类型 0为打开网页，1为打开本地页面. */
    public int type;
    /** 1是检测是否登陆 0不检测. */
    public int logincheck;
    /** 入口名称. */
    public String title;
    /** 入口图标. */
    public String img;
    /** 入口地址. */
    public String target;
    /** 辅助参数 当type参数为0时，此参数起作用. */
    public String param;

    @Override
    public String toString() {
        return "EntryItemModel{" +
                "type=" + type +
                ", logincheck=" + logincheck +
                ", title='" + title + '\'' +
                ", img='" + img + '\'' +
                ", target='" + target + '\'' +
                ", param='" + param + '\'' +
                '}';
    }
}
