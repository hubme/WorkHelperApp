package com.vance.permission.fragment;

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
import com.king.applib.util.EasyPermission;
import com.king.applib.util.ToastUtil;
import com.vance.permission.PermissionUtil;
import com.vance.permission.R;

/**
 * 没有经过封装的使用方式。
 * 权限流程：
 * 1. 首次申请，没有权限，rationale = false
 * 2.1. 禁止权限，不勾选，回调 onRequestPermissionsResult()，且 rationale = true(表明用户禁止过该权限，应给予说明)，下次申请时再提醒
 * 2.2. 禁止权限，勾选，在 onRequestPermissionsResult()， 且 rationale = false，给予用户弹窗说明权限
 * 2.2.1. 再次申请时，rationale = false，此时引导用户到设置界面打开权限。
 *
 * @author VanceKing
 * @since 2016/11/10
 */
public class NormalPermissionFragment extends BaseFragment {
    public static final String TAG = "PermissionFragment";

    public static final int REQ_CODE_PERMISSION_SMS = 0x10;
    public static final int SETTINGS_REQ_CODE = 0x20;

    public static NormalPermissionFragment getInstance() {
        return new NormalPermissionFragment();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.per_fragment_permission;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        view.findViewById(R.id.per_tv_sms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSmsTask();
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
                //拒绝权限，且未选中“不再提醒”时提示使用权限的缘由
                if (shouldShowRequestPermissionRationale(PermissionUtil.PERMISSION_READ_SMS)) {
                    showPermissionRationale();
                } else {//拒绝权限，且选中"不再提醒"
                    showSettingDialog();
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
                if (PermissionUtil.hasPermission(getContext(), PermissionUtil.PERMISSION_READ_SMS)) {
                    ToastUtil.showShort("已获取短信权限");
                } else {
                    ToastUtil.showShort("还是没有短信权限");
                }
                break;
            case EasyPermission.SETTINGS_REQ_CODE:
                Toast.makeText(getContext(), "从设置页面返回", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public void doSmsTask() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            ToastUtil.showShort("已获取短信权限");
            return;
        }

        //将回调 Activity#onRequestPermissionsResult()
        //1. 未申请到权限；2. Rationale = false; 推断“用户选择不再询问”
        if (hasSMSPermission()) {
            ToastUtil.showShort("已获取短信权限");
            return;
        }
        if (shouldShowRequestPermissionRationale(PermissionUtil.PERMISSION_READ_SMS)) {
            showPermissionRationale();
        } else {
            // ActivityCompat.requestPermissions(this, new String[]{PERMISSION_READ_SMS}, REQ_CODE_PERMISSION_SMS);
            requestPermissions(new String[]{PermissionUtil.PERMISSION_READ_SMS}, REQ_CODE_PERMISSION_SMS);
        }

    }

    private boolean hasSMSPermission() {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getContext(), PermissionUtil.PERMISSION_READ_SMS);
    }

    private void showPermissionRationale() {
        new AlertDialog.Builder(getContext())
                .setMessage("为了保证应用正常使用，请允许相关权限")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(new String[]{PermissionUtil.PERMISSION_READ_SMS}, REQ_CODE_PERMISSION_SMS);
                    }
                })
                .show();
    }

    private void showSettingDialog() {
        new AlertDialog.Builder(getContext())
                .setMessage("需要相应的权限，请在设置中打开。")
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