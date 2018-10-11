package com.king.app.workhelper.app.keepalive

import android.content.Context
import android.content.Intent

/**
 *
 * @author VanceKing
 * @since 2018/10/11.
 */
object KeepLiveManager {
    var keepLiveActivity: KeepLiveActivity? = null

    fun startKeepLiveActivity(context: Context) {
        val intent = Intent(context, KeepLiveActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun finishKeepLiveActivity() {
        keepLiveActivity?.finish()
    }
}