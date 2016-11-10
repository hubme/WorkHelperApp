package com.king.app.workhelper.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.log.Logger;
import com.king.applib.util.DateTimeUtil;
import com.king.applib.util.FileUtil;

import java.util.Date;

import butterknife.OnClick;

/**
 * 6.0权限
 * Created by HuoGuangxu on 2016/11/10.
 */

public class PermissionFragment extends AppBaseFragment {
    public static final int REQUEST_CODE_PERMISSION = 1024;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.hello_world)
    public void clickBtn() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            /*
            帮我们判断接下来的对话框是否包含”不再询问“选择框。
            1.第一次询问,没有“不再询问”选项, 拒绝,返回true
            2.以后询问,有“不再询问”选项，不勾选，拒绝，返回true;勾选，拒绝，返回false
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(getContext()).setMessage("请授权")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("授权", new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION);
                            }
                        })
                        .show();
            } else {
                //记得在manifest中加入READ_PHONE_STATE，否则会自动拒绝授权，不会出现弹窗
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION);
            }

        }
//        takePhotoFromCamera(0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                    boolean sdfsd = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    if (!sdfsd) {
                        Toast.makeText(getContext(), "到设置中打开权限", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(), "授权成功", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                Logger.i("相机返回");
                break;
        }
    }

    /**
     * 打开相机
     */
    private void takePhotoFromCamera(int requestCode) {
        String mCameraPhotoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/" +
                DateTimeUtil.formatDate(new Date(), "yyyyMMdd_HHmmss") + ".jpg";
        Uri uri = Uri.fromFile(FileUtil.createFile(mCameraPhotoPath));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, requestCode);
        } else {
            Toast.makeText(getContext(), "手机中未安装拍照应用", Toast.LENGTH_SHORT).show();
        }
    }
}
