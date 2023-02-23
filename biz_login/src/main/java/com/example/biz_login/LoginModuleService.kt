package com.example.biz_login

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.biz_common.ROUTER_LOGIN_SERVICE
import com.example.biz_export.login.ILoginModuleService
import com.king.applib.util.ToastUtil

/**
 * 登录组件暴露的接口实现类。
 *
 * @author VanceKing
 * created at 2023/2/23
 */
@Route(path = ROUTER_LOGIN_SERVICE)
class LoginModuleService : ILoginModuleService {
    override fun init(context: Context?) {
    }

    override fun login() {
        ToastUtil.showShort("登录成功")
    }

    override fun logout() {
        ToastUtil.showShort("退出成功")
    }


}