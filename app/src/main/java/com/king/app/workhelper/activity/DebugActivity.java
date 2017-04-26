package com.king.app.workhelper.activity;

import android.support.annotation.IdRes;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.okhttp.LogInterceptor;

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
    protected void initData() {
        super.initData();
        LogInterceptor.setLogLevel(LogInterceptor.BASAL);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rb_close_log:
                LogInterceptor.setLogLevel(LogInterceptor.NONE);
                break;
            case R.id.rb_basal_log:
                LogInterceptor.setLogLevel(LogInterceptor.BASAL);
                break;
            case R.id.rb_all_log:
                LogInterceptor.setLogLevel(LogInterceptor.ALL);
                break;
            default:
                break;
        }
    }
}
