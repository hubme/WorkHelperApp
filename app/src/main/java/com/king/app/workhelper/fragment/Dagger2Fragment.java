package com.king.app.workhelper.fragment;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.drgger2.DaggerModule;
import com.king.app.workhelper.drgger2.DaggerModuleComponent;
import com.king.app.workhelper.model.dagger2.DaggerModel;
import com.king.applib.log.Logger;

import javax.inject.Inject;

/**
 * @author VanceKing
 * @since 2018/4/26.
 */

public class Dagger2Fragment extends AppBaseFragment {
    @Inject
    DaggerModel mModel;

    @Override protected int getContentLayout() {
        return R.layout.fragment_dagger2;
    }
    

    @Override protected void initInitialData() {
        super.initInitialData();
        DaggerModuleComponent.builder()
                .daggerModule(new DaggerModule(this))
                .build().inject(this);
    }

    @Override protected void initData() {
        super.initData();

        Logger.i(mModel.toString());
    }
}

