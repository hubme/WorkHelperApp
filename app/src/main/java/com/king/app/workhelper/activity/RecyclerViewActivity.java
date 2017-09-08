package com.king.app.workhelper.activity;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.Space;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.adapter.recyclerview.AdvanceRecyclerAdapter;
import com.king.app.workhelper.adapter.recyclerview.BasicMultiRecyclerAdapter2;
import com.king.app.workhelper.adapter.recyclerview.BasicMultiRecyclerAdapter3;
import com.king.app.workhelper.adapter.recyclerview.BasicMultipleRecyclerAdapter;
import com.king.app.workhelper.adapter.recyclerview.HeaderAndFooterAdapter;
import com.king.app.workhelper.adapter.recyclerview.SimpleRecyclerAdapter;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.model.entity.StringEntity;
import com.king.applib.ui.recyclerview.RecyclerDivider;

import java.util.Arrays;

import butterknife.BindView;

/**
 * @author huoguangxu
 * @since 2017/3/30.
 */

public class RecyclerViewActivity extends AppBaseActivity {


    @BindView(R.id.rv_mine) RecyclerView mMineRv;
    private SimpleRecyclerAdapter mRecyclerAdapter;
    private LinearLayoutManager layoutManager;

    @Override public int getContentLayout() {
        return R.layout.activity_recycler_view;
    }

    @Override protected String getActivityTitle() {
        return "RecyclerView";
    }

    @Override protected void initData() {
        super.initData();

        //6.0设置setNestedScrollingEnabled(false)只显示一行，ScrollView换成NestedScrollView即可.
        layoutManager = new LinearLayoutManager(this);
        mMineRv.setLayoutManager(layoutManager);
        mMineRv.setNestedScrollingEnabled(false);//解决滑动冲突
        mMineRv.addOnScrollListener(new MyRecyclerViewScrollListener());

        mRecyclerAdapter = getSimpleAdapter();
        AdvanceRecyclerAdapter mHeaderFooterAdapter = getHeaderFooterAdapter();
        
        mHeaderFooterAdapter.addHeaderViews(Arrays.asList(buildHeaderView("Header One"), buildSpaceView(), buildHeaderView("Header Two")));
        mHeaderFooterAdapter.addFooterViews(Arrays.asList(buildFooterView("Footer One"), buildSpaceView(), buildFooterView("Footer Two")));
        mMineRv.setAdapter(mHeaderFooterAdapter);

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

    private SimpleRecyclerAdapter getSimpleAdapter() {
        return new SimpleRecyclerAdapter(SimpleRecyclerAdapter.fakeData());
    }

    private HeaderAndFooterAdapter getHeaderFooterAdapter() {
        return new HeaderAndFooterAdapter(this, HeaderAndFooterAdapter.fakeData());
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

    private class MyRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE://表示当前并处于静止状态
                    String result = "";
                    int visibleCount = layoutManager.getChildCount();
                    int lastPos = layoutManager.findLastVisibleItemPosition();
                    int totalCount = layoutManager.getItemCount();
                    if (visibleCount > 0 && lastPos >= totalCount - 1) {
                        result = "滑动到最后了。";// TODO: 2017/9/7 经常触发多次导致数据重复 
                    }
//                    Log.i("aaa", "SCROLL_STATE_IDLE." + result);
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING://标识当前RecyclerView处于滑动状态（手指在屏幕上）
//                    Log.i("aaa", "SCROLL_STATE_DRAGGING");
                    break;
                case RecyclerView.SCROLL_STATE_SETTLING://表示当前RecyclerView处于从滑动状态到静止状态（手已经离开屏幕）
//                    Log.i("aaa", "SCROLL_STATE_SETTLING.");
                    break;
                default:

                    break;
            }
        }

        @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            /*Log.i("aaa", "第一个可见View的下标: " + layoutManager.findFirstVisibleItemPosition() +
                    ";第一个完全可见View的下标: " + layoutManager.findFirstCompletelyVisibleItemPosition() +
                    ";最后一个可见View的下标: " + layoutManager.findLastVisibleItemPosition() +
                    ";最后一个完全可见View的下标: " + layoutManager.findLastCompletelyVisibleItemPosition() +
                    ";当前屏幕中显示的View个数: " + layoutManager.getChildCount() +
                    ";总个数: " + layoutManager.getItemCount());*/

        }
    }

    private View buildHeaderView(String headerText) {
        View panel = LayoutInflater.from(this).inflate(R.layout.layout_recycler_header, null, false);
        ((TextView)panel.findViewById(R.id.tv_header)).setText(headerText);
        panel.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                showToast(headerText);
            }
        });
        return panel;
    }

    private View buildFooterView(String footerText) {
        View panel = LayoutInflater.from(this).inflate(R.layout.layout_recycler_footer, null, false);
        ((TextView)panel.findViewById(R.id.tv_footer)).setText(footerText);
        panel.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                showToast(footerText);
            }
        });
        return panel;
    }

    private View buildSpaceView() {
        Space space = new Space(this);
        space.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80));
        return space;
    }
}
