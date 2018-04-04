package com.king.app.workhelper.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.databinding.ActivityDataBindingSampleBinding;
import com.king.app.workhelper.model.databinding.UserModel;
import com.king.applib.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://developer.android.com/topic/libraries/data-binding/index.html
 *
 * @author VanceKing
 * @since 2018/4/4.
 */

public class DataBindingActivity extends AppCompatActivity implements View.OnClickListener {
    public ObservableBoolean mShow = new ObservableBoolean(true);
    private ActivityDataBindingSampleBinding mViewDataBinding;
    private UserModel mUser;
    private int index;
    private List<String> friends;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //根据 activity 布局名称生成对应的类型。IDE 自动生成，可能需要clean rebuild
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding_sample);

        initData();
        mViewDataBinding.setClicklistener(this);
        mViewDataBinding.setClicklistener2(new OnClickListener());

    }

    private void initData() {
        mUser = new UserModel(null, 18);
        mUser.gender.set("男");

        mViewDataBinding.setUser(mUser);
        mViewDataBinding.setIdCard(null);

        friends = new ArrayList<>();
        friends.add("张三");
        friends.add("李四");
        mViewDataBinding.setFriendsList(friends);

        Map<String, String> address = new HashMap<>();
        address.put("011", "北京");
        address.put("021", "上海");
        mViewDataBinding.setAddressMap(address);

        mViewDataBinding.setMyArray(new String[]{"Array"});
    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.click_id1:
                Logger.i(((TextView) v).getText().toString());
                break;
            case R.id.click_id2:
                Logger.i(((TextView) v).getText().toString());
                break;
            case R.id.text_update:
                mUser.setName("张三_" + index);
                mUser.setAge(18 + index);
                mViewDataBinding.setUser(mUser);
                friends.set(0, "王五");
                mUser.gender.set(index % 2 == 0 ? "男" : "女");
                index++;
                break;
            default:
                break;
        }
    }

    public static class OnClickListener {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.click_id3:
                    Logger.i(((TextView) view).getText().toString());
                    break;
                default:
                    break;
            }
        }
    }
}
