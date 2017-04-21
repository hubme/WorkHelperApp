package com.king.app.workhelper.activity;

import android.support.annotation.IdRes;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.okhttp.OkHttpLogInterceptor;

import butterknife.BindView;

/**
 * Debug设置
 *
 * @author huoguangxu
 * @since 2017/4/11.
 */

public class DebugActivity extends AppBaseActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.log_group) RadioGroup mDomainGroup;
    @BindView(R.id.rb_close_log) RadioButton mNoLogRb;
    @BindView(R.id.rb_basal_log) RadioButton mRequestLogRb;
    @BindView(R.id.rb_all_log) RadioButton mResponseLogRb;

    @Override
    public int getContentLayout() {
        return R.layout.activity_debug;
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        mDomainGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rb_close_log:
                OkHttpLogInterceptor.setLogLevel(OkHttpLogInterceptor.NONE);
                break;
            case R.id.rb_basal_log:
                OkHttpLogInterceptor.setLogLevel(OkHttpLogInterceptor.BASAL);
                break;
            case R.id.rb_all_log:
                OkHttpLogInterceptor.setLogLevel(OkHttpLogInterceptor.ALL);
                break;
            default:
                break;
        }
    }
}
