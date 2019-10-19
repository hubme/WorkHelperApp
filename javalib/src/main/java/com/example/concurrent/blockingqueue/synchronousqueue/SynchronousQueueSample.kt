package com.example.concurrent.blockingqueue.synchronousqueue

import java.util.concurrent.SynchronousQueue

/**
 *
 * @author Vanceking
 * @since 19-10-19.
 */

fun main() {
    test()
}

private fun test() {
    val queue = SynchronousQueue<Int>()

    Thread(ProduceTask(queue)).start()
    
    Thread.sleep(300)
    Thread(ConsumeTask(queue)).start()

}

private class ProduceTask(val queue: SynchronousQueue<Int>) : Runnable {
    private var mNumber = 0

    override fun run() {
        while (mNumber < 50) {
            println("produce $mNumber")
            queue.put(mNumber)
            mNumber++
        }
    }

}

private class ConsumeTask(private val queue: SynchronousQueue<Int>) : Runnable {
    override fun run() {
        while (true) {
            val value = queue.take()
            println("consume $value")
        }
    }
}