package com.example.concurrent.blockingqueue.arrayblockingqueue

import com.example.util.Utility
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.atomic.AtomicInteger

/**
 * BlockingQueue实现生产者消费者模型
 *
 * @author VanceKing
 * @since 2017/4/16.
 */
@Volatile
private var check: Boolean = true
private val printer: Printer = Printer()
private val deadNumber = 10

fun main() {
    test1()
}

private fun test1() {
    val queue = ArrayBlockingQueue<Int>(2)

    val produceTask = Producer(queue)
    Thread(produceTask, "producer1").start()
    Thread(produceTask, "producer2").start()
    Thread(produceTask, "producer3").start()

    val consumeTask = Consumer(queue)
    Thread(consumeTask, "consumer1").start()
//    Thread(Consumer(queue), "consumer2").start()
}

private class Producer(private val queue: BlockingQueue<Int>) : Runnable {
    private val number: AtomicInteger = AtomicInteger(1)
    private val products: MutableList<Int> = Collections.synchronizedList(mutableListOf())

    override fun run() {
        try {
            while (check) {
                /*if (queue.remainingCapacity() == 0) {
                    printer.print(Thread.currentThread().name + "队列满")
                }*/
                val produce = produce()
                if (!check) {
                    printer.print(Thread.currentThread().name + " check 退出")
                    break
                }
                queue.put(produce)
                printer.print(Thread.currentThread().name + " produce: " + produce)
                products.add(produce)
                if (produce == deadNumber) {
                    check = false
                    printer.print("停止")
                    products.forEach { printer.print(it) }
                    break
                }
            }
            printer.print(Thread.currentThread().name + " while 退出")

        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    fun produce(): Int {
        val anInt = number.getAndIncrement()
        Utility.sleep(1000)
        return anInt
    }
}

private class Consumer(private val queue: BlockingQueue<Int>) : Runnable {
    override fun run() {
        try {
            while (true) {
                val consume = queue.take()
                consume(consume)
                printer.print(Thread.currentThread().name + " consume: " + consume)
                if (queue.isEmpty()) {
                    printer.print("队列空")
                    break
                }
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    fun consume(x: Int) {
        Utility.sleep(2000)
    }
}

private class Printer {

    @Synchronized
    fun print(message: Any) {
        println(message)
    }
}

