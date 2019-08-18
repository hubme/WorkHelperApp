package com.vance.permission.fragment

import android.content.Intent
import android.view.View
import android.widget.TextView
import com.king.applib.base.BaseFragment
import com.king.applib.util.EasyPermission
import com.vance.permission.PermissionUtil
import com.vance.permission.R

/**
 * 非官方封装库。只需一个类文件。
 *
 * @author VanceKing
 * @since 19-8-11.
 */
class CustomPermissionFragment : BaseFragment(), EasyPermission.PermissionCallback {

    override fun getContentLayout(): Int {
        return R.layout.per_fragment_permission
    }

    override fun initContentView(view: View) {
        super.initContentView(view)
        view.findViewById<TextView>(R.id.per_tv_camera).setOnClickListener {
            doCameraTask()
        }
    }

    private fun doCameraTask() {
        if (PermissionUtil.hasPermissions(context!!, PermissionUtil.PERMISSION_CAMERA)) {
            showToast("已获取相机权限")
            return
        }
        EasyPermission.with(this)
                .rationale("需要相机权限")
                .addRequestCode(REQ_CODE_CAMERA)
                .permissions(PermissionUtil.PERMISSION_CAMERA)
                .request()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EasyPermission.SETTINGS_REQ_CODE) {
            showToast("从设置页面返回")
        }
    }

    override fun onPermissionGranted(requestCode: Int, perms: MutableList<String>?) {
        if (requestCode == REQ_CODE_CAMERA) {
            showToast("已获取相机权限")
        }
    }

    override fun onPermissionDenied(requestCode: Int, perms: MutableList<String>?) {
        if (requestCode == REQ_CODE_CAMERA) {
            val neverAskAgain = EasyPermission.checkDeniedPermissionsNeverAskAgain(this, "", perms)
            if (neverAskAgain) {
                EasyPermission.showSettingsDialog(this, "没有拍照，请到设置中打开相机权限")
            }
        }
    }

    companion object {
        const val TAG = "CustomPermissionFragment"
        const val REQ_CODE_CAMERA = 0x10
        val instance: CustomPermissionFragment
            get() = CustomPermissionFragment()
    }

}