package com.king.app.workhelper.kotlin

/**
 *
 * @author huoguangxu
 * @since 19-7-18.
 */
fun main() {
    lambda()
}

fun lambda() {
    run { println("Hello Kotlin") }
}

fun lambda1() {
    { println("Hello Kotlin") }.invoke()
}

fun lambda2() {
    { println("Hello Kotlin") }()
}

fun lambda3() {
    //{参数名: 参数类型 -> Lambda体}()
    { str: String -> println(str) }("VanceKing")
}

fun lambda4() {
    { name: String, age: Int -> println("name = $name, age = $age") }("VanceKing", 18)
}

fun lambd5() {
    //Lambda 变量的签名不需要写参数名，只需要写类型
    val value1: (String, Int) -> Int
    val value2 = { name: String, age: Int ->
        println("name = $name, age = $age")
        age
    }
}