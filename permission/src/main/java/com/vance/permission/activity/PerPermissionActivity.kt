package com.vance.permission.activity

import android.view.View
import com.king.applib.base.BaseActivity
import com.king.applib.log.Logger
import com.vance.permission.R

/**
 *
 * @author VanceKing
 * @since 19-8-8.
 */
class PerPermissionActivity : BaseActivity() {
    override fun getContentLayout(): Int {
        return R.layout.per_activity_permission
    }

    override fun initContentView() {
        super.initContentView()
        setViewClickListeners(R.id.tv_dispatcher, R.id.tv_easypermission)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.tv_dispatcher -> Logger.i("dispatcher")//supportFragmentManager.beginTransaction().replace(R.id.permission_container, )
            R.id.tv_easypermission -> Logger.i("easypermission")
        }
    }
}