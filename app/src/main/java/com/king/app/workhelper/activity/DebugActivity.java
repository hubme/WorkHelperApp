package com.king.app.workhelper.activity;

import android.widget.CheckedTextView;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.constant.GlobalConstant;
import com.king.app.workhelper.okhttp.interceptor.LogInterceptor;
import com.king.applib.util.SPUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 开发者选项页面
 *
 * @author VanceKing
 * @since 2017/4/11.
 */

public class DebugActivity extends AppBaseActivity {
    @BindView(R.id.ctv_disable_log) CheckedTextView mLogDisableCtv;
    @BindView(R.id.ctv_request_header) CheckedTextView mRequestHeaderCtv;
    @BindView(R.id.ctv_response_header) CheckedTextView mResponseHeaderCtv;

    @Override
    public int getContentLayout() {
        return R.layout.activity_debug;
    }

    @Override
    protected void initContentView() {
        super.initContentView();

        setRequestHeaderViewChecked(SPUtil.getBoolean(GlobalConstant.SP_PARAMS_KEY.PRINT_REQUEST_HEADER));
        setResponseHeaderViewChecked(SPUtil.getBoolean(GlobalConstant.SP_PARAMS_KEY.PRINT_RESPONSE_HEADER));
        setDisableLogViewChecked(SPUtil.getBoolean(GlobalConstant.SP_PARAMS_KEY.INTERCEPTOR_LOG_DISABLE));
    }

    @Override
    protected void initData() {
        super.initData();
        LogInterceptor.setLogLevel(LogInterceptor.BASAL);
    }

    @OnClick(R.id.ctv_request_header)
    public void onRequestHeaderViewClick(CheckedTextView textView) {
        textView.toggle();
        setRequestHeaderViewChecked(textView.isChecked());
        if (mLogDisableCtv.isChecked()) {
            setDisableLogViewChecked(false);
        }
    }

    @OnClick(R.id.ctv_response_header)
    public void onResponseHeaderViewClick(CheckedTextView textView) {
        textView.toggle();
        setResponseHeaderViewChecked(textView.isChecked());
        if (mLogDisableCtv.isChecked()) {
            setDisableLogViewChecked(false);
        }
    }

    @OnClick(R.id.ctv_disable_log)
    public void onDisableLogViewClick(CheckedTextView textView) {
        textView.toggle();
        setDisableLogViewChecked(textView.isChecked());
        if (mRequestHeaderCtv.isChecked()) {
            setRequestHeaderViewChecked(false);
        }
        if (mResponseHeaderCtv.isChecked()) {
            setResponseHeaderViewChecked(false);
        }
    }

    private void setRequestHeaderViewChecked(boolean isChecked) {
        mRequestHeaderCtv.setChecked(isChecked);
        refreshView(mRequestHeaderCtv, isChecked);
        SPUtil.putBoolean(GlobalConstant.SP_PARAMS_KEY.PRINT_REQUEST_HEADER, isChecked);
    }

    private void setResponseHeaderViewChecked(boolean isChecked) {
        mResponseHeaderCtv.setChecked(isChecked);
        refreshView(mResponseHeaderCtv, isChecked);
        SPUtil.putBoolean(GlobalConstant.SP_PARAMS_KEY.PRINT_RESPONSE_HEADER, isChecked);
    }

    private void setDisableLogViewChecked(boolean isChecked) {
        mLogDisableCtv.setChecked(isChecked);
        refreshView(mLogDisableCtv, isChecked);
        SPUtil.putBoolean(GlobalConstant.SP_PARAMS_KEY.INTERCEPTOR_LOG_DISABLE, isChecked);
    }

    private void refreshView(TextView textView, boolean isChecked) {
        textView.setCompoundDrawablesWithIntrinsicBounds(isChecked ?
                R.drawable.icon_multi_checkbox_checked : R.drawable.icon_multi_checkbox_unchecked, 0, 0, 0);
    }
}
