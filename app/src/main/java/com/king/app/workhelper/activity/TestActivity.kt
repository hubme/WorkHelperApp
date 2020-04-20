package com.king.app.workhelper.activity

import android.graphics.drawable.ClipDrawable
import android.view.Gravity
import androidx.core.content.ContextCompat
import com.king.app.workhelper.R
import com.king.app.workhelper.common.AppBaseActivity
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
        val clipDrawable = ClipDrawable(ContextCompat.getDrawable(this, R.drawable.banner1), 
                Gravity.START, ClipDrawable.HORIZONTAL)
        image_aaa.setImageDrawable(clipDrawable)
        clipDrawable.level = 5000
    }

}
