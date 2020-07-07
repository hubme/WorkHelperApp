package com.king.app.workhelper.kotlin

import kotlin.reflect.KProperty

/**
 * 属性代理类。
 *
 * @author VanceKing
 * @since 2020/7/7
 */
class Delegate1 {
    //val 只读属性只需要此方法。
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "$thisRef, thank you for delegating '${property.name}' to me!"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("$value has been assigned to '${property.name}' in $thisRef.")
    }
}