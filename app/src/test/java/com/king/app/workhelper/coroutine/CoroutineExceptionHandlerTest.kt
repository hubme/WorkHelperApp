package com.king.app.workhelper.coroutine

import kotlinx.coroutines.*
import org.junit.Test

/**
 * 在 coroutineScope 中，只要任意一个子协程发生异常，整个 scope 都会执行失败，并且其余的所有子协程都会被取消掉；
 * 在 supervisorScope 中，一个子协程的异常不会影响整个 scope的执行，也不会影响其余子协程的执行；
 *
 * @author VanceKing
 * @since 2022/4/28
 */
class CoroutineExceptionHandlerTest {

    @Test
    fun test1() = runBlocking {
        val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
            println("异常信息。coroutineContext: $coroutineContext throwable: $throwable")
        }

        val job = CoroutineScope(handler).launch {
            val job1 = async() {
                println("begin: job1")
                delay(1000)
                throw RuntimeException("遇到了错误。")
            }

            val job2 = async() {
                println("begin: job2")
                delay(2000)
                println("end: job2")
            }
        }

        job.join()
    }

    @Test
    fun test2() = runBlocking {

        supervisorScope {
            val job1 = async() {
                println("begin: job1")
                delay(1000)
                throw RuntimeException("遇到了错误。")
            }

            val job2 = async() {
                println("begin: job2")
                delay(2000)
                println("end: job2")
            }
            job1.await()
            job2.await()
        }

        /*
        调用 await() 时才会抛出异常
        output:
        begin: job1
        begin: job2
        end: job2
        */
    }

    @Test
    fun test2_1() = runBlocking {

        supervisorScope {
            val job1 = launch {
                println("begin: job1")
                delay(1000)
                throw RuntimeException("遇到了错误。")
            }

            val job2 = launch {
                println("begin: job2")
                delay(2000)
                println("end: job2")
            }
        }

        /*
        output:
        begin: job1
        begin: job2
        Exception in thread "Test worker @coroutine#2" java.lang.RuntimeException: 遇到了错误。
        */

    }

    @Test
    fun test3() = runBlocking {
        coroutineScope {
            val job1 = async() {
                println("begin: job1")
                delay(1000)
                throw RuntimeException("遇到了错误。")
            }

            val job2 = async() {
                println("begin: job2")
                delay(2000)
                println("end: job2")
            }
        }

        /*
        会抛出异常。
        begin: job1
        begin: job2

        遇到了错误。
        java.lang.RuntimeException: 遇到了错误。
         */
    }

    fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

    @Test
    fun test4() = runBlocking(CoroutineName("main")) {
        log("Started main coroutine")
        // 运行两个后台值计算
        val v1 = async(CoroutineName("v1coroutine")) {
            delay(500)
            log("Computing v1")
            252
        }
        val v2 = async(CoroutineName("v2coroutine")) {
            delay(1000)
            log("Computing v2")
            6
        }
        log("The answer for v1 / v2 = ${v1.await() / v2.await()}")

        /*
        output:
        [Test worker @main#1] Started main coroutine
        [Test worker @v1coroutine#2] Computing v1
        [Test worker @v2coroutine#3] Computing v2
        [Test worker @main#1] The answer for v1 / v2 = 42
         */
    }

    @Test
    fun test5() = runBlocking {
        val threadLocal = ThreadLocal<String?>()
        threadLocal.set("main")

        println("Pre-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
        val job = launch(Dispatchers.Default + threadLocal.asContextElement(value = "launch")) {
            println("Launch start, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
            yield()
            println("After yield, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
        }
        job.join()
        println("Post-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")

        /*
        output:
        Pre-main, current thread: Thread[Test worker @coroutine#1,5,main], thread local value: 'main'
        Launch start, current thread: Thread[DefaultDispatcher-worker-1 @coroutine#2,5,main], thread local value: 'launch'
        After yield, current thread: Thread[DefaultDispatcher-worker-1 @coroutine#2,5,main], thread local value: 'launch'
        Post-main, current thread: Thread[Test worker @coroutine#1,5,main], thread local value: 'main'
         */
    }
}