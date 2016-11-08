package com.king.app.workhelper.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.applib.util.ExtendUtil;

/**
 * Author: HuoGuangXu
 * Date: 2016/5/19
 */
public class SimpleDialog extends Dialog implements DialogInterface {

    private Context mContext;

    private LinearLayout mParentPanelLl;

    private RelativeLayout mTopPanelRl;

    private FrameLayout mCustomPanelFl;

    private View mDividerView;

    private TextView mTitleTv;

    private TextView mMessageTv;

    private ImageView mTitleIconIv;

    private Button mLeftBtn;

    private Button mRightBtn;

    private static SimpleDialog mInstance;

    private SimpleDialog(Context context) {
        this(context, 0);
    }

    private SimpleDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        init(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getWindow() != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
            getWindow().setLayout((int) (dm.widthPixels * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public static SimpleDialog getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SimpleDialog.class) {
                if (mInstance == null) {
                    mInstance = new SimpleDialog(context, R.style.DialogStyle);
                }
            }
        }
        return mInstance;
    }

    private void init(Context context) {
        View mDialogView = View.inflate(context, R.layout.layout_dialog, null);

        mParentPanelLl = (LinearLayout) mDialogView.findViewById(R.id.ll_parent_panel);
        mTopPanelRl = (RelativeLayout) mDialogView.findViewById(R.id.rl_top_panel);
        mCustomPanelFl = (FrameLayout) mDialogView.findViewById(R.id.rl_custom_panel);

        mTitleTv = (TextView) mDialogView.findViewById(R.id.tv_dialog_title);
        mMessageTv = (TextView) mDialogView.findViewById(R.id.tv_message);
        mTitleIconIv = (ImageView) mDialogView.findViewById(R.id.iv_title_icon);
        mDividerView = mDialogView.findViewById(R.id.title_divider);
        mLeftBtn = (Button) mDialogView.findViewById(R.id.btn_left);
        mRightBtn = (Button) mDialogView.findViewById(R.id.btn_right);

        setContentView(mDialogView);
    }

    public void toDefault() {
        mTitleTv.setTextColor(mContext.getResources().getColor(R.color.black));
        mDividerView.setBackgroundColor(mContext.getResources().getColor(R.color.divider));
        mMessageTv.setTextColor(mContext.getResources().getColor(R.color.gray_666666));
        mParentPanelLl.setBackgroundColor(mContext.getResources().getColor(R.color.white));
    }

    public SimpleDialog withDividerColor(String colorString) {
        mDividerView.setBackgroundColor(Color.parseColor(colorString));
        return this;
    }

    public SimpleDialog withDividerColor(int color) {
        mDividerView.setBackgroundColor(color);
        return this;
    }


    public SimpleDialog withTitle(CharSequence title) {
        toggleView(mTopPanelRl, title);
        mTitleTv.setText(title);
        return this;
    }

    public SimpleDialog withTitleColor(String colorString) {
        mTitleTv.setTextColor(Color.parseColor(colorString));
        return this;
    }

    public SimpleDialog withTitleColor(int color) {
        mTitleTv.setTextColor(color);
        return this;
    }

    public SimpleDialog withMessage(int textResId) {
        toggleView(mMessageTv, textResId);
        mMessageTv.setText(textResId);
        return this;
    }

    public SimpleDialog withMessage(CharSequence msg) {
        toggleView(mMessageTv, msg);
        mMessageTv.setText(msg);
        return this;
    }

    public SimpleDialog withMessageColor(String colorString) {
        mMessageTv.setTextColor(Color.parseColor(colorString));
        return this;
    }

    public SimpleDialog withMessageColor(int color) {
        mMessageTv.setTextColor(color);
        return this;
    }

    public SimpleDialog withDialogColor(String colorString) {
        mParentPanelLl.getBackground().setColorFilter(ExtendUtil.getColorFilter(Color.parseColor(colorString)));
        return this;
    }

    public SimpleDialog withDialogColor(int color) {
        mParentPanelLl.getBackground().setColorFilter(ExtendUtil.getColorFilter(color));
        return this;
    }

    public SimpleDialog withIcon(int drawableResId) {
        mTitleIconIv.setImageResource(drawableResId);
        return this;
    }

    public SimpleDialog withIcon(Drawable icon) {
        mTitleIconIv.setImageDrawable(icon);
        return this;
    }

    public SimpleDialog withButtonDrawable(int resId) {
        mLeftBtn.setBackgroundResource(resId);
        mRightBtn.setBackgroundResource(resId);
        return this;
    }

    public SimpleDialog withLeftButtonText(CharSequence text) {
        mLeftBtn.setVisibility(View.VISIBLE);
        mLeftBtn.setText(text);

        return this;
    }

    public SimpleDialog withRightButtonText(CharSequence text) {
        mRightBtn.setVisibility(View.VISIBLE);
        mRightBtn.setText(text);
        return this;
    }

    public SimpleDialog setLeftButtonClick(View.OnClickListener click) {
        mLeftBtn.setOnClickListener(click);
        return this;
    }

    public SimpleDialog setRightButtonClick(View.OnClickListener click) {
        mRightBtn.setOnClickListener(click);
        return this;
    }


    public SimpleDialog setCustomView(int resId, Context context) {
        View customView = View.inflate(context, resId, null);
        if (mCustomPanelFl.getChildCount() > 0) {
            mCustomPanelFl.removeAllViews();
        }
        mCustomPanelFl.addView(customView);
        return this;
    }

    public SimpleDialog setCustomView(View view) {
        if (mCustomPanelFl.getChildCount() > 0) {
            mCustomPanelFl.removeAllViews();
        }
        mCustomPanelFl.addView(view);

        return this;
    }

    public SimpleDialog isCancelableOnTouchOutside(boolean cancelable) {
        setCanceledOnTouchOutside(cancelable);
        return this;
    }

    public SimpleDialog isCancelable(boolean cancelable) {
        setCancelable(cancelable);
        return this;
    }

    private void toggleView(View view, Object obj) {
        if (obj == null) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }
}
