package com.example.biz_login.activity

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.biz_common.ROUTER_LOGIN_ACTIVITY
import com.example.biz_login.databinding.ActivityLoginBinding
import com.king.applib.base.BaseActivity
import com.king.applib.util.ToastUtil

/**
 * 登录页面。
 *
 * @author VanceKing
 * created at 2023/2/23
 */
@Route(path = ROUTER_LOGIN_ACTIVITY)
class LoginActivity : BaseActivity() {
    private lateinit var mViewBinding: ActivityLoginBinding

    override fun getContentView(): View {
        mViewBinding = ActivityLoginBinding.inflate(layoutInflater)
        return mViewBinding.root
    }

    override fun initContentView() {
        super.initContentView()
        mViewBinding.btnLogin.setOnClickListener {
            ToastUtil.showShort("登录")
        }
    }
}