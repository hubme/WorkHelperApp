package com.king.app.workhelper.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ScrollView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.applib.util.ExtendUtil;
import com.king.applib.util.FileUtil;
import com.king.applib.util.ImageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

public class TestActivity extends AppBaseActivity {
    @BindView(R.id.panel_overview) View mOverView;
    @BindView(R.id.chart_view_speed) View mView1;
    @BindView(R.id.chart_view_walk) View mView2;
    @BindView(R.id.chart_view_altitude) View mView3;
    @BindView(R.id.scroll_view) ScrollView mScrollView;

    @Override protected void initData() {
    }

    @Override protected int getContentLayout() {
        return R.layout.fragment_run_aft;
    }

    @Override protected void initContentView() {
        super.initContentView();
    }


    @OnClick(R.id.tv_save_data)
    public void onSaveClick() {
        screenShot();
    }

    private void screenShot() {
        int h = 0;
        for (int i = 0; i < mScrollView.getChildCount(); i++) {
            h += mScrollView.getChildAt(i).getMeasuredHeight();
        }
        
        Bitmap bitmap = Bitmap.createBitmap(ExtendUtil.getScreenWidth(), h, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(ContextCompat.getColor(this, R.color.chocolate));
        
        /*Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.aaa);
        Bitmap bm2 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight());
        canvas.drawBitmap(bm2, 0, 0, null);*/

        final String text = "哈哈哈";
        Paint paint = new Paint();
        paint.setTextSize(ExtendUtil.sp2px(12));
        paint.setColor(Color.WHITE);
        canvas.drawText(text, ExtendUtil.dp2px(50), ExtendUtil.dp2px(15), paint);
        canvas.translate((ExtendUtil.getScreenWidth() - mView1.getMeasuredWidth()) / 2, ExtendUtil.dp2px(100));

//        mScrollView.draw(canvas);

        mOverView.draw(canvas);
        canvas.translate(0, mOverView.getMeasuredHeight() + ExtendUtil.dp2px(10));
        mView1.draw(canvas);
        canvas.translate(0, mView1.getMeasuredHeight() + ExtendUtil.dp2px(10));
        mView2.draw(canvas);
        canvas.translate(0, mView2.getMeasuredHeight() + ExtendUtil.dp2px(10));
        mView3.draw(canvas);


        File file = ImageUtil.saveBitmap(bitmap, Environment.getExternalStorageDirectory().getPath() + "/000test/1111.png", Bitmap.CompressFormat.PNG, 90);
        if (FileUtil.isLegalFile(file)) {
            showToast("截图成功");
        } else {
            showToast("截图失败");
        }
    }

    private List<Bitmap> getViewBitmaps(List<View> views) {
        if (views == null || views.isEmpty()) {
            return null;
        }
        List<Bitmap> bitmaps = new ArrayList<>(views.size());
        for (View view : views) {
            view.buildDrawingCache();
            bitmaps.add(view.getDrawingCache());
            
        }
        return bitmaps;
    }
}
