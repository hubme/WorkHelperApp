package com.king.app.workhelper.kotlin

import java.util.*

/**
 * 作用域函数。
 *
 * 1. The way to refer to the context object.
 *    this: run, apply, with; it: let, also
 * 2. The return value.
 *    2.1 apply and also return the context object.
 *    2.2 let, run, and with return the lambda result.
 *
 * https://www.kotlincn.net/docs/reference/scope-functions.html
 *
 * The Kotlin standard library contains several functions whose sole purpose is
 * to execute a block of code within the context of an object.
 * When you call such a function on an object with a lambda expression provided,
 * it forms a temporary scope. In this scope, you can access the object without its name.
 * Such functions are called scope functions.
 * There are five of them: let, run, with, apply, and also.
 *
 * Here is a short guide for choosing scope functions depending on the intended purpose:
 * 1. Executing a lambda on non-null objects: let
 * 2. Introducing an expression as a variable in local scope: let
 * 3. Object configuration: apply
 * 4. Object configuration and computing the result: run
 * 5. Running statements where an expression is required: non-extension run
 * 6. Additional effects: also
 * 7. Grouping function calls on an object: with
 *
 * @author VanceKing
 * @since 2019/10/22.
 */
fun main() {
    testRepeat()
}

fun testLet1() {
    val numbers = mutableListOf("one", "two", "three", "four", "five")
    numbers.filter { it.length > 3 }.let { println(it) }
    numbers.filter { it.length > 3 }.let(::println)
}

fun testLet2() {
    val str: String? = "Hello"
    val length = str?.let {
        //'it' is not null inside '?.let { }'
        println("let() called on $it")
        it.length
    }
    print("length = $length")
}

fun testLet3() {
    val numbers = listOf("one", "two", "three", "four")
    val modifiedFirstItem = numbers.first().let { firstItem ->
        println("The first item of the list is '$firstItem'")
        if (firstItem.length >= 5) firstItem else "!$firstItem!"
    }.toUpperCase(Locale.US)
    println("First item after modifications: '$modifiedFirstItem'")
}

fun testApply() {
    Person().apply {
        println(this)
        this.age = 20
        println(this)
    }
}

fun testThisOrIt() {
    val str = "Hello"
    // this
    str.run {
        //println("The receiver string length: $length")
        println("run(this). The receiver string length: ${this.length}")
    }

    //this
    str.apply {
        println("apply(this). The receiver string length: ${this.length}")
    }

    //this
    with(str) {
        println("with(this). The receiver string's length is ${this.length}")
    }

    //it
    str.let {
        println("let(it). The receiver string's length is ${it.length}")
    }

    //it
    str.also {
        println("also(it). The receiver string's length is ${it.length}")
    }

}

//apply、also 都返回上下文对象，可以链式调用
fun testApplyAlso() {
    val numberList = mutableListOf<Double>()
    numberList.also { println("Populating the list") }
            .apply {
                add(2.71)
                add(3.14)
                add(1.0)
            }
            .also { println("Sorting the list") }
            .sort()
}

//The context object is available as an argument (it). The return value is the object itself.
fun testAlso() {
    val numbers = mutableListOf("one", "two", "three")
    numbers.also { println("The list elements before adding new one: $it") }
            .add("four")
            .also { println("The list elements after adding new one: $it") }
}

fun testLetRunWith() {
    val numbers = mutableListOf("one", "two", "three")
    val countEndsWithE = numbers.run {
        add("four")
        add("five")
        //返回 Int 类型
        count { it.endsWith("e") }
    }
    println("There are $countEndsWithE elements that end with e.")
}

//takeIf returns this object if it matches the predicate. 
fun testTakeIf1() {
    val number = Random().nextInt(100)

    val evenOrNull = number.takeIf { it % 2 == 0 }
    val oddOrNull = number.takeUnless { it % 2 == 0 }
    println("even: $evenOrNull, odd: $oddOrNull")
}

//takeIf 和 lef 结合使用可以实现链式调用。类似 if() ...
fun taskTakeIf2(input: String, sub: String) {
    input.indexOf(sub).takeIf { it >= 0 }?.let {
        println("The substring $sub is found in $input.")
        println("Its start position is $it.")
    }
}

fun testRepeat() {
    repeat(5) {
        println("repeat")
    }
}