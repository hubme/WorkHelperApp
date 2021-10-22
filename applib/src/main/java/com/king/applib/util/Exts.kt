package com.king.applib.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.util.TypedValue

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
        TypedValue.COMPLEX_UNIT_SP, this,
        Resources.getSystem().displayMetrics
    ).toInt()

fun SharedPreferences.spEdit(action: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    action(editor)
    editor.apply()
}

inline fun <reified T : Activity> Context.openActivity(configIntent: Intent.() -> Unit = {}) {
    startActivity(Intent(this, T::class.java).apply(configIntent))
}