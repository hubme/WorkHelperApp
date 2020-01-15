package com.example.rxjava

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.processors.PublishProcessor


/**
 *
 * @author VanceKing
 * @since 2020/1/15.
 */
fun main() {
    zipWith()
}

/**
 * 创建自定义事件
 */
private fun create() {
    val processor = PublishProcessor.create<Boolean>()
    val subscribe = processor.startWith(false)
            .distinctUntilChanged()
            .subscribe { println(if (it) "changed" else "unchanged") }
    processor.onNext(true)
}

private fun zip() {
    val o1 = Observable.range(1, 3).doOnComplete { println("Observable1") }
    val o2 = Observable.fromArray("A", "B", "C").doOnComplete { println("Observable2") }
    val subscribe = Observable.zip<Int, String, String>(o1, o2,
            BiFunction { integer, s -> "$integer$s" })
            .subscribe { println(it) }

}

private fun zipWith() {
    val o1 = Observable.range(1, 3).doOnComplete { println("Observable1") }
    val o2 = Observable.just("A", "B", "C").doOnComplete { println("Observable2") }
    //o1.zipWith(o2, BiFunction { t1, t2, t3 -> "" })
    val subscribe = o1.zipWith<String, String>(o2,
            BiFunction<Int, String, String> { integer: Int, s: String -> "$integer$s" })
            .subscribe { println(it) }
}