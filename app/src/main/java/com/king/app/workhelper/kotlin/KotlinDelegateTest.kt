package com.king.app.workhelper.kotlin

import java.util.*
import kotlin.properties.Delegates

//定义一个接口
interface InterfaceBase {
    fun print(i: Int)
}

//定义接口的实现类
class InterfaceBaseImpl(var index: Int) : InterfaceBase {
    override fun print(i: Int) {
        println("impl index is $index, params is $i")
    }
}

//类似java方式的kotlin委托实现
class InterfaceBaseDelegate(private val base: InterfaceBase) : InterfaceBase {
    override fun print(i: Int) {
        base.print(i)
    }
}

//kotlin推荐的类委托实现
//通过 by 关键字就能省略类体达到上面类似java方式的实现
class InterfaceKotlinDelegate(private val base: InterfaceBase) : InterfaceBase by base

//kotlin推荐的类委托实现的重写
class InterfaceKotlinDelegate1(private val base: InterfaceBase) : InterfaceBase by base {
    override fun print(i: Int) {
        //优先使用自己的实现
        println("override--params is $i, Delegate class is ${base.javaClass}")
        //自己实现又调用了委托对象的实现
        base.print(i)
    }
}

fun main() {
    val base = InterfaceBaseImpl(2)
    base.print(1)

    val base1 = InterfaceBaseDelegate(base)
    base1.print(4)

    val base2 = InterfaceKotlinDelegate(base)
    base2.print(5)

    val base3 = InterfaceKotlinDelegate1(base)
    base3.print(6)

    var name: String by Delegates.notNull()

    var age: Int by Delegates.observable(10) { property, oldValue, newValue ->
        println("property name is ${property.name}, old value is $oldValue, new value is $newValue")
    }

}

class Result(map: Map<String, Any?>) {
    val name: String by map
    val address: String by map
    val age: Int by map
    val date: Date by map


    override fun toString(): String {
        return "{name: ${this.name}, address: ${this.address}, " +
                "age: ${this.age}, date: ${this.date}}"
    }
}