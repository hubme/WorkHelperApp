package com.example.thread

fun main() {
    val thread = Thread(InterruptRunnable())
    thread.start()
    Thread.sleep(5000)
    thread.interrupt()
}

private class InterruptRunnable : Runnable {
    override fun run() {
        try {
            while (!Thread.currentThread().isInterrupted) {
                println("Running");
                Thread.sleep(1000);
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

}