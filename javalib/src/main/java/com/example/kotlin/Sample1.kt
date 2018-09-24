package com.example.kotlin

import java.io.File

/**
 *
 * @author VanceKing
 * @since 2018/9/22.
 */
/*class Sample1 {
    //无效。不识别，无法运行
    fun main(args: Array<String>) {
        println("哈哈哈")
    }
}*/

//直接写在文件中
fun main(args: Array<String>) {


    
}

fun sum(a: Int, b: Int): Int {
    return a + b
}

//将表达式作为函数体、返回值类型自动推断的函数
fun sum2(a: Int, b: Int) = a + b

fun maxOf(a: Int, b: Int) = if (a > b) a else b

//Unit 返回类型可以省略
fun printSum(a: Int, b: Int): Unit {
    println("sum of $a and $b is ${a + b}")
}

//扩展函数。"text".spaceAAA()
fun String.spaceAAA(){
    println("spaceAAA")
}

fun defineVar() {
    var a: Int = 1//没必要写类型，自动推导
    var b = "AAA"
    var c = ""
    val d = "哈哈哈"//final 类型变量

    println("a = $a, b = $b, c = $c, d = $d")

    if (a > 0) {
        c = "BBB"
    }

    println("a = $a, b = $b, c = $c")

    // 模板中的简单名称：
    val s1 = "a is $a"
    println(s1)

    // 模板中的任意表达式：
    val s2 = "${s1.replace("is", "was")}, but now is $a"
    println(s2)
}

//当某个变量的值可以为 null 的时候，必须在声明处的类型后添加 ? 来标识该引用可为 null
fun split(str: String?, start: Int, end: Int): String {
    if (str == null) {
        return ""
    }
    return str.substring(start, end)
}

//for 循环额
fun testLisOf() {
    val items = listOf("a", "b", "c")
    for (item in items) {
        println("item is $item")
    }

    for (index in items.indices) {
        println("item at $index is ${items[index]}")
    }


}

//when 表达式。同 Switch
fun describe(obj: Any): Any =
        when (obj) {
            0, 1 -> println("One")
            "Hello" -> "Greeting"
            is Long -> "Long"
            !is String -> "Not a string"
            else -> "Unknown"
        }

fun testWhen() {
    describe(0)
    println(describe(1))
    println(describe("Hello"))
}

fun testRange(value: Int) {
    if (value in 0..10) {//闭区间
        println("[]: $value in [0, 10]")
    }

    if (value !in 0..10) {
        println("not in: $value not in [0, 10]")
    }

    //不包含10，相当于for循环
    for (value in 0 until 10) {
        println("until: $value in [0, 10)")
    }

    //相当于 value+=2
    for (value in 0..10 step 2) {
        println("step: $value")
    }

    for (value in 0 until 10 step 2) {
        println("step: $value")
    }

    for (x in 10 downTo 5) {
        println("$x")
    }

    for (x in 10 downTo 5 step 2) {
        println("$x")
    }

    val map = mapOf("a" to 1, "b" to 2, "c" to 3)
    println(map["b"])
    for ((k, v) in map) {
        println("$k --- $v")
    }
}

//结合 lambda 表达式
fun testCollection() {
    val fruits = listOf("aaa", "banana", "avocado", "apple", "kiwifruit")
    fruits.asSequence().filter { it.startsWith("a") }
            .sortedBy { it }
            .map { it.toUpperCase() }
            .forEach { println(it) }
}

fun testCollection2() {
    val items = listOf("aaa", "bbb", null, 123)
    items.asSequence()
            /*.filter { it != null }*/ //不会 NPE
            .forEach { println(it.toString()) }
}

fun testSaveNull() {
    val files = File("path").listFiles()
    println(files?.size)//If not null 缩写
    println(files?.size ?: "empty")//If not null and else 缩写
    println(files?.size ?: println("files == null"))//else 分支执行语句
    println(files?.firstOrNull() ?: "== null")//else 分支执行语句

    files?.let { println("不为 null 时才执行") } ?: println("为 null 时执行")
}