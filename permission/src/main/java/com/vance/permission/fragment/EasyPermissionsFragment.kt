package com.vance.permission.fragment

import android.Manifest
import android.view.View
import android.widget.TextView
import com.king.applib.base.BaseFragment
import com.king.applib.log.Logger
import com.king.applib.util.ToastUtil
import com.vance.permission.R
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions



/**
 * https://github.com/googlesamples/easypermissions
 *
 * @author VanceKing
 * @since 19-8-8.
 */

class EasyPermissionsFragment : BaseFragment(), EasyPermissions.PermissionCallbacks{

    override fun getContentLayout(): Int {
        return R.layout.per_fragment_easypermission
    }

    override fun initContentView(view: View) {
        super.initContentView(view)
        val cameraTv = view.findViewById<TextView>(R.id.per_tv_permission_camera)
        cameraTv.setOnClickListener {
            if (EasyPermissions.hasPermissions(context!!, PERMISSION_CAMERA)) {
                ToastUtil.showShort("已经有相机权限了")
            } else {
                EasyPermissions.requestPermissions(this, "需要相机权限", RC_PERMISSION_CAMERA, PERMISSION_CAMERA)
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, list: List<String>) {
        // Some permissions have been granted
        Logger.i("onPermissionsGranted")
        
    }

    override fun onPermissionsDenied(requestCode: Int, list: List<String>) {
        // Some permissions have been denied
        Logger.i("onPermissionsDenied")
    }
    
    @AfterPermissionGranted(RC_PERMISSION_CAMERA)
    private fun methodRequiresTwoPermission() {
        val perms = arrayOf(PERMISSION_CAMERA)
        if (EasyPermissions.hasPermissions(context!!, *perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "需要相机和位置权限", RC_PERMISSION_LOCATION, *perms)
        }
    }

    companion object {
        const val TAG = "EasyPermissionsFragment"
        const val PERMISSION_CAMERA = Manifest.permission.CAMERA
        const val PERMISSION_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION

        const val RC_PERMISSION_CAMERA = 0x10
        const val RC_PERMISSION_LOCATION = 0x11

        val instance: EasyPermissionsFragment
            get() = EasyPermissionsFragment()
    }
}
