package com.king.app.workhelper.activity

import androidx.appcompat.app.AppCompatDelegate
import com.king.app.workhelper.R
import com.king.app.workhelper.common.AppBaseActivity
import kotlinx.android.synthetic.main.activity_night_theme.*

/**
 * 黑色主题
 * @author VanceKing
 * @since 2020/1/8.
 */
class NightThemeActivity : AppBaseActivity() {
    override fun getContentLayout(): Int {
        return R.layout.activity_night_theme
    }

    override fun initContentView() {
        super.initContentView()
        floating_button.setOnClickListener { changeTheme() }
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