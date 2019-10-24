package com.king.app.workhelper.activity

import android.util.Log
import butterknife.OnClick
import com.king.app.workhelper.R
import com.king.app.workhelper.common.AppBaseActivity
import com.king.app.workhelper.common.RxBus
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
        tv_permission.setOnClickListener { Log.i("aaa", it.id.toString()) }
    }
    
    @OnClick(R.id.tv_permission)
    fun onPermissionClick() {
        RxBus.getInstance().post("1024", "哈哈哈")
    }

}
