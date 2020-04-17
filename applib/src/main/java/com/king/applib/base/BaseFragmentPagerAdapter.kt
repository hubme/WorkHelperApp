package com.king.applib.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * 通用 FragmentPagerAdapter。
 *
 * @author VanceKing
 * @since 2020/3/18.
 */
open class BaseFragmentPagerAdapter @JvmOverloads constructor(fm: FragmentManager, behavior: Int = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) :
        FragmentPagerAdapter(fm, behavior) {

    var fragments: List<Fragment>? = null
        set(value) {
            if (value != null && value.isNotEmpty()) {
                field = value
                notifyDataSetChanged()
            }
        }

    var titles: List<String>? = null

    override fun getItem(position: Int): Fragment {
        return fragments!![position]
    }

    override fun getCount(): Int {
        return fragments?.size ?: 0
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles?.let { it[position] }
    }
}