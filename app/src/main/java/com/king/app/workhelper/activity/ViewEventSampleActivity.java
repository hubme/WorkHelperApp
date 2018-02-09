package com.king.app.workhelper.activity;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.app.AppConfig;
import com.king.app.workhelper.common.AppBaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 事件传递机制.
 * 1.正常传递，无消费情况。Activity#dispatchTouchEvent -> Activity#onUserInteraction -> ViewGroup#dispatchTouchEvent -> ViewGroup#onInterceptTouchEvent ->
 * View#dispatchTouchEvent -> View#onTouch(mView) -> View#onTouchEvent -> View#onTouch(mViewGroup) -> ViewGroup#onTouchEvent -> Activity#onTouchEvent
 *
 * @author VanceKing
 * @since 2017/4/17.
 */

public class ViewEventSampleActivity extends AppBaseActivity {
    @BindView(R.id.view_group) ViewGroup mViewGroup;
    @BindView(R.id.view_simple) View mSimpleView;
    @BindView(R.id.view_simple_inner) View mSimpleViewInner;

    @Override protected int getContentLayout() {
        return R.layout.activity_view_event;
    }

    @Override protected void initData() {
        super.initData();
    }

    @Override protected void initContentView() {
        super.initContentView();


        mViewGroup.setEnabled(true);
        mViewGroup.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                Log.e(AppConfig.LOG_TAG, "mViewGroup -> View#onTouch");
                return false;
            }
        });

        mSimpleViewInner.setEnabled(true);
        mSimpleViewInner.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                Log.e(AppConfig.LOG_TAG, "mSimpleViewInner -> View#onTouch");
                return false;
            }
        });
        /*mSimpleViewInner.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Log.e(AppConfig.LOG_TAG, "mSimpleViewInner -> View#onClick");
            }
        });*/
        /*mSimpleViewInner.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View v) {
                Log.e(AppConfig.LOG_TAG, "mSimpleViewInner -> View#onLongClick");
                return false;
            }
        });*/
//        mSimpleViewInner.setTouchDelegate(new TouchDelegate());


        mSimpleView.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                Log.e(AppConfig.LOG_TAG, "mSimpleView -> View#onTouch");
                return false;
            }
        });
    }

    @Override public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(AppConfig.LOG_TAG, "Activity#dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        Log.i(AppConfig.LOG_TAG, "Activity#onTouchEvent");
        return super.onTouchEvent(event);
    }

    @Override protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        Log.i(AppConfig.LOG_TAG, "onUserLeaveHint");
    }

    @Override public void onUserInteraction() {
        super.onUserInteraction();
        Log.i(AppConfig.LOG_TAG, "onUserInteraction");
    }

    @OnClick(R.id.tv_perform_click)
    public void onPerformClick(TextView textView) {
        mSimpleViewInner.performClick();
    }
}
