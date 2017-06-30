package com.king.app.workhelper.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.king.app.workhelper.R;
import com.king.app.workhelper.adapter.recyclerview.BasicMultiRecyclerAdapter2;
import com.king.app.workhelper.adapter.recyclerview.BasicMultiRecyclerAdapter3;
import com.king.app.workhelper.adapter.recyclerview.BasicMultipleRecyclerAdapter;
import com.king.app.workhelper.adapter.recyclerview.SimpleRecyclerAdapter;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.model.entity.StringEntity;
import com.king.applib.ui.recyclerview.RecyclerDivider;

import butterknife.BindView;

/**
 * @author huoguangxu
 * @since 2017/3/30.
 */

public class RecyclerViewActivity extends AppBaseActivity {


    @BindView(R.id.rv_mine) RecyclerView mMineRv;
    private SimpleRecyclerAdapter mRecyclerAdapter;

    @Override public int getContentLayout() {
        return R.layout.activity_recycler_view;
    }

    @Override protected String getActivityTitle() {
        return "RecyclerView";
    }

    @Override protected void initData() {
        super.initData();

        //6.0设置setNestedScrollingEnabled(false)只显示一行，ScrollView换成NestedScrollView即可.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mMineRv.setLayoutManager(layoutManager);
        mMineRv.setNestedScrollingEnabled(false);//解决滑动冲突
        
//        mRecyclerAdapter = getAdapter2();
        mMineRv.setAdapter(getDelegateAdapter());

//        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        itemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.ll_h_divider));

        RecyclerDivider itemDecoration = new RecyclerDivider(RecyclerDivider.VERTICAL, ContextCompat.getColor(this, R.color.chocolate));
//        itemDecoration.setMargin(15, 0, 15, 0);
//        MyItemDivider itemDecoration = new MyItemDivider(20);
        mMineRv.addItemDecoration(itemDecoration);

        mMineRv.setItemAnimator(new DefaultItemAnimator()); //即使不设置,默认也是这个动画

    }

    @Override protected void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbar.inflateMenu(R.menu.menu_recycler);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add:
                        mRecyclerAdapter.addData(2, new StringEntity("哈哈哈"));
                        return true;
                    case R.id.delete:
                        mRecyclerAdapter.deleteData(2);
                        return true;
                    case R.id.modify:
                        mRecyclerAdapter.modifyData(6, new StringEntity("哈哈哈"));
                        return true;
                }
                return false;
            }
        });
    }

    private SimpleRecyclerAdapter getAdapter2() {
        return new SimpleRecyclerAdapter(R.layout.layout_simple_text_view, SimpleRecyclerAdapter.fakeData());
    }

    private BasicMultipleRecyclerAdapter getStringAdapter() {
        return new BasicMultipleRecyclerAdapter(BasicMultipleRecyclerAdapter.fakeMultiTypeData());
    }

    private BasicMultiRecyclerAdapter2 getMultiRecyclerAdapter() {
        return new BasicMultiRecyclerAdapter2(BasicMultiRecyclerAdapter2.fakeMultiTypeData());
    }

    private BasicMultiRecyclerAdapter3 getDelegateAdapter() {
        return new BasicMultiRecyclerAdapter3(BasicMultiRecyclerAdapter3.fakeMultiTypeData());
    }
}
