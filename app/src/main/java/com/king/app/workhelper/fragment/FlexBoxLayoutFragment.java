package com.king.app.workhelper.fragment;

import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.util.ExtendUtil;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author huoguangxu
 * @since 2017/6/1.
 */

public class FlexBoxLayoutFragment extends AppBaseFragment {
    private Random mRandom = new Random();
    
    @BindView(R.id.flex_box_layout) FlexboxLayout mFlexBoxLayout;
    
    @Override protected int getContentLayout() {
        return R.layout.fragment_flexbox_layout;
    }

    @OnClick(R.id.fab_add)
    public void onAddClick(FloatingActionButton button) {
        final int childCount = mFlexBoxLayout.getChildCount();
        TextView textView = createBaseFlexItemTextView(childCount);
        textView.setLayoutParams(createLayoutParams());
        mFlexBoxLayout.addView(textView);
    }

    private TextView createBaseFlexItemTextView(int index) {
        TextView textView = new TextView(mContext);
        textView.setBackgroundResource(R.drawable.flex_item_background);
        textView.setText(String.valueOf(index + 1));
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    private FlexboxLayout.LayoutParams createLayoutParams() {
        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(ExtendUtil.dp2px(createRandomDpValue()), ExtendUtil.dp2px(80));
        return lp;
    }

    private int createRandomDpValue() {
        int[] values = {30, 50, 80, 60, 70, 100, 120};
        return values[mRandom.nextInt(values.length)];
    }
}
