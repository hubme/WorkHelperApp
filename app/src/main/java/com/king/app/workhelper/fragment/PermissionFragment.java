package com.king.app.workhelper.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.log.Logger;
import com.king.applib.util.DateTimeUtil;
import com.king.applib.util.EasyPermission;
import com.king.applib.util.FileUtil;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.OnClick;

/**
 * 6.0权限.在模拟器上Manifest.permission.CALL_PHONE，无法弹出授权弹窗
 * 
 * 权限流程：
 * 1. 首次申请，rationale = false
 * 2. 禁止权限，不勾选，rationale = true(表明用户禁止过该权限，应给予说明)，下次重新申请
 * 3. 禁止权限，勾选，在 onRequestPermissionsResult() 回调中根据 rationale = true，给予用户弹窗说明权限
 * 4. 再次申请时，rationale = false，因为已经弹窗说明过。此时提醒用户到设置界面打开权限。
 * 
 *
 * @author VanceKing
 * @since 2016/11/10
 */
public class PermissionFragment extends AppBaseFragment implements EasyPermission.PermissionCallback {
    public static final int REQ_CODE_PERMISSION_CAMERA = 0x123;
    public static final int REQ_CODE_PERMISSION_STORAGE = 0x124;
    public static final int REQ_CODE_PERMISSION_PHONE_SMS = 0x125;
    public static final int REQ_CODE_PERMISSION_SMS = 100;
    public static final int REQ_CODE_ACTIVITY_CAMERA = 0x127;
    public static final int SETTINGS_REQ_CODE = 0x200;

    //在 AndroidManifest.xml 同样要添加相应的权限
    private static final String PERMISSION_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private static final String PERMISSION_PHONE = Manifest.permission.CALL_PHONE;
    private static final String PERMISSION_SEND_SMS = Manifest.permission.SEND_SMS;
    private static final String PERMISSION_READ_SMS = Manifest.permission.READ_SMS;

    private boolean mIsFirst = true;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_permission;
    }

    @OnClick(R.id.tv_request_storage_permission)
    public void clickStorageBtn() {
        if (!EasyPermission.hasPermissions(getContext(), PERMISSION_STORAGE)) {
            EasyPermission
                    .with(this)
                    .rationale("需要读写SDCard权限")
                    .addRequestCode(REQ_CODE_PERMISSION_STORAGE)
                    .permissions(PERMISSION_STORAGE)
                    .request();
        } else {
            showToast("读写SDCard权限已授权");
        }
    }

    @OnClick(R.id.tv_request_camera_permission)
    public void clickCameraBtn() {
        if (!EasyPermission.hasPermissions(getContext(), PERMISSION_CAMERA)) {
            EasyPermission
                    .with(this)
                    .rationale("需要相机权限")
                    .addRequestCode(REQ_CODE_PERMISSION_CAMERA)
                    .permissions(PERMISSION_CAMERA)
                    .request();
        } else {
            showToast("已经有相机权限");
            takePhotoFromCamera();
        }
    }

    @OnClick(R.id.tv_request_phone_sms_permission)
    public void clickPhoneSmsBtn() {
        if (!EasyPermission.hasPermissions(getContext(), PERMISSION_PHONE, PERMISSION_SEND_SMS, PERMISSION_READ_SMS)) {
            EasyPermission.with(this)
                    .rationale("需要电话和短信权限")
                    .addRequestCode(REQ_CODE_PERMISSION_PHONE_SMS)
                    .permissions(PERMISSION_PHONE, PERMISSION_SEND_SMS, PERMISSION_READ_SMS)
                    .request();
        } else {
            showToast("已经有电话、短信权限");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //EasyPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CODE_PERMISSION_SMS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast("已获取短信权限");
            } else {
                //无权限，且选中"不再提醒"
                if (!shouldShowRequestPermissionRationale(PERMISSION_READ_SMS)) {
                    mIsFirst = false;
                    new AlertDialog.Builder(getContext())
                            .setMessage("没有权限用个毛线")
                            .setPositiveButton("确定", null)
                            .show();
                } else {
                    showToast("拒绝权限，等待下次询问哦");
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SETTINGS_REQ_CODE:
                showToast("从设置页面返回");
                onSmsClick();
                break;
            case EasyPermission.SETTINGS_REQ_CODE:
                Toast.makeText(getContext(), "从设置页面返回", Toast.LENGTH_SHORT).show();
                break;
            case REQ_CODE_ACTIVITY_CAMERA:
                if (data.getData() != null) {
                    Logger.i(data.getData().toString());
                }
                break;
            default:
                break;
        }
    }

    //7.0 上会 android.os.FileUriExposedException
    private void takePhotoFromCamera() {
        String mCameraPhotoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/" +
                DateTimeUtil.formatDate(new Date(), "yyyyMMdd_HHmmss") + ".jpg";
        Uri uri = Uri.fromFile(FileUtil.createFile(mCameraPhotoPath));
        //MediaStore.ACTION_IMAGE_CAPTURE_SECURE 设备在设备锁定状态下捕获图像
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
                showToast("相机权限已授权，打开相机");
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
                EasyPermission.checkDeniedPermissionsNeverAskAgain(this, "请到设置中打开电话和短信权限", R.string.confirm, R.string.cancel, null, perms);
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.tv_sms)
    public void onSmsClick() {
        Logger.i(String.format(Locale.US, "shouldShowRequestPermissionRationale: %s", shouldShowRequestPermissionRationale(PERMISSION_READ_SMS)));
        //如何同时检测多个权限
        int result = ContextCompat.checkSelfPermission(getContext(), PERMISSION_READ_SMS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && result != PackageManager.PERMISSION_GRANTED) {
            //将回调 Activity#onRequestPermissionsResult()
//            ActivityCompat.requestPermissions(this, new String[]{PERMISSION_READ_SMS}, REQ_CODE_PERMISSION_SMS);
            //1. 未申请到权限；2. Rationale = false; 推断“用户选择不再询问”
            if (!mIsFirst && !shouldShowRequestPermissionRationale(PERMISSION_READ_SMS)) {
                showSettingDialog();
            } else {
                requestPermissions(new String[]{PERMISSION_READ_SMS}, REQ_CODE_PERMISSION_SMS);
            }
        } else {
            showToast("已获取短信权限");
        }
    }

    private void showSettingDialog() {
        new AlertDialog.Builder(getContext())
                .setMessage("需要响应的权限，请在设置中打开。")
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openSetting();
                    }
                }).show();
    }

    private void openSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, SETTINGS_REQ_CODE);
    }
}