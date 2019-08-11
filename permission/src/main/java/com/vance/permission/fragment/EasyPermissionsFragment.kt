package com.vance.permission.fragment

import android.content.Intent
import android.view.View
import android.widget.TextView
import com.king.applib.base.BaseFragment
import com.king.applib.util.ToastUtil
import com.vance.permission.PermissionUtil
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


/**
 * https://github.com/googlesamples/easypermissions
 *
 * 实现有些臃肿，有 17 个类文件。
 * @author VanceKing
 * @since 19-8-8.
 */

class EasyPermissionsFragment : BaseFragment(), EasyPermissions.PermissionCallbacks {

    override fun getContentLayout(): Int {
        return com.vance.permission.R.layout.per_fragment_permission
    }

    override fun initContentView(view: View) {
        super.initContentView(view)
        val smsTv = view.findViewById<TextView>(com.vance.permission.R.id.per_tv_sms)
        smsTv.setOnClickListener { doSmsTask() }
        val cameraTv = view.findViewById<TextView>(com.vance.permission.R.id.per_tv_camera)
        cameraTv.setOnClickListener { doCameraTask() }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            showToast("短信，从设置界面返回")
        }
    }

    override fun onPermissionsGranted(requestCode: Int, list: List<String>) {
        if (requestCode == RC_PERMISSION_SMS) {
            ToastUtil.showShort("已经有短信权限了")
        }

    }

    override fun onPermissionsDenied(requestCode: Int, list: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, list)) {
            AppSettingsDialog.Builder(this).setRationale("需要权限才能使用，请在设置中打开")
                    .build().show()
        }
    }

    private fun doSmsTask() {
        if (EasyPermissions.hasPermissions(context!!, PermissionUtil.PERMISSION_SEND_SMS)) {
            ToastUtil.showShort("已经有短信权限了")
        } else {
            EasyPermissions.requestPermissions(this, "需要短信权限", RC_PERMISSION_SMS, PermissionUtil.PERMISSION_SEND_SMS)
        }
    }

    @AfterPermissionGranted(RC_PERMISSION_CAMERA)
    private fun doCameraTask() {
        if (EasyPermissions.hasPermissions(context!!, PermissionUtil.PERMISSION_CAMERA)) {
            ToastUtil.showShort("已经有相机权限了")
        } else {
            EasyPermissions.requestPermissions(this, "需要相机权限", RC_PERMISSION_CAMERA, PermissionUtil.PERMISSION_CAMERA)
        }
    }

    companion object {
        const val TAG = "EasyPermissionsFragment"

        const val RC_PERMISSION_CAMERA = 0x10
        const val RC_PERMISSION_SMS = 0x11

        val instance: EasyPermissionsFragment
            get() = EasyPermissionsFragment()
    }
}
