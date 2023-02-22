package com.example.biz_login

import android.app.Application
import com.example.biz_common.IModuleInit
import com.king.applib.log.Logger

/**
 * Login 组件初始化。
 * 
 * @author VanceKing
 * created at 2023/2/22
 */
class LoginModuleInit : IModuleInit {
    override fun onInit(application: Application) {
        Logger.i("LoginModuleInit")
    }
}