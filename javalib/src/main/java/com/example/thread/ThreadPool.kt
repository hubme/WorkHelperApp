package com.example.thread

import java.util.concurrent.Executors
import java.util.logging.Logger

fun main(args: Array<String>) {
    val threadPool = Executors.newFixedThreadPool(1)
    threadPool.execute {
        Thread.sleep(1000)
        //println("执行了第一个任务。")
        //Logger.getGlobal().info("执行了第一个任务。")
        throw java.lang.RuntimeException("第一个任务报错了。")
    }
    threadPool.execute {
        Thread.sleep(2000)
        //println("执行了第二个任务。")
        Logger.getGlobal().info("执行了第二个任务。")
    }
}