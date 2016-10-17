package com.king.app.workhelper.activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.common.FileUtils;

import java.io.File;

public class WebViewActivity extends AppBaseActivity {
    public static final String TAG = "MainActivity";
    private static final int REQ_CODE_CAMERA = 1;
    private static final int REQ_CODE_ALBUM = 2;

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mFilePathCallback;
    private WebView mWebView;
    private String compressPath = "";
    private String imagePaths;
    private Uri cameraUri;

    @Override public int getContentLayout() {
        return R.layout.activity_webview;
    }

    @Override protected void initData() {
        super.initData();
        initView();
    }

    private void initView() {
        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl("file:///android_asset/upload_image.html");
    }


    private class MyWebChromeClient extends WebChromeClient {

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            if (mUploadMessage != null){
                return;
            }
            mUploadMessage = uploadMsg;
            selectImage();
        }

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "");
        }

        // For Android  > 4.1.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            openFileChooser(uploadMsg, acceptType);
        }

    }

    /**
     * 检查SD卡是否存在
     */
    public final boolean checkSDCard() {
        boolean flag = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        if (!flag) {
            Toast.makeText(this, "请插入手机存储卡再使用本功能", Toast.LENGTH_SHORT).show();
        }
        return flag;
    }


    protected final void selectImage() {
        if (!checkSDCard())
            return;
        String[] selectPicTypeStr = {"照相机", "图片"};
        new AlertDialog.Builder(this)
                .setItems(selectPicTypeStr,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        takePhotoFromCamera();
                                        break;
                                    case 1:
                                        takePhotoFromAlbum();
                                        break;
                                    default:
                                        break;
                                }
                                compressPath = Environment.getExternalStorageDirectory().getPath() + "/000";
                                new File(compressPath).mkdirs();
                                compressPath = compressPath + File.separator + "compress.jpg";
                            }
                        }).show();
    }


    /**
     * 打开照相机
     */
    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        imagePaths = Environment.getExternalStorageDirectory().getPath() + "/000/" + System.currentTimeMillis() + ".jpg";
        // 必须确保文件夹路径存在，否则拍照后无法完成回调
        File vFile = new File(imagePaths);
        if (!vFile.exists()) {
            File vDirPath = vFile.getParentFile();
            vDirPath.mkdirs();
        } else {
            if (vFile.exists()) {
                vFile.delete();
            }
        }
        cameraUri = Uri.fromFile(vFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        startActivityForResult(intent, REQ_CODE_CAMERA);
    }

    /**
     * 拍照结束后
     */
    private void afterOpenCamera() {
        File f = new File(imagePaths);
        addImageGallery(f);
        File newFile = FileUtils.compressFile(f.getPath(), compressPath);
    }

    /** 解决拍照后在相册中找不到的问题 */
    private void addImageGallery(File file) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    /**
     * 本地相册选择图片
     */
    private void takePhotoFromAlbum() {
        FileUtils.delFile(compressPath);
        Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        String IMAGE_UNSPECIFIED = "image/*";
        innerIntent.setType(IMAGE_UNSPECIFIED); // 查看类型
        Intent wrapperIntent = Intent.createChooser(innerIntent, null);
        startActivityForResult(wrapperIntent, REQ_CODE_ALBUM);
    }

    /**
     * 选择照片后结束
     * @param data
     */
    private Uri afterChosePic(Intent data) {

        // 获取图片的路径：
        String[] proj = {MediaStore.Images.Media.DATA};
        // 好像是android多媒体数据库的封装接口，具体的看Android文档
        Cursor cursor = managedQuery(data.getData(), proj, null, null, null);
        if (cursor == null) {
            Toast.makeText(this, "上传的图片仅支持png或jpg格式", Toast.LENGTH_SHORT).show();
            return null;
        }
        // 按我个人理解 这个是获得用户选择的图片的索引值
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        // 将光标移至开头 ，这个很重要，不小心很容易引起越界
        cursor.moveToFirst();
        // 最后根据索引值获取图片路径
        String path = cursor.getString(column_index);
        if (path != null && (path.endsWith(".png") || path.endsWith(".PNG") || path.endsWith(".jpg") || path.endsWith(".JPG"))) {
            File newFile = FileUtils.compressFile(path, compressPath);
            return Uri.fromFile(newFile);
        } else {
            Toast.makeText(this, "上传的图片仅支持png或jpg格式", Toast.LENGTH_SHORT).show();
        }
        return null;
    }


    /**
     * 返回文件选择
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (null == mUploadMessage || resultCode != RESULT_OK) {
            return;
        }
        Uri uri = null;
        switch (requestCode) {
            case REQ_CODE_CAMERA:
                afterOpenCamera();
                uri = cameraUri;
                break;
            case REQ_CODE_ALBUM:
                uri = afterChosePic(intent);
                break;
            default:
                break;
        }
        mUploadMessage.onReceiveValue(uri);
        mUploadMessage = null;
    }

    /*public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }*/
}
