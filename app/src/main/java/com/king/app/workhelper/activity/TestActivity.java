package com.king.app.workhelper.activity;

import android.widget.CheckedTextView;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.ui.customview.ExpandableTextViewGroup;
import com.king.applib.log.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

public class TestActivity extends AppBaseActivity {
    @BindView(R.id.expand_group) ExpandableTextViewGroup mExpandableTextViewGroup;

    @Override protected int getContentLayout() {
        return R.layout.activity_test;
    }
    
    @Override protected void initContentView() {
        super.initContentView();

        List<ExpandableTextViewGroup.ExpandModel> models = new ArrayList<ExpandableTextViewGroup.ExpandModel>(){
            {
                add(new ExpandableTextViewGroup.ExpandModel("哈哈哈", "111111", false));
                add(new ExpandableTextViewGroup.ExpandModel("呵呵呵", "2222222\n222", false));
                add(new ExpandableTextViewGroup.ExpandModel("喔喔喔", "333333\n333\n333", false));
            }
        };
        mExpandableTextViewGroup.setExpandData(models);
        mExpandableTextViewGroup.setOnContentClickListener(new ExpandableTextViewGroup.OnContentClickListener() {
            @Override public void onSubTitleClick(int position, CheckedTextView parent, TextView textView) {
                Logger.i("position"+position + textView.getText().toString() + " "+parent.getText().toString());
            }
        });
        mExpandableTextViewGroup.setOnTitleClickListener(new ExpandableTextViewGroup.OnTitleClickListener() {
            @Override public void onTitleClick(int position, CheckedTextView parent, TextView textView) {
//                Log.i("aaa", "height: " + (int) textView.getTag());
            }
        });
    }
}
