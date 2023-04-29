package com.king.app.workhelper.activity

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import com.king.app.workhelper.common.AppBaseActivity
import com.king.app.workhelper.constant.GlobalConstant
import com.king.app.workhelper.databinding.ActivityTouchEventBinding
import com.king.applib.log.Logger

/**
 * View 事件页面。
 *
 * @author VanceKing
 * created at 2023/3/10
 */
class TouchEventActivity : AppBaseActivity() {
    private lateinit var mBinding: ActivityTouchEventBinding

    override fun getContentView(): View {
        mBinding = ActivityTouchEventBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Logger.i("onCreate width: ${mBinding.tvView1.width}")
        mBinding.tvView1.post {
            Logger.i("post width: ${mBinding.tvView1.width}")
        }
    }

    override fun onResume() {
        super.onResume()
        Logger.i("onResume width: ${mBinding.tvView1.width}")
    }

    override fun initContentView() {
        super.initContentView()
        mBinding.tvView4.setOnClickListener {
            Log.i(TAG, "tvView4 clicked ")
        }

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.i(TAG, "TouchEventActivity#dispatchTouchEvent")
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.i(TAG, "TouchEventActivity#onTouchEvent")
        return super.onTouchEvent(event)
    }
}

class MyViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.i(GlobalConstant.LOG_TAG, "MyViewGroup#onInterceptTouchEvent")
        return super.onInterceptTouchEvent(ev)
        //return true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.i(GlobalConstant.LOG_TAG, "MyViewGroup#onTouchEvent")
        return super.onTouchEvent(event)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Logger.i("MyViewGroup#onDraw")
    }
}

class MyLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.i(GlobalConstant.LOG_TAG, "MyLinearLayout#onInterceptTouchEvent")
        return super.onInterceptTouchEvent(ev)
        //return true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.i(GlobalConstant.LOG_TAG, "MyLinearLayout#onTouchEvent")
        return super.onTouchEvent(event)
    }

}

class MyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.i(GlobalConstant.LOG_TAG, "MyView#onTouchEvent")
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {

            }

            MotionEvent.ACTION_UP -> {

            }
        }
        return super.onTouchEvent(event)
    }

}