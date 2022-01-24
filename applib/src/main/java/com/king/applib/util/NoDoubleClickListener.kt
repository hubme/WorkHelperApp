package com.king.applib.util

import android.view.View

/**
 * 过滤 View 重复点击的监听器。
 *
 * @author VanceKing
 * @since 2022/1/24
 */
abstract class NoDoubleClickListener : View.OnClickListener {
    private var mLastClickTime: Long = 0
    private var mViewId = -1

    override fun onClick(v: View) {
        val currentTime = System.currentTimeMillis()
        if (mViewId != v.id) {
            mViewId = v.id
            mLastClickTime = currentTime
            onNoDoubleClick(v)
        } else if (currentTime - mLastClickTime > MIN_CLICK_DELAY_MS) {
            mLastClickTime = currentTime
            onNoDoubleClick(v)
        }
    }

    protected abstract fun onNoDoubleClick(v: View)

    companion object {
        const val MIN_CLICK_DELAY_MS = 1000
    }
}