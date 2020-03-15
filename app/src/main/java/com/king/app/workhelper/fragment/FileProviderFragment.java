package com.king.app.workhelper.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.log.Logger;
import com.king.applib.util.AppUtil;
import com.king.applib.util.FileUtil;
import com.king.applib.util.ImageUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * FileProvider使用
 *
 * @author VanceKing
 * @since 2017/1/4
 */
public class FileProviderFragment extends AppBaseFragment {
    public static final int REQ_CODE_TAKE_PHOTO_FROM_CAMERA = 1;
    public static final int REQ_CODE_TAKE_PHOTO_FROM_ALBUM = 2;

    private static final String IMAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/000test/img.png";
    private static final String CAMERA_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/000test/camera.png";

    @BindView(R.id.iv_photo)
    public ImageView mPhotoIv;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_file_provider;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        if (data.getData() != null) {
            Logger.i(data.getData().toString() + "==" + ImageUtil.uri2ImageFile(data.getData(), getContext().getContentResolver()));
        }

        switch (requestCode) {
            case REQ_CODE_TAKE_PHOTO_FROM_CAMERA:
                mPhotoIv.setImageBitmap(ImageUtil.getBitmap(FileUtil.getFileByPath(CAMERA_PATH)));
                break;
            case REQ_CODE_TAKE_PHOTO_FROM_ALBUM:
                Uri uri = data.getData();
                Logger.i("uri: " + uri.toString());
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.tv_open_camera)
    public void openCamera(TextView textView) {
        takePhotoFromCamera();
    }

    @OnClick(R.id.tv_open_gallery)
    public void openGallery(TextView textView) {
        takePhotoFromAlbum();
    }

    private void takePhotoFromCamera() {
        File file = FileUtil.createFile(CAMERA_PATH);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        } else {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            //authority需要和manifest里面保持一致
            Uri photoURI = FileProvider.getUriForFile(mContext, AppUtil.getFileProviderAuthor(), file);
            Logger.i(photoURI.toString());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//            intent.putExtra("return-data",false);
//            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//            intent.putExtra("noFaceDetection", true);
//            intent.putExtra("android.intent.extras.CAMERA_FACING",1);
        }
        startActivityForResult(intent, REQ_CODE_TAKE_PHOTO_FROM_CAMERA);
    }

    private void takePhotoFromAlbum() {
        //content://com.android.providers.media.documents/document/image%3A2287
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //要使用Intent.ACTION_PICK，才能把content://转换成file://。content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Ffile%2F49/ORIGINAL/NONE/1537110199
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

//        intent.addCategory(Intent.CATEGORY_OPENABLE);//加上无法处理Intent
        intent.setType("image/*");//去掉会报异常：No Activity found to handle Intent

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, REQ_CODE_TAKE_PHOTO_FROM_ALBUM);
        } else {
            Toast.makeText(getContext(), "无法处理Intent", Toast.LENGTH_SHORT).show();
        }
    }
}
