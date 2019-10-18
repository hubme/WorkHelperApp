package com.example.concurrent.blockingqueue.delayqueue

import java.util.concurrent.DelayQueue
import java.util.concurrent.Delayed
import java.util.concurrent.TimeUnit

/**
 * @author VanceKing
 * @since 2019/10/17.
 */

fun main() {
    test1()
}

fun test1() {
    val queue = DelayQueue<Message>()
    queue.offer(Message(9, "msg_1", 1))
    queue.offer(Message(2, "msg_2", 2))
    queue.offer(Message(3, "msg_3", 5))
    queue.offer(Message(4, "msg_4", 6))

    Thread(Consumer(queue)).start()
}

private class Message(private val id: Int, private val message: String, private val delayDuration: Long) : Delayed {

    val executeTime: Long = TimeUnit.NANOSECONDS.convert(delayDuration, TimeUnit.SECONDS) + System.nanoTime()

    override fun getDelay(unit: TimeUnit): Long {
        return unit.convert(this.executeTime - System.nanoTime(), TimeUnit.NANOSECONDS)
    }

    override fun compareTo(o: Delayed): Int {
        val other = o as Message
        return this.id - other.id
    }

    override fun toString(): String {
        return "Message{" +
                "id=" + id +
                ", message='" + message + '\''.toString() +
                ", executeTime=" + executeTime +
                '}'.toString() + System.currentTimeMillis()
    }
}

private class Consumer(private val queue: DelayQueue<Message>) : Runnable{
    override fun run() {
        println("开始获取消息: " + System.currentTimeMillis())
        while (true) {
            try {
                val message = queue.take()
                println("消费消息: $message")
                if (queue.isEmpty()) {
                    break
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
    }

}

