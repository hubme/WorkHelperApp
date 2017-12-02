package com.king.applib.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

/**
 * PopWindow 工具类。
 *
 * @author VanceKing
 * @since 2017/12/2.
 */

public class SimplePopWindow implements OnDismissListener {
    private static final float DEFAULT_ALPHA = 0.7f;
    private static final String TAG = "SimplePopWindow";
    private boolean enableOutsideTouchDisMiss;
    private int mAnimationStyle;
    private float mBackgroundDarkValue;
    private boolean mClippEnable;
    private View mContentView;
    private Context mContext;
    private int mHeight;
    private boolean mIgnoreCheekPress;
    private int mInputMode;
    private boolean mIsBackgroundDark;
    private boolean mIsFocusable;
    private boolean mIsOutside;
    private OnDismissListener mOnDismissListener;
    private OnTouchListener mOnTouchListener;
    private PopupWindow mPopupWindow;
    private int mResLayoutId;
    private int mSoftInputMode;
    private boolean mTouchable;
    private int mWidth;
    private Window mWindow;

    public static class PopupWindowBuilder {
        private SimplePopWindow mCustomPopWindow;

        public PopupWindowBuilder(Context context) {
            this.mCustomPopWindow = new SimplePopWindow(context);
        }

        public PopupWindowBuilder size(int width, int height) {
            this.mCustomPopWindow.mWidth = width;
            this.mCustomPopWindow.mHeight = height;
            return this;
        }

        public PopupWindowBuilder setFocusable(boolean focusable) {
            this.mCustomPopWindow.mIsFocusable = focusable;
            return this;
        }

        public PopupWindowBuilder setView(int resLayoutId) {
            this.mCustomPopWindow.mResLayoutId = resLayoutId;
            this.mCustomPopWindow.mContentView = null;
            return this;
        }

        public PopupWindowBuilder setView(View view) {
            this.mCustomPopWindow.mContentView = view;
            this.mCustomPopWindow.mResLayoutId = -1;
            return this;
        }

        public PopupWindowBuilder setOutsideTouchable(boolean outsideTouchable) {
            this.mCustomPopWindow.mIsOutside = outsideTouchable;
            return this;
        }

        public PopupWindowBuilder setAnimationStyle(int animationStyle) {
            this.mCustomPopWindow.mAnimationStyle = animationStyle;
            return this;
        }

        public PopupWindowBuilder setClippingEnable(boolean enable) {
            this.mCustomPopWindow.mClippEnable = enable;
            return this;
        }

        public PopupWindowBuilder setIgnoreCheekPress(boolean ignoreCheekPress) {
            this.mCustomPopWindow.mIgnoreCheekPress = ignoreCheekPress;
            return this;
        }

        public PopupWindowBuilder setInputMethodMode(int mode) {
            this.mCustomPopWindow.mInputMode = mode;
            return this;
        }

        public PopupWindowBuilder setOnDissmissListener(OnDismissListener onDismissListener) {
            this.mCustomPopWindow.mOnDismissListener = onDismissListener;
            return this;
        }

        public PopupWindowBuilder setSoftInputMode(int softInputMode) {
            this.mCustomPopWindow.mSoftInputMode = softInputMode;
            return this;
        }

        public PopupWindowBuilder setTouchable(boolean touchable) {
            this.mCustomPopWindow.mTouchable = touchable;
            return this;
        }

        public PopupWindowBuilder setTouchIntercepter(OnTouchListener touchInterceptor) {
            this.mCustomPopWindow.mOnTouchListener = touchInterceptor;
            return this;
        }

        public PopupWindowBuilder enableBackgroundDark(boolean isDark) {
            this.mCustomPopWindow.mIsBackgroundDark = isDark;
            return this;
        }

        public PopupWindowBuilder setBgDarkAlpha(float darkValue) {
            this.mCustomPopWindow.mBackgroundDarkValue = darkValue;
            return this;
        }

        public PopupWindowBuilder enableOutsideTouchableDissmiss(boolean disMiss) {
            this.mCustomPopWindow.enableOutsideTouchDisMiss = disMiss;
            return this;
        }

        public SimplePopWindow create() {
            this.mCustomPopWindow.build();
            return this.mCustomPopWindow;
        }
    }

    private SimplePopWindow(Context context) {
        this.mIsFocusable = true;
        this.mIsOutside = true;
        this.mResLayoutId = -1;
        this.mAnimationStyle = -1;
        this.mClippEnable = true;
        this.mIgnoreCheekPress = false;
        this.mInputMode = -1;
        this.mSoftInputMode = -1;
        this.mTouchable = true;
        this.mIsBackgroundDark = false;
        this.mBackgroundDarkValue = 0.0f;
        this.enableOutsideTouchDisMiss = true;
        this.mContext = context;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public SimplePopWindow showAsDropDown(View anchor, int xOff, int yOff) {
        if (this.mPopupWindow != null) {
            this.mPopupWindow.showAsDropDown(anchor, xOff, yOff);
        }
        return this;
    }

    public SimplePopWindow showAsDropDown(View anchor) {
        if (this.mPopupWindow != null) {
            this.mPopupWindow.showAsDropDown(anchor);
        }
        return this;
    }

    @RequiresApi(api = 19)
    public SimplePopWindow showAsDropDown(View anchor, int xOff, int yOff, int gravity) {
        if (this.mPopupWindow != null) {
            this.mPopupWindow.showAsDropDown(anchor, xOff, yOff, gravity);
        }
        return this;
    }

    public SimplePopWindow showAtLocation(View parent, int gravity, int x, int y) {
        if (this.mPopupWindow != null) {
            this.mPopupWindow.showAtLocation(parent, gravity, x, y);
        }
        return this;
    }

    private void apply(PopupWindow popupWindow) {
        popupWindow.setClippingEnabled(this.mClippEnable);
        if (this.mIgnoreCheekPress) {
            popupWindow.setIgnoreCheekPress();
        }
        if (this.mInputMode != -1) {
            popupWindow.setInputMethodMode(this.mInputMode);
        }
        if (this.mSoftInputMode != -1) {
            popupWindow.setSoftInputMode(this.mSoftInputMode);
        }
        if (this.mOnDismissListener != null) {
            popupWindow.setOnDismissListener(this.mOnDismissListener);
        }
        if (this.mOnTouchListener != null) {
            popupWindow.setTouchInterceptor(this.mOnTouchListener);
        }
        popupWindow.setTouchable(this.mTouchable);
    }

    private PopupWindow build() {
        if (this.mContentView == null) {
            this.mContentView = LayoutInflater.from(this.mContext).inflate(this.mResLayoutId, null);
        }
        Activity activity = (Activity) this.mContentView.getContext();
        if (activity != null && this.mIsBackgroundDark) {
            float alpha = (this.mBackgroundDarkValue <= 0.0f || this.mBackgroundDarkValue >= 1.0f) ? DEFAULT_ALPHA : this.mBackgroundDarkValue;
            this.mWindow = activity.getWindow();
            LayoutParams params = this.mWindow.getAttributes();
            params.alpha = alpha;
            this.mWindow.addFlags(2);
            this.mWindow.setAttributes(params);
        }
        if (this.mWidth == 0 || this.mHeight == 0) {
            this.mPopupWindow = new PopupWindow(this.mContentView, -2, -2);
        } else {
            this.mPopupWindow = new PopupWindow(this.mContentView, this.mWidth, this.mHeight);
        }
        if (this.mAnimationStyle != -1) {
            this.mPopupWindow.setAnimationStyle(this.mAnimationStyle);
        }
        apply(this.mPopupWindow);
        if (this.mWidth == 0 || this.mHeight == 0) {
            this.mPopupWindow.getContentView().measure(0, 0);
            this.mWidth = this.mPopupWindow.getContentView().getMeasuredWidth();
            this.mHeight = this.mPopupWindow.getContentView().getMeasuredHeight();
        }
        this.mPopupWindow.setOnDismissListener(this);
        if (this.enableOutsideTouchDisMiss) {
            this.mPopupWindow.setFocusable(this.mIsFocusable);
            this.mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
            this.mPopupWindow.setOutsideTouchable(this.mIsOutside);
        } else {
            this.mPopupWindow.setFocusable(true);
            this.mPopupWindow.setOutsideTouchable(false);
            this.mPopupWindow.setBackgroundDrawable(null);
            this.mPopupWindow.getContentView().setFocusable(true);
            this.mPopupWindow.getContentView().setFocusableInTouchMode(true);
            this.mPopupWindow.getContentView().setOnKeyListener(new OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode != 4) {
                        return false;
                    }
                    SimplePopWindow.this.mPopupWindow.dismiss();
                    return true;
                }
            });
            this.mPopupWindow.setTouchInterceptor(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    if (event.getAction() == 0 && (x < 0 || x >= SimplePopWindow.this.mWidth || y < 0 || y >= SimplePopWindow.this.mHeight)) {
                        Log.e(SimplePopWindow.TAG, "out side ");
                        Log.e(SimplePopWindow.TAG, "width:" + SimplePopWindow.this.mPopupWindow.getWidth() + "height:" + SimplePopWindow.this.mPopupWindow.getHeight() + " x:" + x + " y  :" + y);
                        return true;
                    } else if (event.getAction() != 4) {
                        return false;
                    } else {
                        Log.e(SimplePopWindow.TAG, "out side ...");
                        return true;
                    }
                }
            });
        }
        this.mPopupWindow.update();
        return this.mPopupWindow;
    }

    public void onDismiss() {
        dissmiss();
    }

    public void dissmiss() {
        if (this.mOnDismissListener != null) {
            this.mOnDismissListener.onDismiss();
        }
        if (this.mWindow != null) {
            LayoutParams params = this.mWindow.getAttributes();
            params.alpha = 1.0f;
            this.mWindow.setAttributes(params);
        }
        if (this.mPopupWindow != null && this.mPopupWindow.isShowing()) {
            this.mPopupWindow.dismiss();
        }
    }

    public PopupWindow getPopupWindow() {
        return this.mPopupWindow;
    }
}
