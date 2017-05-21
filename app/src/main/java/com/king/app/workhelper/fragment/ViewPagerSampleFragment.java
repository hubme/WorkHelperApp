package com.king.app.workhelper.fragment;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.adapter.viewpager.MyViewPagerAdapter;
import com.king.app.workhelper.app.AppConfig;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.model.entity.Student;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * ViewPager相关
 * Created by VanceKing on 2016/11/26 0026.
 */

public class ViewPagerSampleFragment extends AppBaseFragment {
    @BindView(R.id.king_view_pager)
    ViewPager mViewPager;
    private MyViewPagerAdapter mViewPagerAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_view_pager;
    }

    @Override
    protected void initData() {
        super.initData();
        mViewPagerAdapter = new MyViewPagerAdapter();
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPagerAdapter.setAdapterData(buildPagerView(fakeData()));
        mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
    }

    private List<Student> fakeData() {
        List<Student> students = new ArrayList<>(2);
        students.add(new Student(20, "哈哈哈"));
        students.add(new Student(30, "呵呵呵"));
        return students;
    }

    private List<View> buildPagerView(List<Student> students) {
        List<View> views = new ArrayList<>();
        for (Student student : students) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_view_pager, null);
            ((TextView) view.findViewById(R.id.tv_name)).setText(student.name);
            ((TextView) view.findViewById(R.id.tv_age)).setText(String.valueOf(student.age));
            views.add(view);
        }
        return views;
    }

    private static class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.i(AppConfig.LOG_TAG, "onPageScrolled--->position: " + position + ";positionOffset: " + positionOffset + ";positionOffsetPixels: " + positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            Log.i(AppConfig.LOG_TAG, "onPageSelected--->position: " + position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager.SCROLL_STATE_IDLE:
                    Log.i(AppConfig.LOG_TAG, "onPageScrollStateChanged--->SCROLL_STATE_IDLE");
                    break;
                case ViewPager.SCROLL_STATE_DRAGGING:
                    Log.i(AppConfig.LOG_TAG, "onPageScrollStateChanged--->SCROLL_STATE_DRAGGING");
                    break;
                case ViewPager.SCROLL_STATE_SETTLING:
                    Log.i(AppConfig.LOG_TAG, "onPageScrollStateChanged--->SCROLL_STATE_SETTLING");
                    break;
            }
        }
    }
}
