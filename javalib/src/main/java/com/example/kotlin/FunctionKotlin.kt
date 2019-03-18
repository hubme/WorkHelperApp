package com.example.kotlin

/**
 * 方法是一种特殊的函数，它必须通过类的实例调用，
 * 也就是说每个方法可以在方法内部拿到这个方法的实例。这是方法和函数的不同之处。
 * 方法和函数几乎一模一样，唯一的区别就是方法必须声明在类里面。
 *
 * https://mp.weixin.qq.com/s?__biz=MzIzMTYzOTYzNA==&mid=2247483929&idx=1&sn=93ea1cba77d6d59eca7e8707c8369b8e&scene=19#wechat_redirect
 * @author VanceKing
 * @since 19-3-18.
 */

fun thisIsAFunction() = Unit

class FunctionKotlin {

    fun main(args: Array<String>) {

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
    
    fun methodInfix(){
        val a = FunctionKotlin()
        a infixFunction 3
    }
}

