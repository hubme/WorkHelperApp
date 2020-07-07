package com.king.app.workhelper.kotlin

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * 属性代理类。
 *
 * @author VanceKing
 * @since 2020/7/7
 */
class Delegate2 : ReadOnlyProperty<Any, String> {
    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        return "$thisRef, thank you for delegating '${property.name}' to me!"
    }
}