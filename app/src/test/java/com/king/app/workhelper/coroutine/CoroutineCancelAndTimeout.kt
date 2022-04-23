package com.king.app.workhelper.coroutine

import kotlinx.coroutines.*
import org.junit.Test

/**
 * 协程取消和超时。
 * https://www.kotlincn.net/docs/reference/coroutines/cancellation-and-timeouts.html
 *
 * @author VanceKing
 * @since 2022/4/23
 */
class CoroutineCancelAndTimeout {
    @Test
    fun cancel_1() = runBlocking {
        val job = launch {
            repeat(1000) { i ->
                println("job: I'm sleeping $i ...")
                delay(500L)
            }
        }
        delay(1300L) // 延迟一段时间
        println("main: I'm tired of waiting!")
        job.cancel() // 取消该作业
        job.join() // 等待作业执行结束
        println("main: Now I can quit.")

        /*output:
        job: I'm sleeping 0 ...
        job: I'm sleeping 1 ...
        job: I'm sleeping 2 ...
        main: I'm tired of waiting!
        main: Now I can quit.
         */
    }

    @Test
    fun cancel_2() = runBlocking {
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0
            while (i < 5) { // 一个执行计算的循环，只是为了占用 CPU
                // 每秒打印消息两次
                if (System.currentTimeMillis() >= nextPrintTime) {
                    println("job: I'm sleeping ${i++} ...")
                    nextPrintTime += 500L
                }
            }
        }
        delay(1300L) // 等待一段时间
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消一个作业并且等待它结束。但 launch 并不会立即停止，还是会持续打印
        println("main: Now I can quit.")

        /*output:
        job: I'm sleeping 0 ...
        job: I'm sleeping 1 ...
        job: I'm sleeping 2 ...
        main: I'm tired of waiting!
        job: I'm sleeping 3 ...
        job: I'm sleeping 4 ...
        main: Now I can quit.
         */
    }

    @Test
    fun cancel_2_1() = runBlocking {
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0
            while (isActive) { // 可以被取消的计算循环
                // 每秒打印消息两次
                if (System.currentTimeMillis() >= nextPrintTime) {
                    println("job: I'm sleeping ${i++} ...")
                    nextPrintTime += 500L
                }
            }
        }
        delay(1300L) // 等待一段时间
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消一个作业并且等待它结束。
        println("main: Now I can quit.")

        /*output:
        job: I'm sleeping 0 ...
        job: I'm sleeping 1 ...
        job: I'm sleeping 2 ...
        main: I'm tired of waiting!
        main: Now I can quit.
         */
    }

    @Test
    fun cancel_3() = runBlocking {
        val job = launch {
            try {
                repeat(1000) { i ->
                    println("job: I'm sleeping $i ...")
                    delay(500L)
                }
            } finally {
                println("job: I'm running finally")
            }

        }
        delay(1300L) // 延迟一段时间
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消该作业并且等待它结束
        println("main: Now I can quit.")

        /*output:
        job: I'm sleeping 0 ...
        job: I'm sleeping 1 ...
        job: I'm sleeping 2 ...
        main: I'm tired of waiting!
        job: I'm running finally
        main: Now I can quit.
         */
    }

    @Test
    fun cancel_3_1() = runBlocking {
        val job = launch {
            try {
                repeat(1000) { i ->
                    println("job: I'm sleeping $i ...")
                    delay(500L)
                }
            } finally {
                delay(1000L)
                //下面一行不会打印
                println("job: I'm running finally")
            }

        }
        delay(1300L) // 延迟一段时间
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消该作业并且等待它结束
        println("main: Now I can quit.")

        /*output:
        job: I'm sleeping 0 ...
        job: I'm sleeping 1 ...
        job: I'm sleeping 2 ...
        main: I'm tired of waiting!
        main: Now I can quit.
         */
    }

    @Test
    fun cancel_3_2() = runBlocking {
        val job = launch {
            try {
                repeat(1000) { i ->
                    println("job: I'm sleeping $i ...")
                    delay(500L)
                }
            } finally {
                withContext(NonCancellable) {
                    println("job: I'm running finally")
                    delay(1000L)
                    println("job: And I've just delayed for 1 sec because I'm non-cancellable")
                }
            }
        }
        delay(1300L) // 延迟一段时间
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消该作业并等待它结束
        println("main: Now I can quit.")

        /*output:
        job: I'm sleeping 0 ...
        job: I'm sleeping 1 ...
        job: I'm sleeping 2 ...
        main: I'm tired of waiting!
        job: I'm running finally
        job: And I've just delayed for 1 sec because I'm non-cancellable
        main: Now I can quit.
         */
    }

    @Test
    fun timeout_1() = runBlocking {
        withTimeout(1300L) {
            repeat(1000) {
                println("I'm sleeping $it ...")
                delay(500L)
            }
        }
    }

    @Test
    fun timeout_1_1() = runBlocking {
        try {
            withTimeout(1300L) {
                repeat(1000) {
                    println("I'm sleeping $it ...")
                    delay(500L)
                }
            }
        } catch (e: TimeoutCancellationException) {
            println("超时异常信息：$e")
        }

        /*
        output:
        I'm sleeping 0 ...
        I'm sleeping 1 ...
        I'm sleeping 2 ...
        超时异常信息：kotlinx.coroutines.TimeoutCancellationException: Timed out waiting for 1300 ms
         */
    }

    @Test
    fun timeout_2() = runBlocking {
        val result = withTimeoutOrNull(1300L) {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
            "Done" // 在它运行得到结果之前取消它
        }
        println("Result is $result")
    }
}