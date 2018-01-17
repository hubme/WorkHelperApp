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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.ui.customview.FundFormPieView;
import com.king.app.workhelper.ui.customview.HorizontalTagView;
import com.king.app.workhelper.ui.customview.PieChartView;
import com.king.app.workhelper.ui.customview.SimpleDrawable;
import com.king.app.workhelper.ui.customview.SwitchTitle;
import com.king.app.workhelper.ui.customview.TagTextView;
import com.king.app.workhelper.ui.customview.TestView;
import com.king.app.workhelper.ui.customview.ValueBar;
import com.king.applib.log.Logger;
import com.king.applib.ui.customview.BadgeTextView;
import com.king.applib.ui.customview.BadgeView;
import com.king.applib.ui.customview.BadgeView2;
import com.king.applib.ui.customview.FormView;
import com.king.applib.ui.customview.FormViewTest;
import com.king.applib.ui.customview.InsLoadingView;
import com.king.applib.ui.customview.NoticeView;
import com.king.applib.ui.customview.SimpleBadgeTextView;
import com.king.applib.ui.customview.TitleImageView;

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
    public static final String[] notices = new String[]{
            "风萧萧兮易水寒",
            "壮士一去兮不复还",
            "不返就不返",
    };

    @BindView(R.id.tv_haha) public TextView mTextViewHaHa;
    @BindView(R.id.gradient_drawable) public ImageView mGradientDrawable;
    @BindView(R.id.my_badge) public TextView myBadgeTextView;
    @BindView(R.id.htv_tag) public HorizontalTagView mTagView;
    @BindView(R.id.tag_view) public TagTextView mTagTextView;
    @BindView(R.id.pie_view) PieChartView mPieView;
    @BindView(R.id.pie_tax) FundFormPieView mFundPieView;
    @BindView(R.id.form_view) FormView mFormView;
    @BindView(R.id.form_view2) FormViewTest mFormView2;
    @BindView(R.id.switch_title) SwitchTitle mSwitchTitle;
    @BindView(R.id.tv_test) TextView mTestTv;
    @BindView(R.id.simple_badge) SimpleBadgeTextView mSimpleBadger;
    @BindView(R.id.notice) NoticeView mNoticeView;
    @BindView(R.id.ins_loading_view) InsLoadingView mInsLoadingView;
    @BindView(R.id.title_image_view) TitleImageView mTitleImageView;
    @BindView(R.id.valueBar) ValueBar mValueBar;

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

        BadgeTextView badgeTextView = new BadgeTextView(mContext);
        badgeTextView.setText("99+");
        badgeTextView.setTargetView(mGradientDrawable);

        applyBadge();

        mSimpleBadger.show("9");

        mNoticeView.setNotices(Arrays.asList(notices));
        mNoticeView.setOnClickListener(v -> showToast(notices[mNoticeView.getIndex()]));

        mInsLoadingView.setCircleDuration(2000);
        mInsLoadingView.setRotateDuration(10000);
        mInsLoadingView.setStartColor(Color.YELLOW);
        mInsLoadingView.setEndColor(Color.BLUE);
        mInsLoadingView.setOnClickListener(v -> {
            switch (mInsLoadingView.getStatus()) {
                case InsLoadingView.UN_CLICKED:
                    mInsLoadingView.setStatus(InsLoadingView.LOADING);
                    break;
                case InsLoadingView.LOADING:
                    mInsLoadingView.setStatus(InsLoadingView.CLICKED);
                    break;
                case InsLoadingView.CLICKED:
                    mInsLoadingView.setStatus(InsLoadingView.UN_CLICKED);
            }
        });

//        mTitleImageView.setImageUrl("http://gjj.9188.com/image/home/index/checkshebao_2_6.png");
        mTitleImageView.setImageRes(R.drawable.icon_3);
        mTitleImageView.setBackgroundResource(R.drawable.normal_selector);
        mTitleImageView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                showToast("哈哈哈");
            }
        });

        mValueBar.setMaxValue(100);
        mValueBar.setAnimated(true);
        mValueBar.setAnimationDuration(4000);
        mValueBar.setValue(10);
    }

    private void applyBadge() {
        ViewParent parent = mTestTv.getParent();
        if (parent instanceof ViewGroup) {
            ViewGroup container = (ViewGroup) parent;
            int childPosition = ((ViewGroup) parent).indexOfChild(mTestTv);
            container.removeView(mTestTv);

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(10,
                    10, Gravity.RIGHT | Gravity.TOP);

            View view = new View(mContext);
            view.setLayoutParams(new ViewGroup.LayoutParams(8, 8));
            view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.chocolate));

            FrameLayout frameLayout = new FrameLayout(mContext);

//            frameLayout.setLayoutParams(layoutParams);
            frameLayout.addView(mTestTv);
            frameLayout.addView(view);

            container.addView(frameLayout, childPosition);
        }
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
        initSwitchTitle();

    }

    private void initSwitchTitle() {
        mSwitchTitle.setTabTitles(Arrays.asList("哈哈哈", "呵呵呵", "哦哦哦", "恩恩嗯", "么么么"));
        mSwitchTitle.setOnTabClickListener((tab, position) -> showToast(tab.getText().toString()));
    }

    private void initPieView() {
        List<PieChartView.PieItem> pies = new ArrayList<>();
        pies.add(new PieChartView.PieItem("8000元", "税后月薪", 0.1, ContextCompat.getColor(mContext, R.color.chocolate)));
        pies.add(new PieChartView.PieItem("700元", "公积金", 0.1, ContextCompat.getColor(mContext, R.color.peru)));
        pies.add(new PieChartView.PieItem("500元", "社保", 0.1, ContextCompat.getColor(mContext, R.color.indianred)));
        pies.add(new PieChartView.PieItem("1000元", "个税", 20, ContextCompat.getColor(mContext, R.color.blue_57a5e2)));
        mPieView.setCenterText("3000元\r\n税后月薪");
        mPieView.drawPies(pies, PieChartView.ASC);
    }

    private void initPieView2() {
        final List<FundFormPieView.PieItem> datas = new ArrayList<>();

        datas.add(new FundFormPieView.PieItem("1000元", "", 1000, Color.GRAY));//税后月薪
        datas.add(new FundFormPieView.PieItem("100元", "", 100, Color.GREEN));//公积金
        datas.add(new FundFormPieView.PieItem("100元", "", 100, Color.DKGRAY));//社保
        datas.add(new FundFormPieView.PieItem("100元", "", 100, Color.MAGENTA));//个税

        mFundPieView.postDelayed(new Runnable() {
            @Override public void run() {
                if (mFundPieView != null) {
                    mFundPieView.updateData(datas, true);
                }
            }
        }, 300);
    }

    private void initFormView() {
        int color = ContextCompat.getColor(getContext(), R.color.gray_fbfbfb);
        List<FormView.FormLine> formLines = new ArrayList<>();
        formLines.add(new FormView.FormLine(Arrays.asList(new FormView.FormItem("购房情况"),
                new FormView.FormItem("建筑面积", color), new FormView.FormItem("税率", color))));
        formLines.add(new FormView.FormLine(Arrays.asList(new FormView.FormItem("首套房", 1, 3, Color.WHITE),
                new FormView.FormItem("90(含)平以下", Color.WHITE), new FormView.FormItem("1%", Color.WHITE))));
        formLines.add(new FormView.FormLine(Arrays.asList(new FormView.FormItem("", 0, 0, color),
                new FormView.FormItem("90-144平", color), new FormView.FormItem("1.5%", color))));
        formLines.add(new FormView.FormLine(Arrays.asList(new FormView.FormItem("", 0, 0, Color.WHITE),
                new FormView.FormItem("144平以上", Color.WHITE), new FormView.FormItem("1.5%", Color.WHITE))));
        formLines.add(new FormView.FormLine(Arrays.asList(new FormView.FormItem("二套房", 1, 2, Color.WHITE),
                new FormView.FormItem("90(含)平以下", color), new FormView.FormItem("1%", color))));
        formLines.add(new FormView.FormLine(Arrays.asList(new FormView.FormItem("", 0, 0, Color.WHITE),
                new FormView.FormItem("90平以上", Color.WHITE), new FormView.FormItem("2%", Color.WHITE))));
        mFormView.setData(formLines);

        List<FormViewTest.FormLine> formItems = new ArrayList<>();
        formItems.add(new FormViewTest.FormLine(Arrays.asList(new FormViewTest.FormItem("税前 / 12", 1.5f, color), new FormViewTest.FormItem("税率", color), new FormViewTest.FormItem("速算扣除", color))));
//        formItems.add(new FormViewTest.FormLine(Arrays.asList(new FormViewTest.FormItem("不超过1500元(含)", 1.5f), new FormViewTest.FormItem("3%"), new FormViewTest.FormItem("0"))));
//        formItems.add(new FormViewTest.FormLine(Arrays.asList(new FormViewTest.FormItem("1500元-4500元(含)", 1.5f, color), new FormViewTest.FormItem("10%", color), new FormViewTest.FormItem("105", color))));
//        formItems.add(new FormViewTest.FormLine(Arrays.asList(new FormViewTest.FormItem("4500元-9000元(含)", 1.5f), new FormViewTest.FormItem("20%"), new FormViewTest.FormItem("555"))));
//        formItems.add(new FormViewTest.FormLine(Arrays.asList(new FormViewTest.FormItem("9000元-35000元(含)", 1.5f, color), new FormViewTest.FormItem("25%", color), new FormViewTest.FormItem("1005", color))));
//        formItems.add(new FormViewTest.FormLine(Arrays.asList(new FormViewTest.FormItem("35000元-55000元(含)", 1.5f), new FormViewTest.FormItem("30%"), new FormViewTest.FormItem("2755"))));
//        formItems.add(new FormViewTest.FormLine(Arrays.asList(new FormViewTest.FormItem("55000元-80000元(含)", color), new FormViewTest.FormItem("35%", color), new FormViewTest.FormItem("5505", color))));
//        formItems.add(new FormViewTest.FormLine(Arrays.asList(new FormViewTest.FormItem("超过80000元",1.5f), new FormViewTest.FormItem("45%"), new FormViewTest.FormItem("13505"))));

        mFormView2.setColumnWeights(10, 2, 2);
        mFormView2.setData(formItems);
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

    @OnClick(R.id.tv_haha)
    public void testGradientDrawable(TextView textView) {
        Drawable drawable = getShapeDrawable();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textView.setBackground(drawable);
        } else {
            textView.setBackgroundDrawable(drawable);
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
                .setShape(GradientDrawable.RECTANGLE)
                .setBackgroundColor(R.color.colorAccent)
                .setCornerRadius(45)
                .build();
    }

    @OnClick(R.id.test_view)
    public void onTestViewClick(TestView view) {
        view.startAnim();
    }

    @OnClick(R.id.valueBar)
    public void onValueBarClick(ValueBar valueBar) {
        valueBar.setValue(valueBar.getValue() + 24);
    }

}
