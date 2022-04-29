package com.king.app.workhelper.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * https://www.kotlincn.net/docs/reference/coroutines/flow.html
 *
 * @author VanceKing
 * @since 2022/4/29
 */
class FlowTest {

    @Test
    fun test1() = runBlocking {
        listOf(1, 2, 3).forEach {
            println(it)
        }
    }

    @Test
    fun test2() = runBlocking {
        sequence {
            for (i in 1..3) {
                Thread.sleep(1000)
                yield(i)
            }
        }.forEach { println(it) }
    }

    private fun simple(): Flow<Int> = flow { // 流构建器
        for (i in 1..3) {
            //为什么不会提示：suspend function 'delay' should be called only from a coroutine or another suspend function
            delay(500) // 假装我们在这里做了一些有用的事情
            emit(i) // 发送下一个值
        }
    }

    @Test
    fun test3() = runBlocking {
        launch {
            for (i in 1..6) {
                println("I'm not blocked $i")
                delay(500)
            }
        }

        simple().collect { println(it) }
    }
}