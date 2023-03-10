package com.king.applib.util

import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Rect
import android.util.TypedValue
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.ViewGroupUtils

/**
 * Kotlin 扩展方法。
 *
 * @author guangxu.huo
 * @since 2020/7/30
 */

val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this,
        Resources.getSystem().displayMetrics
    ).toInt()

val Float.sp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics
    ).toInt()

fun SharedPreferences.spEdit(action: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    action(editor)
    editor.apply()
}

/**
 * View 增加点击区域的扩展方法
 */
fun View.expand(dx: Int, dy: Int) {
    class MultiTouchDelegate(bound: Rect? = null, delegateView: View) :
        TouchDelegate(bound, delegateView) {
        val delegateViewMap = mutableMapOf<View, Rect>()
        private var delegateView: View? = null

        override fun onTouchEvent(event: MotionEvent): Boolean {
            val x = event.x.toInt()
            val y = event.y.toInt()
            var handled = false
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    delegateView = findDelegateViewUnder(x, y)
                }
                MotionEvent.ACTION_CANCEL -> {
                    delegateView = null
                }
            }
            delegateView?.let {
                event.setLocation(it.width / 2f, it.height / 2f)
                handled = it.dispatchTouchEvent(event)
            }
            return handled
        }

        private fun findDelegateViewUnder(x: Int, y: Int): View? {
            delegateViewMap.forEach { entry -> if (entry.value.contains(x, y)) return entry.key }
            return null
        }
    }

    // 获取当前控件的父控件
    val parentView = parent as? ViewGroup
    // 若父控件不是 ViewGroup, 则直接返回
    parentView ?: return

    // 若父控件未设置触摸代理，则构建 MultiTouchDelegate 并设置给它
    if (parentView.touchDelegate == null) parentView.touchDelegate =
        MultiTouchDelegate(delegateView = this)
    post {
        val rect = Rect()
        // 获取子控件在父控件中的区域
        ViewGroupUtils.getDescendantRect(parentView, this, rect)
        // 将响应区域扩大
        rect.inset(-dx, -dy)
        // 将子控件作为代理控件添加到 MultiTouchDelegate 中
        (parentView.touchDelegate as? MultiTouchDelegate)?.delegateViewMap?.put(this, rect)
    }
}

