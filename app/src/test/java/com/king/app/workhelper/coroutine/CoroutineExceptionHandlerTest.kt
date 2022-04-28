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

}