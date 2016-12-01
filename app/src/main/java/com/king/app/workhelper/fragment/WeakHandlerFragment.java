package com.king.app.workhelper.fragment;

import android.os.Message;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.base.WeakHandler;
import com.king.applib.log.Logger;

import butterknife.OnClick;

/**
 * WeakHandler
 * Created by HuoGuangxu on 2016/12/1.
 */

public class WeakHandlerFragment extends AppBaseFragment {
    private static final int MSG_WHAT = 0;

    private MyHandler mMyHandler = new MyHandler(this);

    private static class MyHandler extends WeakHandler<WeakHandlerFragment> {
        public MyHandler(WeakHandlerFragment target) {
            super(target);
        }

        @Override public void handle(WeakHandlerFragment target, Message msg) {
            switch (msg.what) {
                case MSG_WHAT:
                    Logger.i("哈哈哈哈");
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    protected int getContentLayout() {
        return R.layout.fragment_weak_handler;
    }

    @OnClick(R.id.tv_weak_handler)
    public void clickWeakHandler() {
        mMyHandler.sendEmptyMessage(MSG_WHAT);
    }
}
