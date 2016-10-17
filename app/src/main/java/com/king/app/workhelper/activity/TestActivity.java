package com.king.app.workhelper.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.king.app.workhelper.R;

import java.io.File;

/**
 * Created by HuoGuangxu on 2016/10/17.
 */

public class TestActivity extends Activity {
    private static final String TAG = "aaa";
    public static final int FILECHOOSER_RESULTCODE = 1;
    private static final int REQ_CAMERA = FILECHOOSER_RESULTCODE + 1;
    private static final int REQ_CHOOSE = REQ_CAMERA + 1;


    ValueCallback<Uri> mUploadMessage;
    ValueCallback<Uri[]> mFilePathCallback;
    private WebView mWebView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_web_view);
        initView();
    }

    private void initView() {
        mWebView = (WebView) findViewById(R.id.web_my);
        mWebView.setWebChromeClient(new MyWebChromeClient());

        mWebView.setWebViewClient(new MyWebViewClient(this));
//    webView.loadUrl("file:///android_asset/upload_image.html");
        mWebView.loadUrl("http://192.168.25.38:8001/test/2016/5/2016-5-24/index.html");
    }

    private class MyWebViewClient extends WebViewClient {
        private Context mContext;

        public MyWebViewClient(Context context) {
            super();
            mContext = context;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "URL地址:" + url);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.i(TAG, "onPageFinished");
            super.onPageFinished(view, url);
        }
    }

    private class MyWebChromeClient extends WebChromeClient {

        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            if (mFilePathCallback != null) {
                return true;
            }
            mFilePathCallback = filePathCallback;
            selectImage();
            return true;
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            if (mUploadMessage != null)
                return;
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
     * @return
     */
    public final boolean checkSDcard() {
        boolean flag = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (!flag) {
            Toast.makeText(this, "请插入手机存储卡再使用本功能", Toast.LENGTH_SHORT).show();
        }
        return flag;
    }

    String compressPath = "";

    protected final void selectImage() {
        if (!checkSDcard())
            return;
        String[] selectPicTypeStr = {"camera", "photo"};
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setItems(selectPicTypeStr,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                switch (which) {
                                    // 相机拍摄
                                    case 0:
                                        openCarcme();
                                        break;
                                    // 手机相册
                                    case 1:
                                        chosePic();
                                        break;
                                    default:
                                        break;
                                }
                                compressPath = Environment
                                        .getExternalStorageDirectory()
                                        .getPath()
                                        + "/fuiou_wmp/temp";
                                new File(compressPath).mkdirs();
                                compressPath = compressPath + File.separator
                                        + "compress.jpg";
                            }
                        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        if (mFilePathCallback != null) {
                            Uri[] uris = new Uri[1];
                            uris[0] = Uri.parse("");
                            mFilePathCallback.onReceiveValue(uris);
                            mFilePathCallback = null;
                        } else {
                            mUploadMessage.onReceiveValue(Uri.parse(""));
                            mUploadMessage = null;
                        }
                    }
                }).show();
    }

    String imagePaths;
    Uri cameraUri;

    /**
     * 打开照相机
     */
    private void openCarcme() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        imagePaths = Environment.getExternalStorageDirectory().getPath() + "/fuiou_wmp/temp/" + (System.currentTimeMillis() + ".jpg");
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
        startActivityForResult(intent, REQ_CAMERA);
    }

    /**
     * 拍照结束后
     */
    private void afterOpenCamera() {
        File f = new File(imagePaths);
        addImageGallery(f);
    }

    /** 解决拍照后在相册中找不到的问题 */
    private void addImageGallery(File file) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    /**
     * 本地相册选择图片
     */
    private void chosePic() {
//        FileUtils.delFile(compressPath);
        Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
        String IMAGE_UNSPECIFIED = "image/*";
        innerIntent.setType(IMAGE_UNSPECIFIED); // 查看类型
        Intent wrapperIntent = Intent.createChooser(innerIntent, null);
        startActivityForResult(wrapperIntent, REQ_CHOOSE);
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
            File newFile = null;//FileUtils.compressFile(path, compressPath);
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

        if (null == mUploadMessage || mFilePathCallback == null) {
            return;
        }
        Uri uri = null;
        File file = new File(cameraUri.getPath());
        if (!file.exists()) {
            cameraUri = Uri.parse("");
        }
        if (requestCode == REQ_CAMERA) {
            afterOpenCamera();
            uri = cameraUri;
        } else if (requestCode == REQ_CHOOSE) {
            uri = afterChosePic(intent);
        }
        if (mFilePathCallback != null) {
            Uri[] uris = new Uri[1];
            uris[0] = uri;
            mFilePathCallback.onReceiveValue(uris);
        } else {
            mUploadMessage.onReceiveValue(uri);
        }
        mFilePathCallback = null;
        mUploadMessage = null;
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
