package com.king.app.workhelper.fragment;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.log.Logger;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * LeakCanary使用
 * see also:https://github.com/square/leakcanary,https://www.liaohuqiu.net/cn/posts/leak-canary-read-me/
 * Created by VanceKing on 2016/12/2 0002.
 */
/*
    LeakCanary工作机制:
    1.RefWatcher.watch() 创建一个 KeyedWeakReference 到要被监控的对象。
    2.然后在后台线程检查引用是否被清除，如果没有，调用GC。
    3.如果引用还是未被清除，把 heap 内存 dump 到 APP 对应的文件系统中的一个 .hprof 文件中。
    4.在另外一个进程中的 HeapAnalyzerService 有一个 HeapAnalyzer 使用HAHA 解析这个文件。
    5.得益于唯一的 reference key, HeapAnalyzer 找到 KeyedWeakReference，定位内存泄露。
    6.HeapAnalyzer 计算 到 GC roots 的最短强引用路径，并确定是否是泄露。如果是的话，建立导致泄露的引用链。
    7.引用链传递到 APP 进程中的 DisplayLeakService， 并以通知的形式展示出来。
*/
public class LeakCanaryFragment extends AppBaseFragment {
    @BindView(R.id.tv_leak_canary)
    public TextView textView;

    //1.非静态内部类导致的Activity泄漏
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Logger.i("哈哈哈");
                    mHandler.sendEmptyMessageDelayed(0, 1000);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_leak_canary;
    }

    @Override
    protected void initData() {
        super.initData();

        //2.静态方法导致的Activity泄漏
//        LeakTestModel.getInstance().setRetainedTextView(textView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @OnClick(R.id.tv_leak_canary)
    public void onClick() {
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }
}
