package com.king.app.workhelper.fragment

import android.annotation.SuppressLint
import android.view.View
import com.king.app.workhelper.R
import com.king.app.workhelper.common.AppBaseFragment
import com.king.app.workhelper.databinding.FragmentRxjavaSample2Binding
import io.reactivex.Observable
import java.util.concurrent.TimeUnit


/**
 *
 * @author VanceKing
 * @since 2020/1/16.
 */

@SuppressLint("CheckResult")
class RxJavaSampleFragment2 : AppBaseFragment() {
    private lateinit var mViewBinding: FragmentRxjavaSample2Binding

    override fun getContentLayout(): Int {
        return R.layout.fragment_rxjava_sample2
    }

    override fun getContentView(): View {
        mViewBinding = FragmentRxjavaSample2Binding.inflate(layoutInflater)
        return mViewBinding.root
    }

    override fun initContentView(rootView: View?) {
        super.initContentView(rootView)
        mViewBinding.tvMerger.setOnClickListener { mergeWith() }
        mViewBinding.tvConcat.setOnClickListener { concat() }
        mViewBinding.tvMap.setOnClickListener { flatMap() }
    }

    private fun mergeWith() {
        Observable.just("1", "2", "3")
            .mergeWith(Observable.just("a", "b"))
            .subscribe { println(it) }
    }

    private fun concat() {
        Observable.concat(
            Observable.just("1", "2", "3").delay(1, TimeUnit.SECONDS),
            Observable.just("a", "b"),
            Observable.just("A", "B").delay(2, TimeUnit.SECONDS)
        )
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
        fun newInstance(): androidx.fragment.app.Fragment {
            return RxJavaSampleFragment2()
        }
    }
}