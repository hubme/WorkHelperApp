package com.king.app.workhelper.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.log.Logger;

import java.io.File;

import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * FileProvider使用
 * Created by HuoGuangxu on 2017/1/4.
 */

public class FileProviderFragment extends AppBaseFragment {
    public static final int REQ_CODE_PICK_PHOTO = 1;
    @Override
    protected int getContentLayout() {
        return R.layout.fragment_file_provider;
    }

    @OnClick(R.id.tv_open_gallery)
    public void openGallery(TextView textView) {
        bbb();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        switch (requestCode) {
            case REQ_CODE_PICK_PHOTO:
                Logger.i(data.toString());
                break;
            default:
                break;
        }
    }

    private void bbb() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/000test/img.png";

        File file = new File(path);

        //主要修改就在下面3行

    /* getUriForFile(Context context, String authority, File file):此处的authority需要和manifest里面保持一致。
    photoURI打印结果：content://cn.lovexiaoai.myapp.fileprovider/camera_photos/Pictures/Screenshots/testImg.png 。
    这里的camera_photos:对应filepaths.xml中的name
    */
        Uri photoURI = FileProvider.getUriForFile(mContext, "com.king.app.workhelper.fileprovider", file);

     /* 这句要记得写：这是申请权限，之前因为没有添加这个，打开裁剪页面时，一直提示“无法修改低于50*50像素的图片”，
      开始还以为是图片的问题呢，结果发现是因为没有添加FLAG_GRANT_READ_URI_PERMISSION。
      如果关联了源码，点开FileProvider的getUriForFile()看看（下面有），注释就写着需要添加权限。
      */
        Intent intent = new Intent();//"com.android.camera.action.CROP"
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(photoURI, "image/*");

//        intent.putExtra("crop", "true");
//        intent.putExtra("outputX", 80);
//        intent.putExtra("outputY", 80);
//        intent.putExtra("return-data", false);
        getActivity().startActivityForResult(intent, 1);
    }

    private void aaa() {
        Intent intent = new Intent("com.android.camera.action.CROP");

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/000test/img.png";
        Uri uri = Uri.parse("file://" + path);
        intent.setDataAndType(uri, "image/*");

        intent.putExtra("crop", "true");
        intent.putExtra("outputX", 80);
        intent.putExtra("outputY", 80);
        intent.putExtra("return-data", false);
        getActivity().startActivityForResult(intent, 1);
    }

}
