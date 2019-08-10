package com.vance.permission.fragment;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.king.applib.base.BaseFragment;
import com.vance.permission.R;

/**
 * https://github.com/permissions-dispatcher/PermissionsDispatcher
 *
 * @author VanceKing
 * @since 19-8-8.
 */

//@RuntimePermissions
public class PermissionsDispatcherFragment extends BaseFragment {
    public static final String TAG = "PermissionsDispatcherFragment";

    public static PermissionsDispatcherFragment getInstance() {
        return new PermissionsDispatcherFragment();
    }
            
    @Override
    protected int getContentLayout() {
        return R.layout.per_fragment_permission_dispatcher;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    //    @NeedsPermission(Manifest.permission.CAMERA)
    void showCamera() {
        Toast.makeText(getContext(), "申请权限", Toast.LENGTH_SHORT).show();
    }

    //    @OnPermissionDenied(Manifest.permission.CAMERA)
    void onCameraDenied() {
        Toast.makeText(getContext(), "权限被拒绝了", Toast.LENGTH_SHORT).show();
    }

    /*@OnShowRationale(Manifest.permission.CAMERA)
    void showRationaleForCamera(PermissionRequest request) {
        Toast.makeText(this, "没有权限无法使用呦^-^", Toast.LENGTH_SHORT).show();
    }*/

    //    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void onCameraNeverAskAgain() {
        Toast.makeText(getContext(), "不再询问", Toast.LENGTH_SHORT).show();
    }
}
