package com.king.app.workhelper.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Message;
import android.os.UserManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.dialog.PhotoBottomDialog;
import com.king.app.workhelper.dialog.ShareBottomDialog;
import com.king.app.workhelper.ui.dialog.SimpleDialogFragment;
import com.king.applib.base.WeakHandler;
import com.king.applib.log.Logger;

import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * WeakHandler
 * Created by HuoGuangxu on 2016/12/1.
 */

public class UsageFragment extends AppBaseFragment {
    private static final int MSG_WHAT = 0x01;
    private static final int MSG_WAIT = 0x02;

    @BindView(R.id.tv_weak_handler) TextView mTextView;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout mRefreshLayout;

    private MyHandler mMyHandler = new MyHandler(this);

    private static class MyHandler extends WeakHandler<UsageFragment> {
        public MyHandler(UsageFragment target) {
            super(target);
        }

        @Override
        public void handle(UsageFragment target, Message msg) {
            switch (msg.what) {
                case MSG_WHAT:
                    target.mTextView.setText("哈哈哈");
                case MSG_WAIT:

                    target.mRefreshLayout.setRefreshing(false);
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    protected int getContentLayout() {
        return R.layout.fragment_usage;
    }

    @Override
    protected void initData() {
        super.initData();

        mRefreshLayout.setOnRefreshListener(() -> {
            Logger.i("onRefresh");
            mMyHandler.sendEmptyMessageDelayed(MSG_WAIT, 3000);
        });
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PhotoBottomDialog.REQ_CODE_CAMERA:
                Logger.i("相机");
                break;
            case PhotoBottomDialog.REQ_CODE_ALBUM:
                Logger.i("相册");
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.tv_weak_handler)
    public void clickWeakHandler() {
        mMyHandler.sendEmptyMessage(MSG_WHAT);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @OnClick(R.id.tv_reflect)
    public void reflectClick() {
        try {
            final Method method = UserManager.class.getMethod("get", Context.class);
            method.setAccessible(true);
            method.invoke(null, this);
        } catch (Exception e) {//(NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.tv_bottom_dialog)
    public void onBottomDialogClick() {
        /*BottomDialog.create(getActivity().getSupportFragmentManager())
                .setViewListener(new BottomDialog.ViewListener() {
                    @Override
                    public void bindView(View v) {
                        initBottomDialogView(v);
                    }
                })
                .setLayoutRes(R.layout.dialog_layout)
                .setDimAmount(0.2f)
                .setTag("BottomDialog")
                .show();*/
        ShareBottomDialog dialog = new ShareBottomDialog();
        dialog.show(getActivity().getSupportFragmentManager());
    }

    private void initBottomDialogView(View v) {
        TextView textView = ButterKnife.findById(v, R.id.tv_we_chat);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "哈哈哈", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.tv_ripple_effect)
    public void onRippleEffect() {

    }

    @OnClick(R.id.tv_dialog_fragment)
    public void onDialogFragmentClick() {
        SimpleDialogFragment dialogFragment = new SimpleDialogFragment.Builder().create();
        if (!dialogFragment.isVisible()) {
            dialogFragment.show(getFragmentManager(), "aaa");
        }
    }

}
