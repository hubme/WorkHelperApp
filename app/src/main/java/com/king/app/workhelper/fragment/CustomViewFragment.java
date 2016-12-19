package com.king.app.workhelper.fragment;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.log.Logger;
import com.king.applib.util.FileUtil;
import com.king.applib.util.ImageUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 自定义View
 * Created by VanceKing on 2016/11/26 0026.
 */

public class CustomViewFragment extends AppBaseFragment {
    String mImagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/000test/origin_spring.jpg";
    String mSavedBitmapPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/000test/dest_spring.jpg";

    @BindView(R.id.image_after)
    public ImageView mAfterImage;
    private Context mContext;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_custom;
    }

    @Override
    protected void initData() {
        super.initData();
        mContext = getContext();
        requestBank();
    }

    @OnClick(R.id.tv_show_dialog)
    public void clickBtn() {
//        showDialog();

        Bitmap bitmap = ImageUtil.getBitmap(FileUtil.getFileByPath(mImagePath));
        /*long aaa = System.currentTimeMillis();
        Bitmap compressedBitmap = ImageUtil.compressByQuality(bitmap, 60);
        Logger.i("压缩需要：" + (System.currentTimeMillis() - aaa) + " Millis");

        mAfterImage.setImageBitmap(compressedBitmap);*/

        Bitmap compressedImage = ImageUtil.compressBySize(bitmap);
        long aaa = System.currentTimeMillis();
        ImageUtil.saveBitmap(compressedImage, mSavedBitmapPath, Bitmap.CompressFormat.JPEG, 100);
        Logger.i("压缩需要：" + (System.currentTimeMillis() - aaa) + " Millis");
        mAfterImage.setImageBitmap(compressedImage);
    }

    private void requestBank() {
        final String url = "http://gjj.9188.com/app/loan/xiaoying_bank.json";
        OkHttpUtils.get().url(url).build()
                .execute(new StringCallback() {
                    @Override public void onError(Call call, Exception e, int id) {
                    }

                    @Override public void onResponse(String response, int id) {
//                        Logger.i("response : " + response);
                    }
                });
    }

    private void showDialog() {
        View customView = LayoutInflater.from(mContext).inflate(R.layout.list_picker_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(customView);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @OnClick(R.id.image1)
    public void clickImage1(ImageView textView) {
        textView.setBackground(wrap(R.mipmap.icon_1));
    }

    public Drawable wrap(int icon) {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(),icon, getActivity().getTheme());
        ColorStateList mTint = ResourcesCompat.getColorStateList(getResources(), R.color.tab, getActivity().getTheme());
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable.mutate());
            DrawableCompat.setTintList(drawable, mTint);
        }
        return drawable;
    }
}
