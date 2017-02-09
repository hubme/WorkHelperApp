package com.king.app.workhelper.fragment;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBasePageFragment;
import com.king.applib.log.Logger;

public class FirstTabFragment extends AppBasePageFragment {

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_first;
    }

    @Override
    protected void fetchData() {
        Logger.i("FirstTabFragment fetchData");
    }

    /*@Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Logger.i("onActivityCreated");
    }

    @Override public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Logger.i("setUserVisibleHint " + isVisibleToUser);
    }*/
}
