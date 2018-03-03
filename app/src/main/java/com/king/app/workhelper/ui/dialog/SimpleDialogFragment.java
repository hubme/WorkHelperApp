package com.king.app.workhelper.ui.dialog;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.king.app.workhelper.R;

/**
 * 简单的弹框封装
 * Created by HuoGuangxu on 2016/11/8.
 */
public class SimpleDialogFragment extends DialogFragment {

    private boolean mOutsideCancel;
    private ImageView mLeftIconIv;
    private ImageView mRightIconIv;
    private FrameLayout mDialogContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //或者在onCreateView()中写getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_simple_dialog_fragment, container);
        initView(view);
        getDialog().setCanceledOnTouchOutside(mOutsideCancel);
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDialog();
    }

    @Override public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    private void initView(View view) {
        mLeftIconIv = (ImageView) view.findViewById(R.id.iv_left_icon);
        mRightIconIv = (ImageView) view.findViewById(R.id.iv_righ_icon);
        mDialogContainer = (FrameLayout) view.findViewById(R.id.dialog_container);
    }

    //写在onCreate()报NPE,写在onCreateView()或onViewCreated无效
    private void initDialog() {
        Window window = getDialog().getWindow();
        if (window != null) {
            //设置背景为透明，避免设置圆角时四个角有黑色.
            window.setBackgroundDrawableResource(android.R.color.transparent);

            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            window.setLayout((int) (dm.widthPixels * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);

            window.setWindowAnimations(R.style.dialogEnter);

        }
    }

    public static class Builder {
        @DrawableRes
        int mResId;

        public Builder setLeftIcon(@DrawableRes int resId) {
            mResId = resId;
            return this;
        }

        public SimpleDialogFragment create() {
            SimpleDialogFragment dialog = new SimpleDialogFragment();
            dialog.setLeftIcon(mResId);
            return dialog;
        }
    }

    public SimpleDialogFragment isCancelable(boolean cancelable) {
        setCancelable(cancelable);
        return this;
    }

    public SimpleDialogFragment setCanceledOnTouchOutside(boolean cancel) {
        mOutsideCancel = cancel;
        return this;
    }

    public SimpleDialogFragment setLeftIcon(@DrawableRes int resId) {
        if (mLeftIconIv != null) {
            mLeftIconIv.setBackgroundResource(resId);
        }
        return this;
    }

    public SimpleDialogFragment setRightIcon(@DrawableRes int resId) {
        mRightIconIv.setBackgroundResource(resId);
        return this;
    }

    public SimpleDialogFragment setView(View customView) {
        if (mDialogContainer.getChildCount() > 0) {
            mDialogContainer.removeAllViews();
        }
        mDialogContainer.addView(customView);
        return this;
    }

    //在时间很短的情况下，会弹出多个Dialog.场景: 收到账户退出广播或event.
    //解决方法：1.两次弹出的时间间隔长一些.2.用变量自己控制.
    /*private void showDialog() {
        if (dialogFragment == null) {//一个实例即可
            dialogFragment = new SimpleDialogFragment();
        }

        //在时间很短的情况下isVisible()条件还没有更新,会再show(),此时会报Fragment already added异常
        if (!dialogFragment.isVisible()) {
            dialogFragment.show(getFragmentManager(), "aaa");
        }
    }*/
}
