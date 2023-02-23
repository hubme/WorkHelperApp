package com.example.biz_export.login

import com.alibaba.android.arouter.facade.template.IProvider

interface ILoginModuleService : IProvider {
    fun login()

    fun logout()
}