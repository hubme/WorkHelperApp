package com.king.app.workhelper

import kotlinx.coroutines.*
import org.junit.Test
import kotlin.system.measureTimeMillis

class CoroutineSample {
    @Test
    fun aaa() = runBlocking {
        val job = launch { // 启动一个新协程并保持对这个作业的引用
            delay(1000L)
            println("World!")
        }
        println("Hello,")
        job.join() // 等待直到子协程执行结束

    }

    @Test
    fun coroutineScope() = runBlocking {
        coroutineScope { // 创建一个协程作用域
            launch {
                delay(1000L)
                println("Task from nested launch")
            }

            delay(500L)
            println("Task from coroutine scope") // 这一行会在内嵌 launch 之前输出
        }
    }

    @Test
    fun cancelCoroutine() = runBlocking {
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
        //job.cancelAndJoin() //job.cancel() 和 job.join() 的合并方法
        println("main: Now I can quit.")

    }

    @Test
    fun withTimeoutSample() = runBlocking {
        // 超时抛出 TimeoutCancellationException 异常
        withTimeout(1300L) {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        }

    }

    @Test
    fun withTimeoutOrNullSample() = runBlocking {
        // 超时，返回 null
        val result = withTimeoutOrNull(1300L) {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
            "Done" // 在它运行得到结果之前取消它
        }
        println("Result is $result")
    }

    @Test
    fun measureTimeMillisSample() = runBlocking {
        val result = measureTimeMillis {
            val async1 = async(start = CoroutineStart.LAZY) {
                println("async1 start.")
                val value = heavyWorkOne()
                println("async1 end.")
                value
            }
            val async2 = async(start = CoroutineStart.LAZY) {
                println("async2 start.")
                val value = heavyWorkTwo()
                println("async2 end.")
                value
            }
            delay(100L)
            println("aaa")

            // 如果不调用 start()，后面调用 await() 会按顺序执行
            async1.start()
            async2.start()
            println("The answer is ${async1.await() + async2.await()}")
        }
        println("Result is $result")
    }

    private suspend fun heavyWorkOne(): Int {
        delay(500)
        return 500
    }

    private suspend fun heavyWorkTwo(): Int {
        delay(400)
        return 400
    }

    private fun heavyWorkOneAsync() = GlobalScope.async { heavyWorkOne() }

    private fun heavyWorkTwoAsync() = GlobalScope.async { heavyWorkTwo() }

    private suspend fun failedConcurrentSum(): Int = coroutineScope {
        val one = async {
            try {
                delay(Long.MAX_VALUE) // 模拟一个长时间的运算
                42
            } finally {
                println("First child was cancelled")
            }
        }
        val two = async<Int> {
            println("Second child throws an exception")
            throw ArithmeticException()
        }
        one.await() + two.await()
    }

    @Test
    fun failedConcurrentSumTest() = runBlocking<Unit> {
        try {
            failedConcurrentSum()
        } catch (e: ArithmeticException) {
            println("Computation failed with ArithmeticException")
        }
    }

    @Test
    fun dispatchersSample() = runBlocking<Unit> {
        launch { // 运行在父协程的上下文中，即 runBlocking 主协程
            println("runBlocking: ${Thread.currentThread().name}")
        }
        launch(Dispatchers.Unconfined) { // 不受限的——将工作在主线程中
            println("Unconfined: ${Thread.currentThread().name}")
        }
        launch(Dispatchers.Default) { // 将会获取默认调度器
            println("Default: ${Thread.currentThread().name}")
        }
        launch(Dispatchers.IO) {
            println("IO: ${Thread.currentThread().name}")
        }
        SupervisorJob()
    }

    @Test
    fun coroutineScopeSample() = runBlocking {
        val request = launch {
            // GlobalScope 不受父协程的影响
            GlobalScope.launch {
                println("job1: I run in GlobalScope and execute independently!")
                delay(1000)
                println("job1: I am not affected by cancellation of the request")
            }
            // 继承父协程的上下文
            launch {
                delay(100)
                println("job2: I am a child of the request coroutine")
                delay(1000)
                println("job2: I will not execute this line if my parent request is cancelled")
            }
        }
        delay(500)
        request.cancel()
        delay(1000)
        println("main: Who has survived request cancellation?")
    }

    @Test
    fun testDispatchersUnconfined() = runBlocking<Unit> {
        GlobalScope.launch(Dispatchers.Unconfined) {
            log("")
            withContext(Dispatchers.IO) {
                log("")
            }
            //恢复
            log("")

            //挂起
            withContext(Dispatchers.Default) {
                log("")
            }
            //恢复
            log("")
        }
    }

    @Test
    fun asdfsdf() = runBlocking<Unit> {
        val handler = CoroutineExceptionHandler { _, throwable ->
            log("my coroutineExceptionHandler catch exception, msg = ${throwable.message}")
        }
        val parentJob = GlobalScope.launch(handler) {
            val childJob = launch {
                try {
                    delay(Long.MAX_VALUE)
                } catch (e: CancellationException) {
                    log("catch cancellationException thrown from child launch")
                    log("rethrow cancellationException")
                    throw CancellationException()
                } finally {
                    log("child was canceled")
                }
            }
            //取消子协程
            childJob.cancelAndJoin()
            log("parent is still running")
        }
        parentJob.start()

        Thread.sleep(1000)
    }

    @Test
    fun coroutineNameSample() = runBlocking(CoroutineName("main-coroutine")) {
        log("")
        launch(CoroutineName("launch-coroutine")) {
            log("launch")
        }
        val v1 = async(CoroutineName("async-coroutine")) {
            log("async")
        }

    }

    suspend fun performRequest(request: Int): String {
        delay(1000) // 模仿长时间运行的异步任务
        return "response $request"
    }

    private fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")
}