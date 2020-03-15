package com.king.app.workhelper.fragment;

import androidx.core.content.ContextCompat;
import androidx.legacy.widget.Space;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.adapter.recyclerview.BasicMultiRecyclerAdapter2;
import com.king.app.workhelper.adapter.recyclerview.BasicMultiRecyclerAdapter3;
import com.king.app.workhelper.adapter.recyclerview.BasicMultipleRecyclerAdapter;
import com.king.app.workhelper.adapter.recyclerview.HeaderAndFooterSampleAdapter;
import com.king.app.workhelper.adapter.recyclerview.MyRecyclerAdapter;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.ui.recyclerview.RecyclerDivider;
import com.king.applib.ui.recyclerview.SimpleRecyclerAdapter;
import com.king.applib.ui.recyclerview.SimpleRecyclerView;

import java.util.Arrays;

import butterknife.BindView;

/**
 * @author VanceKing
 * @since 2017/9/12.
 */

public class RecyclerHeaderFooterFragment extends AppBaseFragment {
    @BindView(R.id.swipe_layout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.rv_mine) SimpleRecyclerView mMineRv;
    private MyRecyclerAdapter mRecyclerAdapter;
    private LinearLayoutManager layoutManager;
    private SimpleRecyclerAdapter mHeaderFooterAdapter;
    private View mEmptyView;


    @Override protected int getContentLayout() {
        return R.layout.fragment_recycler_view;
    }

    @Override protected void initData() {
        super.initData();

        //6.0设置setNestedScrollingEnabled(false)只显示一行，ScrollView换成NestedScrollView即可.
        layoutManager = new LinearLayoutManager(mContext);
        mMineRv.setLayoutManager(layoutManager);
        mMineRv.setHasFixedSize(true);
        mMineRv.setNestedScrollingEnabled(false);//解决滑动冲突
        mMineRv.addOnScrollListener(new MyRecyclerViewScrollListener());

        mRecyclerAdapter = getSimpleAdapter();
        mHeaderFooterAdapter = getHeaderFooterAdapter();

        mHeaderFooterAdapter.addHeaderViews(Arrays.asList(buildHeaderView("Header One"), buildSpaceView(), buildHeaderView("Header Two")));
        mHeaderFooterAdapter.addFooterViews(Arrays.asList(buildFooterView("Footer One"), buildSpaceView(), buildFooterView("Footer Two")));
        mMineRv.setAdapter(mHeaderFooterAdapter);

//        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        itemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.ll_h_divider));

        RecyclerDivider itemDecoration = new RecyclerDivider(RecyclerDivider.VERTICAL, ContextCompat.getColor(mContext, R.color.chocolate));
//        itemDecoration.setMargin(15, 0, 15, 0);
//        MyItemDivider itemDecoration = new MyItemDivider(20);
        mMineRv.addItemDecoration(itemDecoration);

        mMineRv.setItemAnimator(new DefaultItemAnimator()); //即使不设置,默认也是这个动画
    }

    @Override protected void initContentView(View rootView) {
        super.initContentView(rootView);
        mEmptyView = buildEmptyView();

        mRefreshLayout.setColorSchemeResources(R.color.chocolate);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
//                mHeaderFooterAdapter.resetAdapterData();
                mHeaderFooterAdapter.setViewState(SimpleRecyclerAdapter.STATE_EMPTY, mEmptyView);
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    private View buildEmptyView() {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(R.drawable.empty_data);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        return imageView;
    }

    private MyRecyclerAdapter getSimpleAdapter() {
        return new MyRecyclerAdapter(MyRecyclerAdapter.fakeData());
    }

    private HeaderAndFooterSampleAdapter getHeaderFooterAdapter() {
        return new HeaderAndFooterSampleAdapter(mContext, HeaderAndFooterSampleAdapter.fakeData());
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
        View panel = LayoutInflater.from(mContext).inflate(R.layout.layout_recycler_header, null, false);
        ((TextView)panel.findViewById(R.id.tv_header)).setText(headerText);
        panel.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                showToast(headerText);
            }
        });
        return panel;
    }

    private View buildFooterView(String footerText) {
        View panel = LayoutInflater.from(mContext).inflate(R.layout.layout_recycler_footer, null, false);
        ((TextView)panel.findViewById(R.id.tv_footer)).setText(footerText);
        panel.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                showToast(footerText);
            }
        });
        return panel;
    }

    private View buildSpaceView() {
        Space space = new Space(mContext);
        space.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80));
        return space;
    }
}
