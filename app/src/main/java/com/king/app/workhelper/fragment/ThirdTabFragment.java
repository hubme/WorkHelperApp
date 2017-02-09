package com.king.app.workhelper.fragment;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBasePageFragment;
import com.king.applib.log.Logger;

public class ThirdTabFragment extends AppBasePageFragment {

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_third;
    }

    @Override 
    protected void fetchData() {
        Logger.i("ThirdTabFragment fetchData");
    }

}
