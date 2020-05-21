package com.king.app.workhelper.location

import android.content.Context

class SomeSingleton(context: Context) {
    private val mContext: Context = context

    companion object {
        @Volatile
        private var instance: SomeSingleton? = null

        fun getInstance(context: Context): SomeSingleton {
            val i = instance
            if (i != null) {
                return i
            }

            return synchronized(this) {
                val i2 = instance
                if (i2 != null) {
                    i2
                } else {
                    val created = SomeSingleton(context)
                    instance = created
                    created
                }
            }
        }
    }
}