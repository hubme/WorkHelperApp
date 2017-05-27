package com.king.app.workhelper.retrofit.callback;

/**
 * @author huoguangxu
 * @since 2017/5/27.
 */

public interface OnResponseCallback<T> {
    void onSuccess(T results);

    void onFailure(int code, String msg);
}
