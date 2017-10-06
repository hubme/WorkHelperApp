package com.king.app.workhelper.fragment;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.log.Logger;
import com.king.applib.util.DateTimeUtil;
import com.king.applib.util.EasyPermission;
import com.king.applib.util.FileUtil;

import java.util.Date;
import java.util.List;

import butterknife.OnClick;

/**
 * 6.0权限.在模拟器上Manifest.permission.CALL_PHONE，无法弹出授权弹窗
 *
 * @author VanceKing
 * @since 2016/11/10
 */
public class PermissionFragment extends AppBaseFragment implements EasyPermission.PermissionCallback {
    public static final int REQ_CODE_PERMISSION_CAMERA = 0;
    public static final int REQ_CODE_PERMISSION_STORAGE = 1;
    public static final int REQ_CODE_PERMISSION_PHONE_SMS = 2;
    public static final int REQ_CODE_ACTIVITY_CAMERA = 3;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_permission;
    }

    @OnClick(R.id.tv_request_storage_permission)
    public void clickStorageBtn() {
        EasyPermission
                .with(this)
                .rationale("需要读写SDCard权限")
                .addRequestCode(REQ_CODE_PERMISSION_STORAGE)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request();
    }

    @OnClick(R.id.tv_request_camera_permission)
    public void clickCameraBtn() {
        EasyPermission
                .with(this)
                .rationale("需要相机权限")
                .addRequestCode(REQ_CODE_PERMISSION_CAMERA)
                .permissions(Manifest.permission.CAMERA)
                .request();
    }

    @OnClick(R.id.tv_request_phone_sms_permission)
    public void clickPhoneSmsBtn() {
        EasyPermission.with(this)
                .rationale("需要电话和短信权限")
                .addRequestCode(REQ_CODE_PERMISSION_PHONE_SMS)
                .permissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EasyPermission.SETTINGS_REQ_CODE:
                Toast.makeText(getContext(), "从设置页面返回", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    /**
     * 打开相机
     */
    private void takePhotoFromCamera() {
        String mCameraPhotoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/" +
                DateTimeUtil.formatDate(new Date(), "yyyyMMdd_HHmmss") + ".jpg";
        Uri uri = Uri.fromFile(FileUtil.createFile(mCameraPhotoPath));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, REQ_CODE_ACTIVITY_CAMERA);
        } else {
            Toast.makeText(getContext(), "手机中未安装拍照应用", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPermissionGranted(int requestCode, List<String> perms) {
        switch (requestCode) {
            case REQ_CODE_PERMISSION_STORAGE:
                Logger.i("读写SDCard权限已授权");
                break;
            case REQ_CODE_PERMISSION_CAMERA:
                Logger.i("相机权限已授权，打开相机");
                takePhotoFromCamera();
                break;
            case REQ_CODE_PERMISSION_PHONE_SMS:
                Logger.i("电话短信已授权");
                break;
            default:
                break;
        }
    }

    @Override
    public void onPermissionDenied(int requestCode, List<String> perms) {
        switch (requestCode) {
            case REQ_CODE_PERMISSION_CAMERA:
                EasyPermission.checkDeniedPermissionsNeverAskAgain(this, "请到设置中打开相机权限", R.string.confirm, R.string.cancel, null, perms);
                break;
            case REQ_CODE_PERMISSION_STORAGE:
                EasyPermission.checkDeniedPermissionsNeverAskAgain(this, "请到设置中打开读写SDCard权限", R.string.confirm, R.string.cancel, null, perms);
                break;
            case REQ_CODE_PERMISSION_PHONE_SMS:
                Toast.makeText(getContext(), "PHONE_SMS", Toast.LENGTH_SHORT).show();
                EasyPermission.checkDeniedPermissionsNeverAskAgain(this, "请到设置中打开电话和短信权限", R.string.confirm, R.string.cancel, null, perms);
                break;
            default:
                break;
        }
    }
}