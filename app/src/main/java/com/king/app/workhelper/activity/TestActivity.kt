package com.king.app.workhelper.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import com.king.app.workhelper.R
import com.king.app.workhelper.common.AppBaseActivity
import com.king.applib.log.Logger
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
        val text = "狂赛季疯狂是反对龙kjdfoiwejflsd龙井扥塞拉芬零件sdklfjiowejfslef双看扥令赛季反对s神霜扥sljsfsdsdlkfjsldf狂赛季疯狂是反对龙kjdfoiwejflsd龙井扥塞拉芬零件sdklfjiowejfslef双看扥令赛季反对s神霜扥sljsfsdsdlkfjsldf"

        val transitionDrawable = TransitionDrawable(arrayOf(ColorDrawable(Color.RED), ColorDrawable(Color.GREEN), ColorDrawable(Color.BLUE)))
        tv_text.text = text
        tv_text.background = transitionDrawable
        tv_text.setOnClickListener { transitionDrawable.startTransition(300) }
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
}
