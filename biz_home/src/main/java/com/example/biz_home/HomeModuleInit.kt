package com.example.biz_home

import android.app.Application
import com.example.biz_common.IModuleInit
import com.king.applib.log.Logger

/**
 * Home 组件初始化。
 *
 * @author VanceKing
 * created at 2023/2/22
 */
class HomeModuleInit : IModuleInit {
    override fun onInit(application: Application) {
        Logger.i("HomeModuleInit")
    }
}