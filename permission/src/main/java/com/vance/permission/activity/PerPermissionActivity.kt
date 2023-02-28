package com.vance.permission.activity

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.biz_common.ROUTE_PERMISSION_MAIN
import com.king.applib.base.BaseActivity
import com.vance.permission.R
import com.vance.permission.databinding.PerActivityPermissionBinding
import com.vance.permission.fragment.CustomPermissionFragment
import com.vance.permission.fragment.EasyPermissionsFragment
import com.vance.permission.fragment.NormalPermissionFragment
import com.vance.permission.fragment.PermissionsDispatcherFragment

/**
 *
 * @author VanceKing
 * @since 19-8-8.
 */
@Route(path = ROUTE_PERMISSION_MAIN)
open class PerPermissionActivity : BaseActivity() {
    private lateinit var mViewBinding: PerActivityPermissionBinding

    override fun getContentView(): View? {
        mViewBinding = PerActivityPermissionBinding.inflate(layoutInflater)
        return mViewBinding.root
    }

    override fun initContentView() {
        super.initContentView()
        showFragment(CustomPermissionFragment.TAG)
        setViewClickListeners(
            R.id.tv_normal,
            R.id.tv_dispatcher,
            R.id.tv_easypermission,
            R.id.tv_custom_easypermission
        )
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.tv_normal -> showFragment(NormalPermissionFragment.TAG)
            R.id.tv_dispatcher -> showFragment(PermissionsDispatcherFragment.TAG)
            R.id.tv_easypermission -> showFragment(EasyPermissionsFragment.TAG)
            R.id.tv_custom_easypermission -> showFragment(CustomPermissionFragment.TAG)
        }
    }

    private fun showFragment(tag: String) {
        var fragment = supportFragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            fragment = when (tag) {
                PermissionsDispatcherFragment.TAG -> PermissionsDispatcherFragment.getInstance()
                EasyPermissionsFragment.TAG -> EasyPermissionsFragment.instance
                NormalPermissionFragment.TAG -> NormalPermissionFragment.getInstance()
                CustomPermissionFragment.TAG -> CustomPermissionFragment.instance
                else -> null
            }
        }
        fragment?.let {
            if (it.isVisible) {//当前正在显示
                return
            }
            hiddenFragments()
            if (!it.isAdded) {//判断是否已经添加到当前 Activity
                supportFragmentManager.beginTransaction()
                        .add(R.id.permission_container, it, tag)
                        .commitAllowingStateLoss()
            } else {
                supportFragmentManager.beginTransaction()
                        .show(fragment)
                        .commitAllowingStateLoss()
            }
        }

    }

    private fun hiddenFragments() {
        if (supportFragmentManager.fragments.isEmpty()) {
            return
        }
        val beginTransaction = supportFragmentManager.beginTransaction()
        for (fragment in supportFragmentManager.fragments) {
            if (!fragment.isHidden) {
                beginTransaction.hide(fragment)
            }
        }
        beginTransaction.commitAllowingStateLoss()
    }
}