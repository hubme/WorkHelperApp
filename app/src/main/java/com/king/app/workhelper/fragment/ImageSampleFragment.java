package com.king.app.workhelper.fragment;

import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.log.Logger;
import com.king.applib.util.FileUtil;
import com.king.applib.util.ImageUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 图片操作相关
 * Created by VanceKing on 2016/11/26 0026.
 */

public class ImageSampleFragment extends AppBaseFragment {
    private String mImagePath;
    private String mSavedBitmapPath;


    @BindView(R.id.iv_before_compressed)
    public ImageView mBeforeCompressedIv;

    @BindView(R.id.iv_after_compressed)
    public ImageView mAfterCompressedIv;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_image_sample;
    }

    @Override protected void initContentView(View rootView) {
        super.initContentView(rootView);
    }

    @Override protected void initData() {
        super.initData();
        mImagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/000test/compress_before.jpg";
        mSavedBitmapPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/000test/compress_after_02.jpg";
    }

    @OnClick(R.id.tv_image_sample)
    public void compressImage() {
        //图片太大，无法显示。
//        Bitmap mBeforeBitmap = ImageUtil.getBitmap(getContext(), R.mipmap.compress_before);
//        mBeforeCompressedIv.setImageBitmap(mBeforeBitmap);

        int width = mAfterCompressedIv.getWidth();
        int height = mAfterCompressedIv.getHeight();
        File file = ImageUtil.compressBySampling(FileUtil.getFileByPath(mImagePath), mSavedBitmapPath, width, height);
        Logger.i(file != null ? "图片保存成功" : "图片保存失败");
    }
}
