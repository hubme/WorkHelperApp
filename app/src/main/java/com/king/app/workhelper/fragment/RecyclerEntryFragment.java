package com.king.app.workhelper.fragment;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;

import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2017/9/12.
 */

public class RecyclerEntryFragment extends AppBaseFragment {

    @Override protected int getContentLayout() {
        return R.layout.activity_recycler_entry;
    }

    @OnClick(R.id.tv_simple_recycler_view)
    public void onSimpleRecyclerView() {
        clickedOn(new RecyclerSimpleFragment());
    }

    @OnClick(R.id.tv_header_footer)
    public void onHeaderFooterClick() {
        clickedOn(new RecyclerHeaderFooterFragment());
    }
    
    @OnClick(R.id.tv_refresh_more)
    public void onRefreshMoreClick() {
        clickedOn(new RecyclerRefreshMoreFragment());
    }
    
    @OnClick(R.id.tv_swipe_menu)
    public void onSwipeMenuClick() {
        clickedOn(new RecyclerSwipeMenuFragment());
    }
    
    @OnClick(R.id.tv_scroll)
    public void onScrollClick() {
        clickedOn(new RecyclerScrollFragment());
    }

    private void clickedOn(@NonNull Fragment fragment) {
        final String tag = fragment.getClass().toString();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(tag)
                .replace(R.id.layout_container, fragment, tag)
                .commit();
    }
}
