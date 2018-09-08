package com.king.app.workhelper.activity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.applib.util.ExtendUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

public class TestActivity extends AppBaseActivity {
    @BindView(R.id.panel_tag) LinearLayout mTagPanel;
    @BindView(R.id.scroll_panel) HorizontalScrollView mScrollPanel;

    private GradientDrawable mNormalDrawable;
    private GradientDrawable mCheckedDrawable;
    private CheckedTextView mFirstTagView;
    private CheckedTextView mCheckedTagView;

    private List<String> tags;
    private int screenWidth;

    @Override protected void initInitialData() {
        super.initInitialData();
        mNormalDrawable = new GradientDrawable();
        mNormalDrawable.setShape(GradientDrawable.OVAL);
        mNormalDrawable.setColor(Color.parseColor("#383C49"));

        mCheckedDrawable = new GradientDrawable();
        mCheckedDrawable.setShape(GradientDrawable.OVAL);
        mCheckedDrawable.setColor(Color.parseColor("#383C49"));
        mCheckedDrawable.setStroke(ExtendUtil.dp2px(3), Color.parseColor("#3890EF"));

        tags = new ArrayList<>();
        tags.add("空白");
        tags.add("里程");
        tags.add("详细\n数据");
        tags.add("基础\n数据");
        tags.add("打卡");
        tags.add("累计\n用时");
        tags.add("aaa");
        tags.add("bbb");
        tags.add("ccc");

        screenWidth = ExtendUtil.getScreenWidth();
    }

    @Override protected void initData() {

    }

    @Override protected int getContentLayout() {
        return R.layout.activity_test;
    }

    @Override protected void initContentView() {
        super.initContentView();

        Log.i("aaa", "getStatusBarHeight: " + ExtendUtil.getStatusBarHeight());
        populateTags(tags);

    }

    private void printViewLocation(View view) {
        int[] window = new int[2];
        int[] screen = new int[2];
        view.getLocationInWindow(window);
        view.getLocationOnScreen(screen);

        Log.i("aaa", "getLeft(): " + view.getLeft() + "  getTop(): " + view.getTop());
        Log.i("aaa", "window[x]: " + window[0] + "  window[y]: " + window[1]);
        Log.i("aaa", "screen[x]: " + screen[0] + "  screen[y]: " + screen[1]);

    }

    /*@OnClick(R.id.floating_button)
    public void onFloatingViewClick(FloatingActionButton button) {
        printViewLocation(mview);
        printViewLocation(mPanel);
    }
*/
    private void populateTags(List<String> tags) {
        for (String tag : tags) {
            mTagPanel.addView(buildTagView(tag));
        }
        mFirstTagView = (CheckedTextView) mTagPanel.getChildAt(0);
        mCheckedTagView = mFirstTagView;
        mCheckedTagView.setBackground(mCheckedDrawable);
    }

    private CheckedTextView buildTagView(String tag) {
        final CheckedTextView textView = new CheckedTextView(this);
        int size = ExtendUtil.dp2px(70);
        int margin = ExtendUtil.dp2px(5);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        params.setMargins(margin, 0, margin, 0);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        textView.setText(tag);
        textView.setTextSize(15);
        textView.setBackground(mNormalDrawable);
        textView.setTextColor(Color.WHITE);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                selectTag(textView, mCheckedTagView);
            }
        });
        return textView;
    }

    private void selectTag(CheckedTextView checkedTag, CheckedTextView preTag) {
        if (checkedTag == null || preTag == null) {
            return;
        }
        if (checkedTag != preTag) {
            preTag.setChecked(false);
            preTag.setBackgroundDrawable(mNormalDrawable);
        }
        checkedTag.setBackgroundDrawable(mCheckedDrawable);
        int[] window = new int[2];
        checkedTag.getLocationInWindow(window);
        int dx = window[0] - screenWidth / 2 + checkedTag.getMeasuredWidth() / 2;
        Log.i("aaa", "window[x]: " + window[0] + "   getX: " + checkedTag.getX() + "   dx: " + dx);
        mScrollPanel.smoothScrollBy(dx, 0);
        checkedTag.setChecked(true);
        mCheckedTagView = checkedTag;
    }
}
