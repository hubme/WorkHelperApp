package com.king.app.workhelper.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.Gravity;
import android.widget.ImageView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * RoundedBitmapDrawable使用
 *
 * @author VanceKing
 * @since 2016/12/7
 */
public class RoundDrawableFragment extends AppBaseFragment {
    @BindView(R.id.iv_image1)
    public ImageView mImage1;

    @BindView(R.id.iv_image2)
    public ImageView mImage2;

    @BindView(R.id.iv_image3)
    public ImageView mImage3;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_round_image;
    }

    @OnClick(R.id.tv_image1)
    public void onImage1Click() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.beautiful_girl);
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        drawable.setCornerRadius(100);
        mImage1.setImageDrawable(drawable);
    }

    @OnClick(R.id.tv_image2)
    public void onImage2Click() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.beautiful_girl);
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        drawable.setCircular(true);
        mImage2.setImageDrawable(drawable);
    }

    @OnClick(R.id.tv_image3)
    public void onImage3Click() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.beautiful_girl);
        mImage3.setImageDrawable(createRoundImageWithBorder(bitmap, 0, R.color.orange_ff8800));
    }

    /**
     * 获取圆形图片，根据宽高裁剪，不变形。
     * @param bitmap 图片
     * @param borderWidth 边框宽度。单位 pixel。<br/>
     * borderWidth < 0:无边框，裁剪borderWidth像素;borderWidth = 0:无边框;borderWidth > 0:borderWidth像素的边框
     * @param colorId 边框颜色.
     */
    private Drawable createRoundImageWithBorder(Bitmap bitmap, int borderWidth, @ColorRes int colorId) {
        if (bitmap == null) {
            return null;
        }
        final int bitmapWidth = bitmap.getWidth();
        final int bitmapHeight = bitmap.getHeight();

        //转换为正方形后的宽高.为了图片不变形，宽高按最小值裁剪
        final int bitmapMinWidth = Math.min(bitmapWidth, bitmapHeight);

        //最终图像的宽高
        final int finalBitmapWidth = bitmapMinWidth + borderWidth;

        final Bitmap roundedBitmap = Bitmap.createBitmap(finalBitmapWidth, finalBitmapWidth, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundedBitmap);
        final int x = borderWidth + bitmapMinWidth - bitmapWidth;
        final int y = borderWidth + bitmapMinWidth - bitmapHeight;

        //裁剪后图像,注意X,Y要除以2 来进行一个中心裁剪
        canvas.drawBitmap(bitmap, x / 2, y / 2, null);

        Paint borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setColor(getResources().getColor(colorId));
        //添加边框
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getWidth() / 2, finalBitmapWidth / 2, borderPaint);

        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), roundedBitmap);
        roundedBitmapDrawable.setGravity(Gravity.CENTER);
        roundedBitmapDrawable.setCircular(true);
        return roundedBitmapDrawable;
    }
}
