package com.king.applib.ui.recyclerview.listener

import android.graphics.Rect
import android.support.annotation.IdRes
import android.support.annotation.Nullable
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import org.jetbrains.annotations.NotNull

/**
 * RecyclerView item 和 ChildView 点击事件，基于 TouchEvent 实现。
 *
 * @author VanceKing
 * @since 2019/11/14.
 */
open class RecyclerViewItemTouchListener<out T : RecyclerViewItemTouchListener.ClickListener>(
        @NotNull val recycleView: RecyclerView,
        @Nullable @IdRes private val childViewIDs: IntArray?,
        @NotNull protected val mClickListener: T)
    : RecyclerView.OnItemTouchListener {

    private val gestureDetector: GestureDetector by lazy {
        GestureDetector(recycleView.context, object : GestureDetector.SimpleOnGestureListener() {

            override fun onSingleTapUp(event: MotionEvent): Boolean = true

            override fun onLongPress(event: MotionEvent) {
                with(recycleView) {
                    findChildViewUnder(event.x, event.y)?.let {
                        mClickListener.onLongClick(it, getChildAdapterPosition(it))
                    }
                }
            }
        })
    }

    interface ClickListener {
        fun onItemClick(view: View, position: Int)
        fun onChildClick(view: View, position: Int)
        fun onLongClick(view: View, position: Int)
    }

    open class ClickListenerAdapter : ClickListener {
        override fun onItemClick(view: View, position: Int) {

        }

        override fun onChildClick(view: View, position: Int) {

        }

        override fun onLongClick(view: View, position: Int) {

        }

    }

    override fun onInterceptTouchEvent(recyclerView: RecyclerView, event: MotionEvent): Boolean {
        if (gestureDetector.onTouchEvent(event)) {
            recyclerView.findChildViewUnder(event.x, event.y)?.let {
                val listPosition = recyclerView.getChildAdapterPosition(it)
                val childView: View? = findExactChild(it, event.rawX, event.rawY, getSpecialViewClickPadding())
                if (listPosition != RecyclerView.NO_POSITION) {
                    if (childView != null) {
                        mClickListener.onChildClick(childView, listPosition)
                    } else {
                        mClickListener.onItemClick(it, listPosition)
                    }
                    return true
                }
            }
        }

        return false
    }

    private fun findExactChild(@Nullable view: View?, x: Float, y: Float, specialViewClickPadding: Int): View? {
        if (view == null || view !is ViewGroup) {
            return null
        }

        if (childViewIDs == null || childViewIDs.isEmpty()) {
            return null
        }

        for (specialViewId in childViewIDs) {
            val specialView: View? = view.findViewById(specialViewId)
            specialView?.let {
                val viewBounds = Rect()
                specialView.getGlobalVisibleRect(viewBounds)
                if (x >= viewBounds.left - specialViewClickPadding &&
                        x <= viewBounds.right + specialViewClickPadding &&
                        y >= viewBounds.top - specialViewClickPadding &&
                        y <= viewBounds.bottom + specialViewClickPadding) {
                    return specialView
                }
            }
        }
        return null
    }

    open fun getSpecialViewClickPadding() = 0

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}