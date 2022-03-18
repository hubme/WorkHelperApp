package com.king.app.workhelper.kotlin

import java.util.*

fun main() {

}

//结合 lambda 表达式
fun testCollection() {
    val fruits = listOf("aaa", "banana", "avocado", "apple", "kiwifruit")
    fruits.asSequence().filter { it.startsWith("a") }
            .sortedBy { it }
            .map { it.uppercase(Locale.getDefault()) }
            .forEach { println(it) }
}

fun testCollection2() {
    val items = listOf("aaa", "bbb", null, 123)
    items.asSequence()
            /*.filter { it != null }*/ //不会 NPE
            .forEach { println(it.toString()) }
}


fun testArray() {
    val array1 = arrayOf(1, 2, 3)
    for ((index, value) in array1.withIndex()) {
        println("the element at $index is $value")
    }

    val array2 = arrayOfNulls<String>(3)
    val array3 = arrayListOf<String>()
    val array4 = longArrayOf()

    // 创建一个 Array<String> 初始化为 ["0", "1", "4", "9", "16"]
    val array5 = Array(5) { i -> (i * i).toString() }
    array5.forEach { println(it) }

    val x = 1
    when (x) {
        1 -> print("x == 1")
        2 -> print("x == 2")
        else -> print("x is neither 1 nor 2")

    }
}

fun testString() {
    val text = """
        
 |sdfdsf\n
        >sdf\\tsd
        |sdf
    """.trimMargin(">")
    println(text)
}

fun testWhen() {

}