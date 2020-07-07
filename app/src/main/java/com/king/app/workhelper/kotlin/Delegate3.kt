package com.king.app.workhelper.kotlin

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 属性代理类。
 *
 * @author VanceKing
 * @since 2020/7/7
 */
class Delegate3 : ReadWriteProperty<Any, String> {
    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        return "$thisRef, thank you for delegating '${property.name}' to me!"
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
        println("$value has been assigned to '${property.name}' in $thisRef.")
    }
}