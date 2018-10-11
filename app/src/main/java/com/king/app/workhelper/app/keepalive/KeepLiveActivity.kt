package com.king.app.workhelper.app.keepalive

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import com.king.app.workhelper.constant.GlobalConstant

/**
 *
 * @author VanceKing
 * @since 2018/10/11.
 */
class KeepLiveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(GlobalConstant.LOG_TAG, "KeepLiveActivity#onCreate()")

        KeepLiveManager.keepLiveActivity = this

        // 设置Activity在左上角
        window.setGravity(Gravity.START)

        // 设置window的像素为1
        window.attributes.run {
            x = 0
            y = 0
            width = 1
            height = 1
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(GlobalConstant.LOG_TAG, "KeepLiveActivity#onDestroy()")
    }

}