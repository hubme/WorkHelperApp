package com.king.app.workhelper.dialog;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.View;

import com.king.app.workhelper.R;
import com.king.applib.dialog.BaseBottomDialog;
import com.king.applib.util.AppUtil;
import com.king.applib.util.ContextUtil;
import com.king.applib.util.EasyPermission;
import com.king.applib.util.ExtendUtil;
import com.king.applib.util.ToastUtil;

import java.io.File;
import java.util.List;

/**
 * @author huoguangxu
 * @since 2017/6/15.
 */

public class PhotoBottomDialog extends BaseBottomDialog implements View.OnClickListener, EasyPermission.PermissionCallback {
    private static final String PHOTO_NAME = "selected_photo.jpg";
    private static final long MAX_PHOTO_SIZE = 5 * 1024 * 1024;

    /** 调用相机请求码 */
    public static final int REQ_CODE_CAMERA = 0x100;
    /** 调用相册请求码 */
    public static final int REQ_CODE_ALBUM = 0x101;
    /** 相册权限请求码 */
    private static final int REQ_CODE_PERM_CAMERA = 0x123;
    /** sdcard权限请求码 */
    private static final int REQ_CODE_PERM_STORAGE = 0x124;

    private Object mHost;

    @Override public int getLayoutRes() {
        return R.layout.dialog_bottom_photo;
    }

    @Override public void bindView(View v) {
        v.findViewById(R.id.tv_photo_camera).setOnClickListener(this);
        v.findViewById(R.id.tv_photo_album).setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_photo_camera:
                requestPermission();
                break;
            case R.id.tv_photo_album:
                requestPermission();
                break;
        }
    }

    @Override public void onPermissionGranted(int requestCode, List<String> perms) {
        switch (requestCode) {
            case REQ_CODE_PERM_CAMERA:
                openCamera();
                break;
            case REQ_CODE_PERM_STORAGE:
                openAlbumForPhoto();
                break;
            default:
                break;
        }
    }

    @Override public void onPermissionDenied(int requestCode, List<String> perms) {

    }

    public void setHost(Object host) {
        if (host instanceof Activity || host instanceof Fragment) {
            mHost = host;
        } else {
            throw new RuntimeException("Host must be either Activity or Fragment.");
        }
    }

    protected void openCamera() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            ToastUtil.showLong("无法访问SD卡");
            return;
        }
        File file = new File(ContextUtil.getAppContext().getCacheDir(), PHOTO_NAME);
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, MAX_PHOTO_SIZE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        } else {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(ContextUtil.getAppContext(), AppUtil.getFileProviderAuthor(), file));
        }

        if (ExtendUtil.canResolveActivity(intent)) {
            setResult(intent, REQ_CODE_CAMERA);
        } else {
            ToastUtil.showLong("手机中未安装拍照应用");
        }
    }

    /**
     * 从相册选择照片
     */
    protected void openAlbumForPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        if (ExtendUtil.canResolveActivity(intent)) {
            setResult(intent, REQ_CODE_ALBUM);
        } else {
            ToastUtil.showLong("手机中未安装相册应用");
        }
    }

    private void requestPermission() {
        EasyPermission easyPermission;
        if (mHost instanceof Fragment) {
            easyPermission = EasyPermission.with((Fragment) mHost);
        } else {
            easyPermission = EasyPermission.with((Activity) mHost);
        }
        easyPermission
                .rationale("需要相机和访问照片的权限")
                .addRequestCode(REQ_CODE_PERM_CAMERA)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .request();
    }

    private void setResult(Intent intent, int requestCode) {
        if (mHost instanceof Fragment) {
            ((Fragment) mHost).startActivityForResult(intent, requestCode);
        } else if (mHost instanceof Activity) {
            ((Activity) mHost).startActivityForResult(intent, requestCode);
        }
    }
}
