package com.king.app.workhelper.activity;

import android.graphics.Color;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.ui.customview.PieView2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

public class TestActivity extends AppBaseActivity {
    @BindView(R.id.pie_view) PieView2 mPieView;
    
    @Override protected int getContentLayout() {
        return R.layout.activity_test;
    }
    
    @Override protected void initContentView() {
        super.initContentView();

        List<PieView2.PieItem> sdlf = new ArrayList<PieView2.PieItem>(){
            {
                add(new PieView2.PieItem("323元", "公交", 323, Color.parseColor("#dd4649")));
                add(new PieView2.PieItem("1020元", "购物", 1020, Color.parseColor("#fc9d42")));
                add(new PieView2.PieItem("1200元", "食宿", 1200, Color.parseColor("#dd4649")));
                add(new PieView2.PieItem("323元", "其他", 323, Color.parseColor("#dd4649")));
            }
        };
        mPieView.drawPies(sdlf, PieView2.DESC);
    }
}
