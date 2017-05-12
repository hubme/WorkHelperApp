package com.king.app.workhelper.fragment;

import android.support.v4.view.ViewPager;

import com.king.app.workhelper.R;
import com.king.app.workhelper.adapter.viewpager.MyViewPagerAdapter;
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
    @BindView(R.id.king_view_pager) ViewPager mViewPager;
    private MyViewPagerAdapter mViewPagerAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_view_pager;
    }

    @Override protected void initData() {
        super.initData();
        mViewPagerAdapter = new MyViewPagerAdapter(R.layout.layout_view_pager, fakeData());
        mViewPager.setAdapter(mViewPagerAdapter);
    }

    private List<Student> fakeData() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(20, "aaa"));
        students.add(new Student(90, "bbb"));
        return students;
    }

}
