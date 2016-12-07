package com.king.app.workhelper.fragment;

import android.util.Log;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.log.Logger;
import com.king.applib.util.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Log测试
 * Created by HuoGuangxu on 2016/12/6.
 */

/**
 * StackTrace简述以及StackTraceElement使用实例
 * StackTrace简述
 * 1 StackTrace用栈的形式保存了方法的调用信息.
 * 2 怎么获取这些调用信息呢?
 * 可用Thread.currentThread().getStackTrace()方法
 * 得到当前线程的StackTrace信息.
 * 该方法返回的是一个StackTraceElement数组.
 * 3 该StackTraceElement数组就是StackTrace中的内容.
 * 4 遍历该StackTraceElement数组.就可以看到方法间的调用流程.
 * 比如线程中methodA调用了methodB那么methodA先入栈methodB再入栈.
 * 5 在StackTraceElement数组下标为2的元素中保存了当前方法的所属文件名,当前方法所属
 * 的类名,以及该方法的名字.除此以外还可以获取方法调用的行数.
 * 6 在StackTraceElement数组下标为3的元素中保存了当前方法的调用者的信息和它调用
 * 时的代码行数.
 * 示例说明:
 * 1 methodA()调用methodB()
 * methodB()调用methodC()
 * 2 在methodC()中获取StackTrace中的内容并遍历StackTraceElement数组
 * 这样就能观察到从开始到现在的方法间调用流程.
 * 在该流程中可以观察到:
 * StackTraceElement数组下标为2的元素中保存了当前方法的所属文件名,
 * 当前方法所属的类名,以及该方法的名字.
 * 除此以外还可以利用stackTraceElement.getLineNumber()获取调用getStackTrace()方法的行数.
 * 在StackTraceElement数组下标为3的元素中保存了当前方法的调用者的信息.
 * 并且可以还可以利用stackTraceElement.getLineNumber()获取到调用时的代码行数.
 * 注意此时获取到的不再是调用getStackTrace()方法的行数.
 * 3 methodC()调用methodD()
 * 在methodD()中获取StackTraceElement数组下标为2和3的元素信息.
 * 这两个元素包含了对于代码调试的重要信息.所以在此单独获取查看.
 */
public class LogFragment extends AppBaseFragment {
    public static final String TAG = LogFragment.class.getSimpleName();

    @BindView(R.id.tv_origin_log)
    public TextView mOriginLogTv;

    @BindView(R.id.tv_log)
    public TextView mLogTv;
    private LogUtil mLog;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_log;
    }

    @Override
    protected void initData() {
        super.initData();

        mLog = new LogUtil(true, TAG);
    }

    @OnClick(R.id.tv_trace)
    public void onTraceClick() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        Log.i(TAG, "stackTrace's length is " + stackTrace.length);
        for (int i = 0; i < stackTrace.length; i++) {

            String threadName = Thread.currentThread().getName();
            long threadID = Thread.currentThread().getId();

            StackTraceElement trace = stackTrace[i];
            String className = trace.getClassName();
            String methodName = trace.getMethodName();
            String fileName = trace.getFileName();
            int lineNumber = trace.getLineNumber();

            Log.i(TAG, "StackTraceElement数组下标 i=" + i + ",threadID=" + threadID + ",threadName=" + threadName + ",fileName="
                    + fileName + ",className=" + className + ",methodName=" + methodName + ",lineNumber=" + lineNumber);
        }
    }

    @OnClick(R.id.tv_origin_log)
    public void onOriginLogTvClick() {
        //IDE支持行号跳转，但要符合一定格式。"(简单类名:行号)"
        Log.i(TAG, "(LogFragment.java:39)");//可跳转
        Log.i(TAG, "(com.king.app.workhelper.fragment.LogFragment.java:40)");//不可跳转
    }

    @OnClick(R.id.tv_log)
    public void onClickLog() {
        /*StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement trace = stackTrace[2];
        Log.i(TAG, trace.getClassName());
        Log.i(TAG, trace.getMethodName());
        Log.i(TAG, trace.getFileName());
        Log.i(TAG, trace.getLineNumber() + "");

        StringBuilder sb = new StringBuilder();
        sb.append("(").append(trace.getFileName()).append(":").append(trace.getLineNumber()).append(")");*/
        Log.i(TAG, buildLogMsg("哈哈"));

        Logger.i("呵呵呵");


    }

    private static String buildLogMsg(String msg) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace == null || stackTrace.length < 3) {
            return "";
        }
        StackTraceElement trace = stackTrace[2];
        StringBuilder sb = new StringBuilder();
        sb.append("(")
                .append(trace.getFileName())
                .append(":")
                .append(stackTrace[3].getLineNumber())
                .append(")")
                .append("\n")
                .append(msg);
        return sb.toString();
    }


}
