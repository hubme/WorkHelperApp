package com.king.app.workhelper.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Environment;
import android.widget.ImageView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

public class TestActivity extends AppBaseActivity {
    @BindView(R.id.image) ImageView imageView;

    @Override protected void initInitialData() {
        super.initInitialData();
    }

    @Override protected void initData() {

    }

    @Override protected int getContentLayout() {
        return R.layout.activity_test;
    }

    @Override protected void initContentView() {
        super.initContentView();


    }

    @OnClick(R.id.textView)
    public void onViewClick() {
        try {
            String path = Environment.getExternalStorageDirectory() + "/000test/sunset.jpg";
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, bitmapOptions);
            int outWidth = bitmapOptions.outWidth;
            int outHeight = bitmapOptions.outHeight;

            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(path, false);
            BitmapFactory.Options options = new BitmapFactory.Options();
            Rect rect = new Rect(outWidth / 2 - 300, outHeight / 2 - 300, outWidth / 2 + 300, outHeight / 2 + 300);
            Bitmap bitmap = decoder.decodeRegion(rect, options);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
