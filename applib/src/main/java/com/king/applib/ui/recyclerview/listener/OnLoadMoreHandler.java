package com.king.applib.ui.recyclerview.listener;

public interface OnLoadMoreHandler {
    void onLoading();

    void onLoadError(int code, String desc);

    void onLoadComplete();
    
    void onNoMoreData(String desc);
}