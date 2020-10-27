package com.king.app.workhelper.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.*
import android.text.Annotation
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import androidx.core.content.ContextCompat
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

        val emptyText = getText(R.string.no_filters_selected) as SpannedString
        val ssb = SpannableStringBuilder(emptyText)
        val annotations = emptyText.getSpans(0, emptyText.length, Annotation::class.java)

        annotations?.forEach { annotation ->
            if (annotation.key == "src") {
                // image span markup
                val id = annotation.getResId(this)
                if (id != 0) {
                    ssb.setSpan(
                            ImageSpan(this, id, ImageSpan.ALIGN_BASELINE),
                            emptyText.getSpanStart(annotation),
                            emptyText.getSpanEnd(annotation),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            } else if (annotation.key == "foregroundColor") {

                // foreground color span markup
                val id = annotation.getResId(this)
                if (id != 0) {
                    ssb.setSpan(
                            ForegroundColorSpan(ContextCompat.getColor(this, id)),
                            emptyText.getSpanStart(annotation),
                            emptyText.getSpanEnd(annotation),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
        }
        tv_text.text = ssb
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
