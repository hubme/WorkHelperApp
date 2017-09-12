package com.king.app.workhelper.activity;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;

/**
 * @author VanceKing
 * @since 2017/3/30.
 */

public class RecyclerViewFragment extends AppBaseFragment {


    @Override public int getContentLayout() {
        return R.layout.activity_recycler_view;
    }

    @Override protected void initData() {
        super.initData();
        
    }

    /*@Override protected void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbar.inflateMenu(R.menu.menu_recycler);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add:
                        mRecyclerAdapter.addData(2, new StringEntity("哈哈哈"));
                        return true;
                    case R.id.delete:
//                        mRecyclerAdapter.deleteData(2);
                        mHeaderFooterAdapter.removeFooterView(0);
                        return true;
                    case R.id.modify:
                        mRecyclerAdapter.modifyData(6, new StringEntity("哈哈哈"));
                        return true;
                }
                return false;
            }
        });
    }*/
}
