package com.king.app.workhelper.fragment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.adapter.recyclerview.SimpleRecyclerAdapter;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.constant.GlobalConstant;
import com.king.applib.ui.recyclerview.listener.RecyclerItemTouchListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2018/8/22.
 */
public class RecyclerSimpleFragment extends AppBaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private SimpleRecyclerAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private List<String> mDataList;
    private VHAdapter mBasicAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_simple_recycler;
    }

    @Override
    protected void initInitialData() {
        super.initInitialData();
        mDataList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            mDataList.add("item - " + i);
        }
    }

    @Override
    protected void initContentView(View rootView) {
        super.initContentView(rootView);

        basicUse();
    }

    private void basicUse() {
        //创建和设置布局管理器
        //new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        //new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, true);
        //new StaggeredGridLayoutManager(3, RecyclerView.VERTICAL);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

//        mRecyclerView.addItemDecoration(new MyItemDecoration());
        //设置 item 对其方式
//        PagerSnapHelper mPagerSnapHelper = new PagerSnapHelper();
//        mPagerSnapHelper.attachToRecyclerView(mRecyclerView);

        //创建和设置适配器
        mBasicAdapter = new VHAdapter(mDataList);
        mRecyclerView.setAdapter(mBasicAdapter);

        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(mBasicAdapter, true, true));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemTouchListener(mRecyclerView, new RecyclerItemTouchListener.OnRecyclerItemListenerAdapter() {
            @Override
            public void onItemClick(View view, int position) {
                super.onItemClick(view, position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                super.onItemLongClick(view, position);
                //固定第一项
                if (position != 0) {
                    itemTouchHelper.startDrag(mRecyclerView.getChildViewHolder(view));
                }
            }
        }));
    }

    private static class MyItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);

            String text = "我是底部 ItemDecoration";
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setTextSize(30);
            paint.setColor(ContextCompat.getColor(parent.getContext(), R.color.chocolate));
            c.drawText(text, 0, text.length(), 200, 100, paint);

        }

        @Override
        public void onDrawOver(@NotNull Canvas c, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
            super.onDrawOver(c, parent, state);

            String text = "我是顶部 ItemDecoration";
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setTextSize(30);
            paint.setColor(ContextCompat.getColor(parent.getContext(), R.color.chocolate));
            c.drawText(text, 0, text.length(), 200, 150, paint);

        }

        @Override
        public void getItemOffsets(@NotNull Rect outRect, @NotNull View view, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (parent.getChildAdapterPosition(view) == 5) {
                outRect.set(5, 4, 10, 4);
            }
        }
    }

    private void recyvlerViewNestScroll() {
        //设置 false。解决 NestScrollView 嵌套 RecyclerView 时 notifyItemRangeInserted() 无效的问题。
        //但是会渲染整个 List，不管是不是显示在屏幕上。
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);
//        ViewCompat.setNestedScrollingEnabled(mRecyclerView, false);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new SimpleRecyclerAdapter(SimpleRecyclerAdapter.fakeData(30));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.i(GlobalConstant.LOG_TAG, "last visible position: " + layoutManager.findLastVisibleItemPosition());
                }
            }
        });
    }

    @OnClick(R.id.floating_button)
    public void onFloatingButtonClick() {
//        mAdapter.appendList(SimpleRecyclerAdapter.fakeData("aaa", 5));

        mBasicAdapter.mDataList.add("AAA");
        mBasicAdapter.notifyItemInserted(mBasicAdapter.getItemCount() - 1);
    }

    //自定义 ViewHolder，保存 itemView 的 childView 的引用。
    private static class VH extends RecyclerView.ViewHolder {
        private TextView mTextView;

        VH(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(android.R.id.text1);
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mTextView.setBackgroundColor(Color.parseColor("#9900FFFF"));
        }
    }

    //创建 Adapter，绑定数据源。
    private static class VHAdapter extends RecyclerView.Adapter<VH> {
        private List<String> mDataList;

        VHAdapter(List<String> dataList) {
            this.mDataList = dataList;
        }

        //1. 通过 xml 解析出 itemView 对象。2. 返回 ViewHolder
        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, null);
            return new VH(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            holder.mTextView.setText(mDataList.get(position));
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        List<String> getDataList() {
            return mDataList;
        }
    }

    private static class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
        VHAdapter mAdapter;
        boolean isSwipeEnable;
        //第一个 item 是否可以移动的标记
        boolean isFirstDragUnable;

        ItemTouchHelperCallback(VHAdapter adapter) {
            mAdapter = adapter;
            isSwipeEnable = true;
            isFirstDragUnable = false;
        }

        ItemTouchHelperCallback(VHAdapter adapter, boolean isSwipeEnable, boolean isFirstDragUnable) {
            mAdapter = adapter;
            this.isSwipeEnable = isSwipeEnable;
            this.isFirstDragUnable = isFirstDragUnable;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder) {
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            } else {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }
        }

        @Override
        public boolean onMove(@NotNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            if (isFirstDragUnable && toPosition == 0) {
                return false;
            }
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(mAdapter.getDataList(), i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(mAdapter.getDataList(), i, i - 1);
                }
            }
            mAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int adapterPosition = viewHolder.getAdapterPosition();
            mAdapter.notifyItemRemoved(adapterPosition);
            mAdapter.getDataList().remove(adapterPosition);
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                viewHolder.itemView.setBackgroundColor(Color.YELLOW);
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        //拖拽结束后回调
        @Override
        public void clearView(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setBackgroundColor(Color.RED);
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return !isFirstDragUnable;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return isSwipeEnable;
        }
    }


}
