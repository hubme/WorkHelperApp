package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.applib.util.StringUtil;

/**
 * 表单输入封装。
 * item左边是一个TextView标题，右边是个TextView，显示内容
 * Created by HuoGuangxu on 2016/11/11.
 */

public class FormInputView extends RelativeLayout {

    private Context mContext;
    private TextView mFormTitleTv;
    private EditText mFormContentEt;
    private TextView mFormContentTv;
    private View mRootView;
    private int mInputType = TEXT_INPUT_TYPE;

    public static final int TEXT_INPUT_TYPE = 0;
    public static final int EDIT_INPUT_TYPE = 1;

    @IntDef({TEXT_INPUT_TYPE, EDIT_INPUT_TYPE})
    public @interface INPUT_TYPE {

    }

    public FormInputView(Context context) {
        this(context, null);
    }

    public FormInputView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FormInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        mRootView = View.inflate(mContext, R.layout.layout_form_input_item, this);
        mFormTitleTv = (TextView) findViewById(R.id.tv_item_title);
    }

    public FormInputView setInputType(@INPUT_TYPE int type) {
        mInputType = type;

        ViewStub contentVs = (ViewStub) findViewById(type == EDIT_INPUT_TYPE ? R.id.vs_input_edit_text : R.id.vs_input_text_view);
        View view = contentVs.inflate();

        if (type == EDIT_INPUT_TYPE) {
            mFormContentEt = (EditText) view.findViewById(R.id.et_item_input);
        } else {
            mFormContentTv = (TextView) view.findViewById(R.id.tv_item_input);
        }
        return this;
    }

    public TextView getFormTitle() {
        return mFormTitleTv;
    }

    public TextView getFormTextView() {
        return mFormContentTv;
    }

    public EditText getFormEditText() {
        return mFormContentEt;
    }

    public FormInputView setTitle(@StringRes int resId) {
        if (mFormTitleTv != null) {
            mFormTitleTv.setText(mContext.getString(resId));
        }
        return this;
    }

    public FormInputView setTitle(String text) {
        if (mFormTitleTv != null && !StringUtil.isNullOrEmpty(text)) {
            mFormTitleTv.setText(text);
        }
        return this;
    }

    public FormInputView setTitleTextSize(@DimenRes int resId) {
        if (mFormTitleTv != null) {
            mFormTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(resId));
        }
        return this;
    }

    public FormInputView setTitleTextColor(@ColorRes int resId) {
        if (mFormTitleTv != null) {
            mFormTitleTv.setTextColor(getResources().getColor(resId));
        }
        return this;
    }

    public FormInputView setContentBackgroundResource(@DrawableRes int resId) {
        if (mFormContentTv != null) {
            mFormContentTv.setBackgroundResource(resId);
        }
        return this;
    }

    public void setRightDrawable(@DrawableRes int resId) {
        if (mFormContentTv != null) {
            mFormContentTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);
        }
        if (mFormContentEt != null) {
            mFormContentEt.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);
        }
    }

    public FormInputView setContentText(@StringRes int resId) {
        if (mFormContentTv != null) {
            mFormContentTv.setText(resId);
        }
        if (mFormContentEt != null) {
            mFormContentEt.setText(resId);
        }
        return this;
    }

    public FormInputView setContentText(String text) {
        if (mFormContentTv != null && !StringUtil.isNullOrEmpty(text)) {
            mFormContentTv.setText(text);
        }
        if (mFormContentEt != null) {
            mFormContentEt.setText(text);
        }
        return this;
    }

    public FormInputView setContentTextColor(@ColorRes int resId) {
        if (mFormContentTv != null) {
            mFormContentTv.setTextColor(getResources().getColor(resId));
        }
        if (mFormContentEt != null) {
            mFormContentEt.setTextColor(getResources().getColor(resId));
        }
        return this;
    }

    public FormInputView setContentTextSize(@DimenRes int resId) {
        if (mFormContentTv != null) {
            mFormContentTv.setTextSize(getResources().getDimensionPixelSize(resId));
        }
        if (mFormContentEt != null) {
            mFormContentEt.setTextSize(getResources().getDimensionPixelSize(resId));
        }
        return this;
    }
}
