package com.king.app.workhelper.kotlin

import java.io.File

/**
 * 方法是一种特殊的函数，它必须通过类的实例调用，
 * 也就是说每个方法可以在方法内部拿到这个方法的实例。这是方法和函数的不同之处。
 * 方法和函数几乎一模一样，唯一的区别就是方法必须声明在类里面。
 *
 * https://mp.weixin.qq.com/s?__biz=MzIzMTYzOTYzNA==&mid=2247483929&idx=1&sn=93ea1cba77d6d59eca7e8707c8369b8e&scene=19#wechat_redirect
 * @author VanceKing
 * @since 19-3-18.
 */

fun main(args: Array<String>) {
    FunctionKotlin().method1(2)
}

fun thisIsAFunction() = Unit

fun reformat(str: String,
             normalizeCase: Boolean = true,
             upperCaseFirstLetter: Boolean = true,
             divideByCamelHumps: Boolean = false,
             wordSeparator: Char = ' ') {
}

//当一个函数调用混用位置参数与命名参数时，所有位置参数都要放在第一个命名参数之前。
// 例如，允许调用 f(1, y = 2) 但不允许 f(x = 1, 2)。
fun testreformat() {
    //使用命名参数我们可以使代码更具有可读性：
    reformat("name",
            normalizeCase = true,
            upperCaseFirstLetter = true,
            divideByCamelHumps = false,
            wordSeparator = '_'
    )
}

class FunctionKotlin {
    val aaa = 2
    var bbb = 3
    val ccc: String = "333"

    fun sum(number1: Int, number2: Int): Int {
        print("sum of $number1 and $number2 is ${number1 + number2}")

        return number1 + number2
    }
    
    //Kotlin 不推断具有块代码体的函数的返回类型
    fun sum2(number1: Int, number2: Int) = number1 + number2

    protected class Nested {
        public val e: Int = 5
    }

    fun max(a: Int, b: Int) = if (a > b) a else b

    fun parseInt(str: String): Int? {
        return Integer.parseInt(str)
    }

    fun getStringLength(obj: Any): Int? {
        if (obj is String) {
            return obj.length
        }
        return null
    }

    fun getStringLength2(obj: Any): Int? {
        if (obj !is String) {
            return null
        }
        return obj.length
    }

    fun arrayMethod() {
        val asc = Array(5) { i -> (i * i).toString() }
        asc.forEach { println(it) }
    }

    fun method() {
        println("This is a function")
    }

    fun method1(args: Any?) {
        println("You have passed an object to this function: $args")
    }

    fun method2(): Unit {

    }

    fun methodWithRetrunValue(): Int {
        return 1
    }

    //如果一个函数不会返回（也就是说只要调用这个函数，
    // 那么在它返回之前程序肯定GG了（比如一定会抛出异常的函数））， 
    // 因此你也不知道返回值该写啥，那么你可以让它返回Nothing：
    fun methodReturnNothing(): Nothing {
        throw RuntimeException("error")
    }

    //泛型
    fun <T> methodWithGenericsParams(t: T): T {
        return t
    }

    //参数默认10
    fun methodWithDefaultParams(range: Int = 5): Int {
        for (i in 0..range step 2) {//中缀表达式
            print(i)
        }
        return range
    }

    fun stepMethod(range: Int = 9) {
        for (number in range downTo 3 step 2) {
            print(number)
        }
    }

    fun forMethod() {
        val items = listOf("aaa", "bbb")
        for (item in items) {
            println(item)
        }
        items.forEach { println(it) }
    }

    fun forMethod2() {
        val items = listOf("a", "b")
        for (index in items.indices) {
            println("item at $index is ${items[index]}")
        }
    }

    fun whileMethod() {
        val items = listOf("a", "b")
        var index = 0
        while (index < items.size) {
            print("item at $index is ${items[index]}")
            index++
        }
    }

    fun whenMethod(obj: Any): String =
            when (obj) {
                1 -> "One"
                "Hello" -> "Greeting"
                is Long -> "Long"
                !is String -> "Not a String"
                else -> "Unknown"
            }


    fun filterMethod() {
        val fruits = listOf("banana", "avocado", "apple", "kiwifruit")
        val firstOrNull = fruits.firstOrNull()
        fruits.filter { it.startsWith("a") }
                .sortedBy { it }
                .map { it.toUpperCase() }
                .forEach { println(it) }
    }

    fun printMap() {
        val map = mapOf("a" to 1, "b" to 2, "c" to 3)
        for ((k, v) in map) {
            println("($k, $v)")
        }
        println(map["b"])
        SingleInstance
    }

    fun methodReturnIncreasedInteger(num: Int): Int {
        return num + 1
    }

    //可以省略返回值和括号
    fun methodReturnIncreasedInteger1(num: Int) = num + 1

    fun square(int: Int) = int * int

    //内部函数
    fun methodWithAnotherFunctionInside() {
        val text = "kotlin"
        fun theMethodInside(int: Int = 10) {
            print(text)
            if (int > 5) {
                theMethodInside(int - 1)
            }
        }
        theMethodInside()
    }

    //infix修饰符，那么它可以使用中缀语法调用
    //这个功能基本就是用于将代码变得“更易阅读”。
    infix fun infixFunction(num: Int) = Unit

    fun methodInfix() {
        val a = FunctionKotlin()
        a infixFunction 3
        val file = File("")
        print(file.totalSpace)

    }

    //扩展函数，只能在当前类中使用
    //eg: "".testExtendMethod()
    fun String.testExtendMethod() {
        print("extend method")
    }
}

