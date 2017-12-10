package com.king.applib.mvp;

/**
 * @author VanceKing
 * @since 2017/12/10.
 */

public interface BaseView<T> {
    void setPresenter(T presenter);
}
