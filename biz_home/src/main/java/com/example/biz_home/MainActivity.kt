package com.example.biz_home

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.biz_common.ROUTER_HOME_MAIN
import com.example.biz_common.ROUTER_LOGIN_SERVICE
import com.example.biz_export.login.ILoginModuleService
import com.example.biz_home.databinding.HomeActivityMainBinding
import com.king.applib.base.BaseActivity

/**
 * Home 组件首页。
 *
 * @author VanceKing
 * created at 2023/2/23
 */
@Route(path = ROUTER_HOME_MAIN)
class MainActivity : BaseActivity() {
    private lateinit var mViewBinding: HomeActivityMainBinding

    override fun getContentView(): View {
        super.initContentView()
        mViewBinding = HomeActivityMainBinding.inflate(layoutInflater)
        return mViewBinding.root
    }

    override fun initContentView() {
        super.initContentView()
        mViewBinding.btnLogin.setOnClickListener {
            val navigation = ARouter.getInstance().build(ROUTER_LOGIN_SERVICE).navigation() as ILoginModuleService
            navigation.login()
        }
    }
}