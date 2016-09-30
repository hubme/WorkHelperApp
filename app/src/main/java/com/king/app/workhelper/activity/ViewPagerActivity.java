package com.king.app.workhelper.activity;

import android.content.Intent;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.constant.GlobalConstant;
import com.king.app.workhelper.fragment.FragmentOne;
import com.king.app.workhelper.model.entity.Person;
import com.king.applib.log.Logger;
import com.king.applib.util.ExtendUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewPagerActivity extends AppBaseActivity {

    private FragmentOne mFragmentOne;

    @Override
    public int getContentLayout() {
        return R.layout.activity_view_pager;
    }

    @Override
    public void getIntentData(Intent intent) {
        super.getIntentData(intent);

        //1.不会创建多余的List
        ArrayList<Person> list1 = new ArrayList<>();
        list1.add(new Person("111"));
        list1.add(new Person("222"));

        //2.使用匿名内部类
        ArrayList<Person> list2 = new ArrayList<Person>() {
            {
                add(new Person("111"));
                add(new Person("222"));
            }
        };

        //3.Arrays.asList会产生多余的List对象
        ArrayList<Person> list3 = new ArrayList<>(Arrays.asList(new Person("111"), new Person("222")));

        //4.
        List<Person> defaultList = Arrays.asList(new Person("999"), new Person("888"));
        List<Person> list = getSerializableListExtra(GlobalConstant.BUNDLE_PARAMS_KEY.EXTRA_KEY, defaultList);
        ExtendUtil.printList(list);
        if (ExtendUtil.isListNullOrEmpty(list)) {
            Logger.i("lsit == null");
        }

    }

    @Override
    protected void initContentView() {
        super.initContentView();
        if (mFragmentOne == null) {
            mFragmentOne = new FragmentOne();
        }
        getSupportFragmentManager().beginTransaction().add(R.id.my_frame, mFragmentOne).commit();
    }
}