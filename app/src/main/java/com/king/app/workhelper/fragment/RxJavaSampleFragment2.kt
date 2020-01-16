package com.king.app.workhelper.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import com.king.app.workhelper.R
import com.king.app.workhelper.common.AppBaseFragment
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_rxjava_sample2.*
import java.util.concurrent.TimeUnit


/**
 *
 * @author VanceKing
 * @since 2020/1/16.
 */

@SuppressLint("CheckResult")
class RxJavaSampleFragment2 : AppBaseFragment() {
    override fun getContentLayout(): Int {
        return R.layout.fragment_rxjava_sample2
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tv_merger.setOnClickListener { mergeWith() }
        tv_concat.setOnClickListener { concat() }
        tv_map.setOnClickListener { flatMap() }
    }


    private fun mergeWith() {
        Observable.just("1", "2", "3")
                .mergeWith(Observable.just("a", "b"))
                .subscribe { println(it) }
    }

    private fun concat() {
        Observable.concat(Observable.just("1", "2", "3").delay(1, TimeUnit.SECONDS),
                Observable.just("a", "b"),
                Observable.just("A", "B").delay(2, TimeUnit.SECONDS))
                .subscribe { println(it) }
    }

    private fun concatWith() {
        Observable.just("1", "2", "3")
                .concatWith(Observable.just("a", "b"))
                .subscribe { println(it) }
    }

    private fun map() {
        Observable.just(1, 2, 3)
                .map { "item:$it" }
                .subscribe { println(it) }
    }

    private fun flatMap() {
        Observable.just(1, 2, 3, 4)
                .flatMap {
                    val list = mutableListOf<String>()
                    for (i in 0 until it) {
                        list.add("$it")
                    }
                    println("size: ${list.size}")
                    Observable.fromIterable(list)//.delay(it.toLong(), TimeUnit.SECONDS)
                    //Observable.just("${it.toDouble().pow(2.toDouble())}")
                }
                .doOnComplete { println("complete") }
                .subscribe { println(it) }

        /*val list = listOf(1, 2, 3, 4, 5, 6)
        Observable.fromIterable(list)
                .subscribe { println(it) }*/

    }


    companion object {

        @JvmStatic
        fun newInstance(): Fragment {
            return RxJavaSampleFragment2()
        }
    }
}