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
    queue.offer(Message("id-1", "msg_1", 6))
    queue.offer(Message("id-2", "msg_2", 2))
    queue.offer(Message("id-3", "msg_3", 5))
    queue.offer(Message("id-4", "msg_4", 1))

    Thread(Consumer(queue)).start()
}

private class Message(private val id: String, private val message: String, private val delayDuration: Long) : Delayed {

    val time: Long = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(delayDuration)

    override fun getDelay(unit: TimeUnit): Long {
        return unit.convert(this.time - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
    }

    override fun compareTo(other: Delayed): Int {
        return (this.time - (other as Message).time).toInt()
    }

    override fun toString(): String {
        return "Message{" +
                "id=" + id +
                ", message='" + message + '\''.toString() +
                ", time=" + time +
                '}'.toString() + System.currentTimeMillis()
    }
}

private class Consumer(private val queue: DelayQueue<Message>) : Runnable {
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

