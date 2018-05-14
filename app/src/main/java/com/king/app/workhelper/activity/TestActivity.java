package com.king.app.workhelper.activity;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

public class TestActivity extends AppBaseActivity {

    private AsyncTask<String, Void, Integer> asyncTask;

    @Override protected void initData() {
        super.initData();
        new Thread() {
            @Override public void run() {
                super.run();
                asyncTask = new MyAsyncTask();
            }
        }.start();

    }

    @BindView(R.id.tv_open_qq) TextView mTestTv;

    @Override protected int getContentLayout() {
        return R.layout.activity_test;
    }

    @Override protected void initContentView() {
        super.initContentView();
    }

    @OnClick(R.id.tv_open_qq)
    public void onTestViewClick(CheckedTextView textView) {
        textView.toggle();
        if (textView.isChecked()) {
//            asyncTask.executeOnExecutor()
            asyncTask.execute("");
        } else {
            asyncTask.cancel(true);
            try {
                logMessage("canceled result: " + asyncTask.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private static class MyAsyncTask extends AsyncTask<String, Void, Integer> {
        public MyAsyncTask() {
            super();
        }

        @Override protected void onPreExecute() {
            super.onPreExecute();
            logMessage("onPreExecute");
        }

        @Override protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            logMessage("onPostExecute。result: " + integer);
        }

        @Override protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            logMessage("onProgressUpdate。");
        }

        @Override protected void onCancelled(Integer integer) {
            super.onCancelled(integer);
            logMessage("onCancelled。" + integer.toString());
        }

        @Override protected void onCancelled() {
            super.onCancelled();
            logMessage("onCancelled");
        }

        @Override protected Integer doInBackground(String... strings) {
            for (String param : strings) {
                logMessage(param);
            }
            if (isCancelled()) {
                return -2;
            } else {
                //耗时任务，判断是否取消，中断线程
                SystemClock.sleep(5000);
                return 1;
            }
        }
    }

}
