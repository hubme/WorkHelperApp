package com.king.app.workhelper.coroutine

import kotlinx.coroutines.*
import org.junit.Test
import kotlin.system.measureTimeMillis

/**
 * https://www.kotlincn.net/docs/reference/coroutines/composing-suspending-functions.html
 * 在概念上，async 就类似于 launch，它启动了一个单独的协程。
 * launch 返回一个 Job 并且不附带任何结果值，而 async 返回一个 Deferred，通过 await() 获取延期的结果。
 * 同时 Deferred 接口继承 Job 接口，可以通过 cancel() 取消协程。
 *
 * @author VanceKing
 * @since 2022/4/25
 */
class CoroutineAsync {

    private suspend fun doSomethingUsefulOne(): Int {
        delay(1000L)
        return 13
    }

    private suspend fun doSomethingUsefulTwo(): Int {
        delay(1000L)
        return 29
    }

    private fun doSomethingUsefulOneAsync() = GlobalScope.async {
        doSomethingUsefulOne()
    }

    private fun doSomethingUsefulTwoAsync() = GlobalScope.async {
        doSomethingUsefulTwo()
    }

    @Test
    fun test1() = runBlocking {
        val time = measureTimeMillis {
            // 两个方法在协程中顺序进行
            val one = doSomethingUsefulOne()
            val two = doSomethingUsefulTwo()
            println("The answer is ${one + two}")
        }
        println("Completed in $time ms")

        /*
        output:
        The answer is 42
        Completed in 2017 ms
         */
    }

    @Test
    fun test2() = runBlocking {
        val time = measureTimeMillis {
            val one = async { doSomethingUsefulOne() }
            val two = async { doSomethingUsefulTwo() }
            println("The answer is ${one.await() + two.await()}")
        }
        println("Completed in $time ms")

        /*
        output:
        The answer is 42
        Completed in 1038 ms
         */
    }

    @Test
    fun test2_1() = runBlocking {
        val time = measureTimeMillis {
            val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
            val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
            one.start() // 启动第一个
            two.start() // 启动第二个
            println("The answer is ${one.await() + two.await()}")
        }
        println("Completed in $time ms")

        /*
        output:
        The answer is 42
        Completed in 1038 ms
         */
    }

    // 注意，在这个示例中我们在函数的右边没有加上 `runBlocking`
    @Test
    fun test2_2() {
        val time = measureTimeMillis {
            // 我们可以在协程外面启动异步执行
            val one = doSomethingUsefulOneAsync()
            val two = doSomethingUsefulTwoAsync()
            // 但是等待结果必须调用其它的挂起或者阻塞
            // 当我们等待结果的时候，这里我们使用 `runBlocking { …… }` 来阻塞主线程
            runBlocking {
                println("The answer is ${one.await() + two.await()}")
            }
        }
        println("Completed in $time ms")

        /*
        output:
        The answer is 42
        Completed in 1190 ms
         */
    }

    //使用 async 的结构化并发
    private suspend fun concurrentSum(): Int = coroutineScope {
        val one = async { doSomethingUsefulOne() }
        val two = async { doSomethingUsefulTwo() }
        one.await() + two.await()
    }

    @Test
    fun test3() = runBlocking {
        val time = measureTimeMillis {
            println("The answer is ${concurrentSum()}")
        }
        println("Completed in $time ms")
    }

    private suspend fun failedConcurrentSum(): Int = coroutineScope {
        val one = async {
            try {
                delay(Long.MAX_VALUE) // 模拟一个长时间的运算
                42
            } finally {
                println("First child was cancelled")
            }
        }

        //第一个 async 以及等待中的父协程都会被取消
        val two = async<Int> {
            println("Second child throws an exception")
            throw ArithmeticException()
        }
        one.await() + two.await()
    }

    @Test
    fun test4() = runBlocking {
        try {
            failedConcurrentSum()
        } catch (e: ArithmeticException) {
            println("Computation failed with ArithmeticException")
        }

        /*
        output:
        Second child throws an exception
        First child was cancelled
        Computation failed with ArithmeticException
         */
    }
}