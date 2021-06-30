package com.king.app.workhelper.common

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.king.applib.util.SingletonHolder

/**
 * FirebaseAnalytics 事件管理类。
 *
 * @author VanceKing
 * @since 2020/6/30
 */
class FirebaseManager private constructor(context: Context) {
    private val mFirebase: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    fun logEvent(name: String, params: Bundle? = null) {
        mFirebase.logEvent(name, params)
    }

    companion object : SingletonHolder<FirebaseManager, Context>(::FirebaseManager) {}

}