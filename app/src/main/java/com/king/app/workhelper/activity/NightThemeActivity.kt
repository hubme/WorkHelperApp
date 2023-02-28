package com.king.app.workhelper.activity

import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.king.app.workhelper.common.AppBaseActivity
import com.king.app.workhelper.databinding.ActivityNightThemeBinding

/**
 * 黑色主题
 * @author VanceKing
 * @since 2020/1/8.
 */
class NightThemeActivity : AppBaseActivity() {
    private lateinit var mViewBinding: ActivityNightThemeBinding

    override fun getContentView(): View {
        mViewBinding = ActivityNightThemeBinding.inflate(layoutInflater)
        return mViewBinding.root
    }

    override fun initContentView() {
        super.initContentView()
        mViewBinding.floatingButton.setOnClickListener { changeTheme() }
    }

    private fun changeTheme() {
        val nightMode = AppCompatDelegate.getDefaultNightMode()
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        recreate()
    }
}