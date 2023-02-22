package com.example.biz_common

import android.app.Application

/**
 * 各组件初始化接口。
 */
interface IModuleInit {
    fun onInit(application: Application)
}