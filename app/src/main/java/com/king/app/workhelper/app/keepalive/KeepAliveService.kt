package com.king.app.workhelper.app.keepalive

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder

/**
 *
 * @author huoguangxu
 * @since 2018/10/11.
 */
class KeepAliveService : Service() {
    var aliveReceiver: KeepAliveReceiver? = null

    override fun onCreate() {
        super.onCreate()
        aliveReceiver = KeepAliveReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF)
        intentFilter.addAction(Intent.ACTION_USER_PRESENT)
        registerReceiver(aliveReceiver, intentFilter)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(aliveReceiver)
    }
}