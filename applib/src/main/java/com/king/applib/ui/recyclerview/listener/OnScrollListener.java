package com.king.applib.ui.recyclerview.listener;

/**
 * @author VanceKing
 * @since 2017/9/7.
 */

public interface OnScrollListener {

    /** 向上滑动的监听事件 */
    void onScrollUp();

    /** 向下滑动的监听事件 */
    void onScrollDown();

    /** 滑动到底部的监听事件 */
    void onBottom();

    /** 正在滚动的监听事件 */
    void onScrolled(int dx, int dy);
}
