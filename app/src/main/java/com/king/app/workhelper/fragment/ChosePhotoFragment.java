package com.king.app.workhelper.fragment;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.log.Logger;
import com.king.applib.util.AppUtil;
import com.king.applib.util.ContextUtil;
import com.king.applib.util.DateTimeUtil;
import com.king.applib.util.EasyPermission;
import com.king.applib.util.FileUtil;
import com.king.applib.util.ToastUtil;

import java.io.File;
import java.util.Date;
import java.util.List;

import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * @author huoguangxu
 * @since 2017/6/16.
 */

public class ChosePhotoFragment extends AppBaseFragment implements EasyPermission.PermissionCallback {
    private static final String PHOTO_NAME = "selected_photo.jpg";
    private static final long MAX_PHOTO_SIZE = 5 * 1024 * 1024;

    /** 相册权限请求码 */
    private static final int REQ_CODE_CAMERA = 0x123;
    /** sdcard权限请求码 */
    private static final int REQ_CODE_ALBUM = 0x124;
    /** 录像权限请求码 */
    private static final int REQ_CODE_VIDEO = 0x125;

    //通过测试发现，相册无法写文件到应用的私有目录(data/data/com.king.app.workhelper/cache/xxx.jpg)。拍照后表现为点击确定后无法回调onActivityResult.
    private File mCameraPhotoFile;
    private File mCameraVideoFile;

    @Override protected int getContentLayout() {
        return R.layout.fragment_chose_photo;
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQ_CODE_ALBUM && data != null) {//通过intent获取图片uri
            Logger.i("album。" + data.getDataString());
            //1.Uri转文件路径
            //2.获取图片大小，判断是否需要压缩
            //3.上传图片/显示图片.Uri.fromFile(mCameraPhotoFile)

        } else if (requestCode == REQ_CODE_CAMERA) {//通过File拿到相机保存的图片
            if (mCameraPhotoFile == null) {
                Logger.i("camera。拍照失败");
            } else {
                Logger.i("camera。路径：" + mCameraPhotoFile.getPath() + "; 图片大小: " + mCameraPhotoFile.length());
            }
            //1.图片压缩
            //2.上传图片/显示图片.Uri.fromFile(mCameraPhotoFile)
        } else if (requestCode == REQ_CODE_VIDEO) {//通过File拿到相机保存的图片
            if (mCameraVideoFile == null) {
                Logger.i("camera。录像失败");
            } else {
                Logger.i("camera。路径：" + mCameraVideoFile.getPath() + "; 录像大小: " + mCameraVideoFile.length());
            }
        }
    }

    @OnClick(R.id.tv_open_camera)
    public void onCameraClick() {
        EasyPermission.with(this)
                .rationale("需要相机的权限")
                .addRequestCode(REQ_CODE_CAMERA)
                .permissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request();
    }

    @OnClick(R.id.tv_open_video)
    public void onVideoClick() {
        EasyPermission.with(this)
                .rationale("需要相机的权限")
                .addRequestCode(REQ_CODE_VIDEO)
                .permissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request();
    }

    @OnClick(R.id.tv_open_album)
    public void onAlbumClick() {
        EasyPermission.with(this)
                .rationale("需要访问照片的权限")
                .addRequestCode(REQ_CODE_ALBUM)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override public void onPermissionGranted(int requestCode, List<String> perms) {
        if (requestCode == REQ_CODE_CAMERA) {
            openCamera();
        }
        if (requestCode == REQ_CODE_VIDEO) {
            openVideo();
        } else if (requestCode == REQ_CODE_ALBUM) {
            openAlbum();
        }
    }

    protected void openCamera() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            ToastUtil.showLong("无法访问SD卡");
            return;
        }

        mCameraPhotoFile = FileUtil.createFile(Environment.getExternalStorageDirectory(), "/DCIM/Camera/" +
                DateTimeUtil.formatDate(new Date(), "yyyyMMdd_HHmmss") + ".jpg");

        //INTENT_ACTION_STILL_IMAGE_CAMERA(录像完后没有确认按钮) ACTION_IMAGE_CAPTURE
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
//        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, MAX_PHOTO_SIZE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraPhotoFile));
        } else {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(ContextUtil.getAppContext(), AppUtil.getFileProviderAuthor(), mCameraPhotoFile));
        }

        if (AppUtil.canResolveActivity(intent)) {
            startActivityForResult(intent, REQ_CODE_CAMERA);
        } else {
            ToastUtil.showLong("手机中未安装拍照应用");
        }
    }

    private void openVideo() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            ToastUtil.showLong("无法访问SD卡");
            return;
        }
        mCameraVideoFile = FileUtil.createFile(Environment.getExternalStorageDirectory(), "/DCIM/Video/" +
                DateTimeUtil.formatDate(new Date(), "yyyyMMdd_HHmmss") + ".mp4");

        //INTENT_ACTION_STILL_IMAGE_CAMERA(录像完后没有确认按钮) INTENT_ACTION_VIDEO_CAMERA
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
//        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, MAX_PHOTO_SIZE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraVideoFile));
        } else {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(ContextUtil.getAppContext(), AppUtil.getFileProviderAuthor(), mCameraVideoFile));
        }

        if (AppUtil.canResolveActivity(intent)) {
            startActivityForResult(intent, REQ_CODE_VIDEO);
        } else {
            ToastUtil.showLong("手机中未安装拍照应用");
        }
    }

    protected void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("image/*");
        if (AppUtil.canResolveActivity(intent)) {
            startActivityForResult(intent, REQ_CODE_ALBUM);
        } else {
            ToastUtil.showLong("手机中未安装相册应用");
        }
    }

    @Override public void onPermissionDenied(int requestCode, List<String> perms) {
        if (requestCode == REQ_CODE_CAMERA) {
            EasyPermission.checkDeniedPermissionsNeverAskAgain(this, "没有拍照，请到设置中打开相机权限^-^",
                    R.string.setting, R.string.cancel, null, perms);
        } else if (requestCode == REQ_CODE_ALBUM) {
            EasyPermission.checkDeniedPermissionsNeverAskAgain(this, "没有访问照片权限，请到设置中打开存储空间权限^-^",
                    R.string.setting, R.string.cancel, null, perms);
        }
    }

}
