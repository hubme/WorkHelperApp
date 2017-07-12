package com.king.applib.base.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.king.applib.R;


/**
 * 弹窗抽象类.
 *
 * @author VanceKing
 * @since 2017/7/12.
 */
public abstract class BaseDialogFragment extends DialogFragment {
    private static final String TAG = "BaseDialog";
    private static final float DEFAULT_DIM = 0.4f;
    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = getContext();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.SimpleDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
        }
        getDialog().setCanceledOnTouchOutside(getCancelOutside());

        View v = inflater.inflate(getLayoutRes(), container, false);
        bindView(v);
        return v;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWindow();
    }

    private void initWindow() {
        Window window = getDialog().getWindow();
        if (window == null) {
            return;
        }
        WindowManager.LayoutParams params = window.getAttributes();
        params.dimAmount = getDimAmount();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        if (getWidth() > 0) {
            params.width = getWidth();
        } else {
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
        }

        if (getHeight() > 0) {
            params.height = getHeight();
        } else {
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        params.gravity = getGravity();

        window.setAttributes(params);
        if (getAnimationRes() != -1) {
            window.setWindowAnimations(getAnimationRes());
        }
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    @StyleRes protected int getAnimationRes() {
        return -1;
    }

    /** 返回View给client,可以动态添加View、设置监听事件等 */
    protected abstract void bindView(View v);

    public int getWidth() {
        return -1;
    }

    public int getHeight() {
        return -1;
    }

    public float getDimAmount() {
        return DEFAULT_DIM;
    }

    public boolean getCancelOutside() {
        return true;
    }

    protected int getGravity() {
        return Gravity.CENTER;
    }

    public String getFragmentTag() {
        return TAG;
    }

    public void showDialog(FragmentManager fragmentManager) {
        if (!isVisible()) {
            show(fragmentManager, getFragmentTag());
        }
    }

    public void showDialog(FragmentManager fragmentManager, String tag) {
        if (!isVisible()) {
            show(fragmentManager, tag);
        }
    }

    public DisplayMetrics getDisplayMetrics() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }
}
