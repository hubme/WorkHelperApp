package com.king.applib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * 懒加载Fragment.只初始化View,不加载网络数据.
 *
 * @author huoguangxu
 * @since 2017/2/9.
 */

public abstract class BasePageFragment extends BaseFragment {
    protected boolean mIsViewInitiated = false;
    protected boolean mIsVisibleToUser = false;
    protected boolean mIsDataInitiated = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIsViewInitiated = true;
        prepareFetchData();
    }

    //要结合FragmentPagerAdapter使用才调用此方法
    //setUserVisibleHint(false)->setUserVisibleHint(true)->onActivityCreated()
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }

    protected abstract void lazyData();

    private void prepareFetchData() {
        if (mIsViewInitiated && mIsVisibleToUser && (!mIsDataInitiated || isForceFetchData())) {
            lazyData();
            mIsDataInitiated = true;
        }
    }

    /**
     * 子类重写是否定位到当前页面时就加载数据.默认只有第一次到当前页面时加载一次数据
     */
    protected boolean isForceFetchData() {
        return false;
    }
}
