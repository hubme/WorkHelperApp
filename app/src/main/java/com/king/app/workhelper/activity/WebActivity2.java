package com.king.app.workhelper.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.king.app.workhelper.R;
import com.king.applib.log.Logger;
import com.king.applib.util.ImageUtil;

import java.io.File;

/**
 * Created by HuoGuangxu on 2016/10/17.
 */

public class WebActivity2 extends Activity {
    private static final String TAG = "aaa";
    private static String DingDang = "https://cubetest.doraemoney.com/index?proId=ad63c39595d40686952528d1ae469a00";
    private WebView webView;
    public static final int INPUT_FILE_REQUEST_CODE = 1;
    private final static int FILECHOOSER_RESULTCODE = 2;
    private static final int REQ_CODE_CAMERA = 3;
    private static final int REQ_CODE_ALBUM = 4;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mFilePathCallback;

    private String mCameraPhotoPath = Environment.getExternalStorageDirectory().getPath() + "/000/upload_photo.jpg";


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview);
        webView = (WebView) findViewById(R.id.web_view);
//        webView.loadUrl("file:///android_asset/upload_image.html");
        webView.loadUrl(DingDang);
        WebSettings mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setSupportZoom(false);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setAllowContentAccess(true);

        webView.setWebChromeClient(mWebChromeClient);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                Log.i(TAG, "shouldOverrideUrlLoading url=" + url);
                view.loadUrl(url);
                return true;
            }
        });
    }

    //在sdcard卡创建缩略图
    private File createImageFile() {
        File file = new File(mCameraPhotoPath);
        file.deleteOnExit();
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        return file;
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        // android 5.0 这里需要使用android5.0 sdk
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                         WebChromeClient.FileChooserParams fileChooserParams) {

            Log.i(TAG, "onShowFileChooser");
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = filePathCallback;

            /**
             Open Declaration   String android.provider.MediaStore.ACTION_IMAGE_CAPTURE = "android.media.action.IMAGE_CAPTURE"
             Standard Intent action that can be sent to have the camera application capture an image and return it.
             The caller may pass an extra EXTRA_OUTPUT to control where this image will be written. If the EXTRA_OUTPUT is not present, then a small sized image is returned as a Bitmap object in the extra field. This is useful for applications that only need a small image. If the EXTRA_OUTPUT is present, then the full-sized image will be written to the Uri value of EXTRA_OUTPUT. As of android.os.Build.VERSION_CODES.LOLLIPOP, this uri can also be supplied through android.content.Intent.setClipData(ClipData). If using this approach, you still must supply the uri through the EXTRA_OUTPUT field for compatibility with old applications. If you don't set a ClipData, it will be copied there for you when calling Context.startActivity(Intent).
             See Also:EXTRA_OUTPUT
             标准意图，被发送到相机应用程序捕获一个图像，并返回它。通过一个额外的extra_output控制这个图像将被写入。如果extra_output是不存在的，
             那么一个小尺寸的图像作为位图对象返回。如果extra_output是存在的，那么全尺寸的图像将被写入extra_output URI值。
             */
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    //设置MediaStore.EXTRA_OUTPUT路径,相机拍照写入的全路径
                    photoFile = createImageFile();
                    takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                } catch (Exception ex) {
                    // Error occurred while creating the File
                    Log.e("WebViewSetting", "Unable to create Image File", ex);
                }

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    System.out.println(mCameraPhotoPath);
                } else {
                    takePictureIntent = null;
                }
            }

            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("image/*");
            Intent[] intentArray;
            if (takePictureIntent != null) {
                intentArray = new Intent[]{takePictureIntent};
                System.out.println(takePictureIntent);
            } else {
                intentArray = new Intent[0];
            }

            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

            startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);

            return true;
        }

        // For Android 3.0-
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            Log.i(TAG, "openFileChooser 3.0-");
            mUploadMessage = uploadMsg;
            selectImage();
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            Log.i(TAG, "openFileChooser 3.0+");
            mUploadMessage = uploadMsg;
            selectImage();
        }

        //For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            Log.i(TAG, "openFileChooser 4.1");
            mUploadMessage = uploadMsg;
            selectImage();
        }
    };

    /*
    弹出什么都不选，返回，不能再选择，即只能选择一次。
     */
    private void selectImage() {
        String[] selectPicTypeStr = {"照相机", "图片"};
        new AlertDialog.Builder(this).setTitle("选择照片").setCancelable(false)
                .setItems(selectPicTypeStr, new DialogInterface.OnClickListener() {
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
                    }
                }).show();
    }

    private void takePhotoFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQ_CODE_ALBUM);
    }

    /**
     * 打开照相机
     */
    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 必须确保文件夹路径存在，否则拍照后无法完成回调
        File photoFile = new File(mCameraPhotoPath);
        if (!photoFile.exists()) {
            File photoParentDir = photoFile.getParentFile();
            if (!photoParentDir.exists()) {
                photoParentDir.mkdirs();
            }
        } else {
            if (photoFile.exists()) {
                photoFile.delete();
            }
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        startActivityForResult(intent, REQ_CODE_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult");
        if (requestCode == REQ_CODE_CAMERA) {
            if (null == mUploadMessage){
                return;
            }
            mUploadMessage.onReceiveValue(Uri.parse("file:///" + mCameraPhotoPath));
            mUploadMessage = null;
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            String photoFilePath = "";
            if (data != null) {
                photoFilePath = ImageUtil.uri2ImageFile(data.getData(), getContentResolver());
            }

            mUploadMessage.onReceiveValue(Uri.parse("file:///" + photoFilePath));
            mUploadMessage = null;
        } else if (requestCode == INPUT_FILE_REQUEST_CODE && mFilePathCallback != null) {
            // 5.0的回调
            Uri[] results = null;

            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        Logger.d("camera_photo_path", mCameraPhotoPath);
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    Logger.d("camera_dataString", dataString);
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }

            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        }
    }
}