package com.king.applib.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

inline fun <reified T : Activity> Context.openActivity(configIntent: Intent.() -> Unit = {}) {
    startActivity(Intent(this, T::class.java).apply(configIntent))
}

@SuppressLint("WrongConstant")
private fun Context.getUid(packageName: String): Int {
    try {
        return packageManager.getApplicationInfo(packageName, PackageManager.GET_ACTIVITIES).uid
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return -1
}