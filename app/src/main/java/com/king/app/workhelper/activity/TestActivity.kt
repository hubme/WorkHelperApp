package com.king.app.workhelper.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Annotation
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.king.app.workhelper.R
import com.king.app.workhelper.adapter.recyclerview.SimpleRecyclerAdapter
import com.king.app.workhelper.common.AppBaseActivity
import com.king.applib.log.Logger
import kotlinx.android.synthetic.main.activity_test.*


/**
 * @author VanceKing
 * @since 2017/12/11.
 */

class TestActivity : AppBaseActivity() {
    private lateinit var mAdapter: SimpleRecyclerAdapter

    override fun getContentLayout(): Int {
        return R.layout.activity_test
    }

    override fun initData() {

    }

    override fun initContentView() {
        super.initContentView()

        //禁止 AppbarLayout 滚动方法1
        (appbar_layout.getChildAt(0).layoutParams as AppBarLayout.LayoutParams).apply {
            scrollFlags = 0
        }

        //禁止 AppbarLayout 滚动方法2
        ((appbar_layout.layoutParams as CoordinatorLayout.LayoutParams).behavior as AppBarLayout.Behavior)
                .setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
                    override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                        return false
                    }
                })

        if (ViewCompat.isLaidOut(appbar_layout)) {
            val params: CoordinatorLayout.LayoutParams = appbar_layout.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior as AppBarLayout.Behavior
            behavior.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
                override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                    return false
                }
            })
        }

        mAdapter = SimpleRecyclerAdapter().apply {
            adapterData = SimpleRecyclerAdapter.fakeData(25)
        }

        with(recycler_view) {
            layoutManager = LinearLayoutManager(this@TestActivity)
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }

    //由于用户操作，应用进入后台时(home 键)调用，在 onPause() 之前执行
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        Logger.i("onUserLeaveHint")
    }

    //reified 关键字来表示该泛型要进行实化
    inline fun <reified T : Activity> openActivity() {
        startActivity(Intent(this, T::class.java))
    }

    /**
     * Get the resource identifier for an annotation.
     * @param context The context this should be executed in.
     */
    private fun Annotation.getResId(context: Context): Int {
        return context.resources.getIdentifier(value, null, context.packageName)
    }
}
