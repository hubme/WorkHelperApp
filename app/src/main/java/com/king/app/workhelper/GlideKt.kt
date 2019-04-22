package com.king.app.workhelper

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager

/**
 *
 * @author VanceKing
 * @since 2019/4/18.
 */

//Context 扩展属性
val Context.glide: RequestManager
    get() = Glide.with(this)