package com.king.app.workhelper.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import com.king.app.workhelper.R
import com.king.app.workhelper.common.AppBaseActivity
import com.king.applib.util.dp
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
        tv_clear.setOnClickListener { flow_layout.removeAllViews() }
        with(flow_layout) {
            setPadding(10, 10, 30, 10)
            setVisibleCount(3, 40f.dp)
            setHorizontalSpacing(8)
            setVerticalSpacing(10f.dp)
        }
        tv_remove_first.setOnClickListener {
            if (flow_layout.childCount > 0) {
                flow_layout.removeViewAt(0)
            }
        }
        tv_remove_last.setOnClickListener {
            if (flow_layout.childCount > 0) {
                flow_layout.removeViewAt(flow_layout.childCount - 1)
            }
        }
    }

    private fun onPermissionClick() {
        flow_layout.addView(buildChildView(et_added.text.toString()))
    }

    private fun buildChildView(name: String) = TextView(this).apply {
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.height = 40f.dp
        if (!et_view_width.text.isNullOrBlank()) {
            layoutParams.width = et_view_width.text.toString().toInt()
        }
        text = name
        setTextColor(Color.BLACK)
        textSize = 18f
        gravity = Gravity.CENTER
    }

    //reified 关键字来表示该泛型要进行实化
    inline fun <reified T : Activity> openActivity() {
        startActivity(Intent(this, T::class.java))
    }
}
