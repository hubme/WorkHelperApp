package com.king.app.workhelper.activity

import android.app.Activity
import android.content.Intent
import com.king.app.workhelper.R
import com.king.app.workhelper.common.AppBaseActivity
import com.king.app.workhelper.common.FirebaseManager
import kotlinx.android.synthetic.main.activity_test.*

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

class TestActivity : AppBaseActivity() {

    override fun getContentLayout(): Int {
        return R.layout.activity_test
    }

    override fun initData() {

    }

    override fun initContentView() {
        super.initContentView()
        tv_permission.setOnClickListener { onPermissionClick() }
    }

    private fun onPermissionClick() {
        FirebaseManager.getInstance(this).logEvent("CustomEvent")
    }

    //reified 关键字来表示该泛型要进行实化
    inline fun <reified T : Activity> openActivity() {
        startActivity(Intent(this, T::class.java))
    }
}
