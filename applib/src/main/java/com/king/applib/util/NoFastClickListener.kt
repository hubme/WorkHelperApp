package com.king.applib.util

import android.view.View

/**
 * 防止重复点击监听器。
 *
 * @author guangxu.huo
 * @since 2021/3/12
 */
abstract class NoFastClickListener(private val throttleDuration: Long = 500) : View.OnClickListener {
    override fun onClick(v: View) {
        if (mClickTime == 0L || System.currentTimeMillis() - mClickTime > throttleDuration) {
            mClickTime = System.currentTimeMillis()
            onViewClick(v)
        }
    }

    abstract fun onViewClick(v: View)

    companion object {
        private var mClickTime: Long = 0
    }
}