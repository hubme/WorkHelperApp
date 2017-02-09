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
        prepareFetchData(false);
    }

    //用在FragmentPagerAdapter
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.mIsVisibleToUser = isVisibleToUser;
        prepareFetchData(false);
    }

    protected abstract void fetchData();

    protected boolean prepareFetchData(boolean forceFetch) {
        if (mIsViewInitiated && mIsVisibleToUser && (!mIsDataInitiated || forceFetch)) {
            fetchData();
            mIsDataInitiated = true;
            return true;
        }
        return false;
    }

}
