package com.king.app.workhelper.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.model.entity.StringEntity;
import com.king.app.workhelper.ui.recyclerview.BaseRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author huoguangxu
 * @since 2017/3/30.
 */

public class RecyclerViewActivity extends AppBaseActivity {


    @BindView(R.id.rv_mine) RecyclerView mMineRv;

    @Override public int getContentLayout() {
        return R.layout.activity_recycler_view;
    }

    @Override protected String getActivityTitle() {
        return "RecyclerView";
    }

    @Override protected void initData() {
        super.initData();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mMineRv.setLayoutManager(mLayoutManager);
        StringAdapter mStringAdapter = new StringAdapter();
//        TypeStringAdapter mStringAdapter = new TypeStringAdapter();
        mStringAdapter.setAdapterData(fakeData());
        mMineRv.setAdapter(mStringAdapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.ll_h_divider));
        mMineRv.addItemDecoration(itemDecoration);
    }

    private List<StringEntity> fakeData() {
        List<StringEntity> data = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            if (i % 3 == 0) {
                data.add(new StringEntity("category" + i, StringEntity.ItemType.CATEGORY));
            } else {
                data.add(new StringEntity("item " + i, StringEntity.ItemType.CONTENT));
            }
        }
        return data;
    }

    private class StringAdapter extends RecyclerView.Adapter<ContentViewHolder> {
        private List<StringEntity> mStrings = new ArrayList<>();

        public StringAdapter() {
        }

        public StringAdapter(List<StringEntity> strings) {
            if (strings != null && !strings.isEmpty()) {
                mStrings.addAll(strings);
            }
        }

        public void setAdapterData(List<StringEntity> data) {
            setAdapterData(data, false);
        }

        public void setAdapterData(List<StringEntity> data, boolean append) {
            if (append) {
                mStrings.addAll(data);
            } else {
                mStrings.clear();
                mStrings.addAll(data);
            }
        }

        @Override public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            if (viewType == StringEntity.ItemType.CONTENT) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_simple_text_view, parent, false);
            } else {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_text_category, parent, false);
            }
            return new ContentViewHolder(view);
        }

        @Override public void onBindViewHolder(ContentViewHolder holder, int position) {
            final StringEntity entity = mStrings.get(position);
            if (holder.getItemViewType() == StringEntity.ItemType.CONTENT) {
                holder.content.setText(entity.text);
                holder.content.setOnClickListener(v -> showToast("content: " + entity.text));
            } else {
                TextView textView = holder.categoryName;
                if (textView != null) {
                    textView.setText(entity.text);
                }
            }
            
            
        }

        @Override public int getItemViewType(int position) {
            return mStrings.get(position).type;
        }

        @Override public int getItemCount() {
            return mStrings == null ? 0 : mStrings.size();
        }
    }

    private class TypeStringAdapter extends BaseRecyclerViewAdapter<StringEntity, ContentViewHolder> {

        @Override public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            if (viewType == StringEntity.ItemType.CONTENT) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_simple_text_view, parent, false);
            } else {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_text_category, parent, false);
            }
            return new ContentViewHolder(view);
        }

        @Override public void onBindViewHolder(ContentViewHolder holder, int position) {
            final StringEntity entity = getAdapterData().get(position);
            if (entity.type == StringEntity.ItemType.CONTENT) {
                TextView content = holder.content;
                content.setText(entity.text);
                content.setOnClickListener(v -> showToast("content: " + entity.text));
            } else {
                TextView textView = holder.categoryName;
                if (textView != null) {
                    textView.setText(entity.text);
                }
            }
        }

        @Override public int getItemViewType(int position) {
            return getAdapterData().get(position).type;
        }
    }


    private class ContentViewHolder extends RecyclerView.ViewHolder {
        private final TextView content;
        private final TextView categoryName;

        private ContentViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.tv_item_input);
            categoryName = (TextView) findViewById(R.id.tv_category_name);
        }
        
    }
}
