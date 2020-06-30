package com.king.app.workhelper.activity

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

}
