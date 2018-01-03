package com.king.app.workhelper.activity;

import android.graphics.Color;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.ui.customview.PieView2;
import com.king.applib.util.NumberUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

public class TestActivity extends AppBaseActivity {
    @BindView(R.id.pie_view) PieView2 mPieView;
    private final List<PieView2.PieItem> mPieItems = new ArrayList<>();
    private PieView2.PieItem pieItem1;
    private PieView2.PieItem pieItem2;
    private PieView2.PieItem pieItem3;
    private PieView2.PieItem pieItem4;

    @Override protected int getContentLayout() {
        return R.layout.activity_test;
    }
    
    @Override protected void initContentView() {
        super.initContentView();
        String text1 = "7475";
        String text2 = "323";
        String text3 = "1200";
        String text4 = "1020";

        pieItem1 = new PieView2.PieItem(text1 + "元", "公交", NumberUtil.getInt(text1, 0), Color.parseColor("#dd4649"));
        mPieItems.add(pieItem1);
        
        pieItem2 = new PieView2.PieItem(text2 + "元", "购物", NumberUtil.getInt(text2, 0), Color.parseColor("#fc9d42"));
        mPieItems.add(pieItem2);

        pieItem3 = new PieView2.PieItem(text3 + "元", "食宿", NumberUtil.getInt(text3, 0), Color.parseColor("#5653f9"));
        mPieItems.add(pieItem3);

        pieItem4 = new PieView2.PieItem(text4 + "元", "其他", NumberUtil.getInt(text4, 0), Color.parseColor("#5b96f1"));
        mPieItems.add(pieItem4);
        
        mPieView.drawPies("哈哈哈\n呵呵呵和", mPieItems, PieView2.ASC);
    }
}
