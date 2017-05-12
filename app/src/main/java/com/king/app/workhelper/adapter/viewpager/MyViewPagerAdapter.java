package com.king.app.workhelper.adapter.viewpager;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.king.app.workhelper.R;
import com.king.app.workhelper.model.entity.Student;
import com.king.applib.base.BaseViewPagerAdapter;
import com.king.applib.base.ViewHolder;

import java.util.List;

/**
 * @author huoguangxu
 * @since 2017/5/12.
 */

public class MyViewPagerAdapter extends BaseViewPagerAdapter<Student> {

    public MyViewPagerAdapter(@LayoutRes int layoutId) {
        super(layoutId);
    }

    public MyViewPagerAdapter(@LayoutRes int layoutId, List<Student> data) {
        super(layoutId, data);
    }

    @Override public View buildPagerView(int position) {
        return null;
    }

    @Override public void convert(int position, ViewHolder holder, Student student) {
        holder.setText(R.id.tv_name, student.name);
        holder.setText(R.id.tv_age, String.valueOf(student.age));
    }
}
