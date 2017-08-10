package com.king.app.workhelper.rx.rxlife;

/**
 * @author VanceKing
 * @since 2017/8/9.
 */

public interface LifecycleProvider<E> {

    <T> LifecycleTransformer<T> bindUntilEvent(E event);
}
