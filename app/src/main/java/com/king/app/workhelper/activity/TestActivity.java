package com.king.app.workhelper.activity;

import android.graphics.Color;
import android.text.Editable;
import android.widget.EditText;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.common.TextWatcherAdapter;
import com.king.app.workhelper.ui.customview.PieView2;
import com.king.applib.util.NumberUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

public class TestActivity extends AppBaseActivity {
    @BindView(R.id.pie_view) PieView2 mPieView;
    @BindView(R.id.edit1) EditText mEditText1;
    @BindView(R.id.edit2) EditText mEditText2;
    @BindView(R.id.edit3) EditText mEditText3;
    @BindView(R.id.edit4) EditText mEditText4;
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
        mEditText1.addTextChangedListener(new TextWatcherAdapter() {
            @Override public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                pieItem1.primaryText = s.toString() + "元";
                pieItem1.value = NumberUtil.getDouble(s.toString(), 0);
            }
        });
        mEditText2.addTextChangedListener(new TextWatcherAdapter() {
            @Override public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                pieItem2.primaryText = s.toString() + "元";
                pieItem2.value = NumberUtil.getDouble(s.toString(), 0);
            }
        });
        mEditText3.addTextChangedListener(new TextWatcherAdapter() {
            @Override public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                pieItem3.primaryText = s.toString() + "元";
                pieItem3.value = NumberUtil.getDouble(s.toString(), 0);
            }
        });
        mEditText4.addTextChangedListener(new TextWatcherAdapter() {
            @Override public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                pieItem4.primaryText = s.toString() + "元";
                pieItem4.value = NumberUtil.getDouble(s.toString(), 0);
            }
        });

        String text1 = mEditText1.getText().toString().trim();
        String text2 = mEditText2.getText().toString().trim();
        String text3 = mEditText3.getText().toString().trim();
        String text4 = mEditText4.getText().toString().trim();

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

    @OnClick(R.id.again)
    public void onAgain() {
        mPieView.drawPies("哈哈哈\n呵呵呵和", mPieItems, PieView2.ASC);
    }
}
