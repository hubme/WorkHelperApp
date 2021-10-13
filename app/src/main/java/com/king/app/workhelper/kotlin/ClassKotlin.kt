package com.king.app.workhelper.kotlin

import java.util.*

/**
 *https://www.kotlincn.net/docs/reference/classes.html
 *
 * @author VanceKing
 * @since 2019/4/17.
 */
fun main() {


}


//主构造函数是类头的一部分：它跟在类名后。
//class ClassBean public @Inject constructor(name: String, age: Int)
/*class ClassBean constructor(name: String, age: Int) {
    var isEmpty: Boolean
        get() = true
        set(value) {
            false
        }

    //初始化块中的代码实际上会成为主构造函数的一部分
    init {
        println("Person")
    }
}*/

//如果主构造函数没有任何注解或者可见性修饰符，可以省略这个 constructor 关键字。
//主构造的参数可以在初始化块中使用。它们也可以在类体内声明的属性初始化器中使用
class Person2(name: String, age: Int) {
    val firstProperty = "First property: $name".also(::println)

    //初始化块
    init {
        println("First initializer block that prints $name")
    }

    val secondProperty = "Second property: ${name.length}".also(::println)

    init {
        println("Second initializer block that prints ${name.length}")
    }

    //次构造函数
    //如果类有一个主构造函数，每个次构造函数需要委托给主构造函数，
    // 可以直接委托或者通过别的次构造函数间接委托。
    // 委托到同一个类的另一个构造函数用 this 关键字即可
    constructor(id: String) : this("", 21)
}

//如果一个非抽象类没有声明任何（主或次）构造函数，它会有一个生成的不带参数的主构造函数。构造函数的可见性是 public
class DontCreateMe private constructor()


open class Foo {
    lateinit var subject: String

    @Volatile
    var name: String? = null

    open val x: Int = 2

    open val y: Int get() = 1

    fun f() {
        if (::subject.isInitialized) {

        }
    }
}

class Bar : Foo() {
    override var x: Int = 3

    //内部类
    inner class Baz {
        fun g() {
            super@Bar.f() // 调用 Foo 实现的 f()
            println(super@Bar.x) // 使用 Foo 实现的 x 的 getter
        }
    }
}

open class A {
    open fun f() {
        print("A")
    }

    fun a() {
        print("a")
    }
}

interface B {
    fun f() {
        print("B")
    } // 接口成员默认就是“open”的

    fun b() {
        print("b")
    }
}

class C() : A(), B {
    // 编译器要求覆盖 f()：
    override fun f() {
        super<A>.f() // 调用 A.f()
        super<B>.f() // 调用 B.f()
        println("C#f()")
    }
}

abstract class D {
    abstract fun test()
}