package com.vance.permission.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.king.applib.base.BaseFragment;
import com.king.applib.log.Logger;
import com.king.applib.util.EasyPermission;
import com.king.applib.util.ToastUtil;
import com.vance.permission.R;

import java.util.Locale;

/**
 * 6.0权限.在模拟器上Manifest.permission.CALL_PHONE，无法弹出授权弹窗
 * <p>
 * 权限流程：
 * 1. 首次申请，rationale = false
 * 2. 禁止权限，不勾选，rationale = true(表明用户禁止过该权限，应给予说明)，下次重新申请
 * 3. 禁止权限，勾选，在 onRequestPermissionsResult() 回调中根据 rationale = true，给予用户弹窗说明权限
 * 4. 再次申请时，rationale = false，因为已经弹窗说明过。此时提醒用户到设置界面打开权限。
 *
 * @author VanceKing
 * @since 2016/11/10
 */
public class NormalPermissionFragment extends BaseFragment {
    public static final String TAG = "PermissionFragment";

    public static final int REQ_CODE_PERMISSION_SMS = 0x10;
    public static final int SETTINGS_REQ_CODE = 0x20;

    //在 AndroidManifest.xml 同样要添加相应的权限
    private static final String PERMISSION_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private static final String PERMISSION_PHONE = Manifest.permission.CALL_PHONE;
    private static final String PERMISSION_SEND_SMS = Manifest.permission.SEND_SMS;
    private static final String PERMISSION_READ_SMS = Manifest.permission.READ_SMS;

    private boolean mIsFirst = true;

    public static NormalPermissionFragment getInstance() {
        return new NormalPermissionFragment();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.per_fragment_normal;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        view.findViewById(R.id.tv_sms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSmsClick();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CODE_PERMISSION_SMS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ToastUtil.showShort("已获取短信权限");
            } else {
                //无权限，且选中"不再提醒"
                if (!shouldShowRequestPermissionRationale(PERMISSION_READ_SMS)) {
                    mIsFirst = false;
                    new AlertDialog.Builder(getContext())
                            .setMessage("没有权限用个毛线")
                            .setPositiveButton("确定", null)
                            .show();
                } else {
                    ToastUtil.showShort("拒绝权限，等待下次询问哦");
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SETTINGS_REQ_CODE:
                ToastUtil.showShort("从设置页面返回");
                onSmsClick();
                break;
            case EasyPermission.SETTINGS_REQ_CODE:
                Toast.makeText(getContext(), "从设置页面返回", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

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
            ToastUtil.showShort("已获取短信权限");
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