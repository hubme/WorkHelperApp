package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.applib.util.StringUtil;

/**
 * 表单输入封装。
 * item左边是一个TextView标题，右边是个TextView，显示内容
 * Created by HuoGuangxu on 2016/11/11.
 */

public class FormTextViewItem extends RelativeLayout {

    private Context mContext;
    private TextView mFormTitleTv;
    private TextView mFormContentTv;
    private View mRootView;
    private int mInputType = TEXT_INPUT_TYPE;

    public static final int TEXT_INPUT_TYPE = 0;
    public static final int EDIT_INPUT_TYPE = 1;

    @IntDef({TEXT_INPUT_TYPE, EDIT_INPUT_TYPE})
    public @interface INPUT_TYPE {

    }

    public FormTextViewItem(Context context) {
        this(context, null);
    }

    public FormTextViewItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FormTextViewItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        mRootView = View.inflate(mContext, R.layout.layout_form_input_item, this);
        mFormTitleTv = (TextView) findViewById(R.id.tv_item_title);
    }

    public void setInputType(@INPUT_TYPE int type) {
        mInputType = type;

        ViewStub contentVs = (ViewStub) findViewById(type == EDIT_INPUT_TYPE?R.id.vs_input_edit_text:R.id.vs_input_text_view);

        if (type == EDIT_INPUT_TYPE) {
            ViewStub contentVs = (ViewStub) findViewById(R.id.vs_input_edit_text);
            View view = contentVs.inflate();
            mFormContentTv =  (TextView) view.findViewById(R.id.et_item_input);
        } else {
            ViewStub contentVs = (ViewStub) findViewById(R.id.vs_input_text_view);

        }
    }

    public TextView getFormTitle() {
        return mFormTitleTv;
    }

    public TextView getFormContent() {
        return mFormContentTv;
    }

    public FormTextViewItem setTitle(@StringRes int resId) {
        mFormTitleTv.setText(mContext.getString(resId));
        return this;
    }

    public FormTextViewItem setTitle(String text) {
        if (!StringUtil.isNullOrEmpty(text)) {
            mFormTitleTv.setText(text);
        }
        return this;
    }

    public FormTextViewItem setTitleTextSize(@DimenRes int resId) {
        mFormTitleTv.setTextSize(getResources().getDimensionPixelSize(resId));
        return this;
    }

    public FormTextViewItem setTitleTextColor(@ColorRes int resId) {
        mFormTitleTv.setTextColor(getResources().getColor(resId));
        return this;
    }

    public FormTextViewItem setContentBackgroundResource(@DrawableRes int resId) {
        mFormContentTv.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {

            }
        });
        mFormContentTv.setBackgroundResource(resId);
        return this;
    }

    public void setRightDrawable(@DrawableRes int resId) {
        mFormContentTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);
    }

    public FormTextViewItem setContentText(@StringRes int resId) {
        mFormContentTv.setText(resId);
        return this;
    }

    public FormTextViewItem setContentText(String text) {
        if (!StringUtil.isNullOrEmpty(text)) {
            mFormContentTv.setText(text);
        }
        return this;
    }

    public FormTextViewItem setContentTextColor(@ColorRes int resId) {
        mFormContentTv.setTextColor(getResources().getColor(resId));
        return this;
    }

    public FormTextViewItem setContentTextSize(@DimenRes int resId) {
        mFormContentTv.setTextSize(getResources().getDimensionPixelSize(resId));
        return this;
    }
}
