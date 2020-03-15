package com.king.applib.ui.recyclerview;

import android.content.Context;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.king.applib.R;
import com.king.applib.ui.customview.ProgressWheel;
import com.king.applib.ui.recyclerview.listener.OnLoadMoreHandler;

/**
 * @author VanceKing
 * @since 2017/9/12.
 */

public class LoadMoreView extends FrameLayout implements OnLoadMoreHandler {
    private ProgressWheel mLoadingView;
    private TextView mLoadMoreDesc;

    public LoadMoreView(@NonNull Context context) {
        this(context, null);
    }

    public LoadMoreView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_loading_more_view, this, true);
        mLoadingView = (ProgressWheel) rootView.findViewById(R.id.loading_view);
        mLoadMoreDesc = (TextView) rootView.findViewById(R.id.tv_load_more_desc);
    }

    @Override public void onLoading() {
        mLoadingView.setVisibility(View.VISIBLE);
        mLoadMoreDesc.setVisibility(View.INVISIBLE);
    }

    @Override public void onLoadError(int code, String desc) {
        mLoadingView.setVisibility(View.INVISIBLE);
        mLoadMoreDesc.setVisibility(View.VISIBLE);
        mLoadMoreDesc.setText(desc);
    }

    @Override public void onLoadComplete() {

    }

    @Override public void onNoMoreData(String desc) {
        mLoadingView.setVisibility(View.INVISIBLE);
        mLoadMoreDesc.setVisibility(View.VISIBLE);
        mLoadMoreDesc.setText(desc);
    }
}
