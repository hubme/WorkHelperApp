package com.king.app.workhelper.app.keepalive

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.king.app.workhelper.constant.GlobalConstant

/**
 *
 * @author VanceKing
 * @since 2018/10/11.
 */
class KeepAliveReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.i(GlobalConstant.LOG_TAG, "action = ${intent.action}")

        when (intent.action) {
            // 锁屏
            Intent.ACTION_SCREEN_OFF -> {
                KeepLiveManager.startKeepLiveActivity(context)
            }

            // 解锁
            Intent.ACTION_USER_PRESENT -> {
                KeepLiveManager.finishKeepLiveActivity()
            }

        }
    }
}