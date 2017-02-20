package com.king.app.workhelper.fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.ui.customview.BadgeTextView;
import com.king.app.workhelper.ui.customview.FormView;
import com.king.app.workhelper.ui.customview.FundFormPieView;
import com.king.app.workhelper.ui.customview.HorizontalTagView;
import com.king.app.workhelper.ui.customview.PieView;
import com.king.app.workhelper.ui.customview.SimpleDrawable;
import com.king.app.workhelper.ui.customview.TagTextView;
import com.king.applib.log.Logger;
import com.king.applib.ui.customview.BadgeView;
import com.king.applib.ui.customview.BadgeView2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 自定义View
 * Created by VanceKing on 2016/11/26 0026.
 */

public class CustomViewFragment extends AppBaseFragment {

    @BindView(R.id.tv_haha) public TextView mTextViewHaHa;
    @BindView(R.id.gradient_drawable) public ImageView mGradientDrawable;
    @BindView(R.id.my_badge) public BadgeTextView myBadgeTextView;
    @BindView(R.id.htv_tag) public HorizontalTagView mTagView;
    @BindView(R.id.tag_view) public TagTextView mTagTextView;
    @BindView(R.id.pie_view) PieView mPieView;
    @BindView(R.id.pie_tax) FundFormPieView mFundPieView;
    @BindView(R.id.form_view) FormView mFormView;

    private BadgeView mBadgeView;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_custom;
    }

    @Override
    protected void initContentView(View rootView) {
        super.initContentView(rootView);

        //颜色渐变
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{0xFFFF0000, 0xFF00FF00, 0xFF0000FF});
        mGradientDrawable.setImageDrawable(drawable);

    }

    @Override
    protected void initData() {
        super.initData();

        mBadgeView = new BadgeView(getContext(), mTextViewHaHa);
        mBadgeView.setText("2");
        mBadgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
//        mBadgeView.setBadgeMargin(-20, 0);
        mBadgeView.show();

        BadgeView2 badgeView2 = new BadgeView2(getContext());
        badgeView2.setBadgeCount(15);
        badgeView2.setTargetView(myBadgeTextView);

        mTagView.setOnTagCheckedListener(new HorizontalTagView.OnTagCheckedListener() {
            @Override public void onTagChecked(boolean isChecked, String tag) {
                Logger.i("isChecked: " + isChecked + "; tag: " + tag);
                Toast.makeText(getContext(), tag + "  " + isChecked, Toast.LENGTH_SHORT).show();
            }
        });

        mTagTextView.setChangedBackground(false);
        mTagTextView.setOnTagCheckedListener(new TagTextView.OnTagCheckedListener() {
            @Override public void onTagChecked(TagTextView tagView) {
                Log.i("aaa", tagView.getText().toString());
            }
        });

        initPieView();
        initPieView2();
        initFormView();
    }

    private void initPieView() {
        List<PieView.Pie> pies = new ArrayList<>();
        pies.add(new PieView.Pie(0.35f, R.color.chocolate));
        pies.add(new PieView.Pie(0.15f, R.color.peru));
        pies.add(new PieView.Pie(0.05f, R.color.indianred));
        pies.add(new PieView.Pie(0.45f, R.color.mediumvioletred));
        mPieView.drawPies(pies);
    }

    private void initPieView2() {
        final List<FundFormPieView.IFormStatisticsData> datas = new ArrayList<>();

        datas.add(new FundFormPieView.IFormStatisticsData("7475元", "税后月薪", 7475, Color.GRAY));
        datas.add(new FundFormPieView.IFormStatisticsData("1200元", "公积金", 1200, Color.GREEN));
        datas.add(new FundFormPieView.IFormStatisticsData("1020元", "社保", 1200, Color.DKGRAY));
        datas.add(new FundFormPieView.IFormStatisticsData("323元", "个税", 1200, Color.MAGENTA));

        mFundPieView.postDelayed(new Runnable() {
            @Override public void run() {
                mFundPieView.updateData(datas, true);
            }
        }, 300);
    }

    private void initFormView() {
        List<FormView.FormLine> formLines = new ArrayList<>();
        formLines.add(new FormView.FormLine(Arrays.asList(new FormView.FormItem("购房情况"),
                new FormView.FormItem("建筑面积", ContextCompat.getColor(getContext(), R.color.gray_f0f0f0)),
                new FormView.FormItem("税率", ContextCompat.getColor(getContext(), R.color.gray_f0f0f0)))));
        formLines.add(new FormView.FormLine(Arrays.asList(new FormView.FormItem("首套房", 1, 3, Color.WHITE),
                new FormView.FormItem("90(含)平以下", Color.WHITE),
                new FormView.FormItem("1%", Color.WHITE))));
        formLines.add(new FormView.FormLine(Arrays.asList(new FormView.FormItem("", 0, 0, ContextCompat.getColor(getContext(), R.color.gray_f0f0f0)),
                new FormView.FormItem("90-144平", ContextCompat.getColor(getContext(), R.color.gray_f0f0f0)),
                new FormView.FormItem("1.5%", ContextCompat.getColor(getContext(), R.color.gray_f0f0f0)))));
        formLines.add(new FormView.FormLine(Arrays.asList(new FormView.FormItem("", 0, 0, Color.WHITE),
                new FormView.FormItem("144平以上", Color.WHITE),
                new FormView.FormItem("1.5%", Color.WHITE))));
        formLines.add(new FormView.FormLine(Arrays.asList(new FormView.FormItem("二套房", 1, 2, Color.WHITE),
                new FormView.FormItem("90(含)平以下", ContextCompat.getColor(getContext(), R.color.gray_f0f0f0)),
                new FormView.FormItem("1%", ContextCompat.getColor(getContext(), R.color.gray_f0f0f0)))));
        formLines.add(new FormView.FormLine(Arrays.asList(new FormView.FormItem("", 0, 0, Color.WHITE),
                new FormView.FormItem("90平以上", Color.WHITE),
                new FormView.FormItem("2%", Color.WHITE))));
        mFormView.setData(formLines);
    }


    public Drawable wrap(int icon) {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), icon, getActivity().getTheme());
        ColorStateList mTint = ResourcesCompat.getColorStateList(getResources(), R.color.tab, getActivity().getTheme());
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable.mutate());
            DrawableCompat.setTintList(drawable, mTint);
        }
        return drawable;
    }

    @OnClick(R.id.tv_hehe)
    public void clickHeHe(TextView textView) {
//        ColorStateList csl = getResources().getColorStateList(R.color.button_text);
        ColorStateList csl = ResourcesCompat.getColorStateList(getResources(), R.color.button_text, getActivity().getTheme());
        textView.setTextColor(csl);
    }

    @OnClick(R.id.tv_haha)
    public void testGradientDrawable(TextView textView) {
        Drawable drawable = getShapeDrawable();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textView.setBackground(drawable);
        } else {
            textView.setBackgroundDrawable(drawable);
        }
        if (!mTagView.isTagExists("北京")) {
            mTagView.addTag(true, "北京");
        } else {
            Toast.makeText(getContext(), "tag已存在", Toast.LENGTH_SHORT).show();
        }
    }

    private Drawable getGradientDrawable() {
        //渐变色
//        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{0xFFFF0000, 0xFF00FF00, 0xFF0000FF});
        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setColor(Color.BLUE);
        gd.setCornerRadius(15);
//        gd.setStroke(1, Color.parseColor("#FFFF0000"));//边框
        gd.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        return gd;
    }

    private Drawable getShapeDrawable() {
//        Drawable drawable = new ShapeDrawable(new OvalShape());
//        return drawable;
        return new SimpleDrawable.Builder(getContext())
                .setShape(SimpleDrawable.RADIAL)
                .setBackgroundColor(R.color.colorAccent)
                .setCornerRadius(45)
                .build();
    }

}
