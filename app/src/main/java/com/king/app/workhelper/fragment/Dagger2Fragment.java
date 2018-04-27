package com.king.app.workhelper.fragment;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.drgger2.DaggerModule;
import com.king.app.workhelper.drgger2.DaggerModuleComponent;
import com.king.app.workhelper.model.dagger2.DaggerModel;
import com.king.applib.log.Logger;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * 1. Client 里有需要被初始化的元素。需要被初始化的元素必须标上@Inject，只有被标上@Inject的元素才会被自动初始化。@Inject
 * 在Dagger2中一般标记构造方法与成员变量。
 * 2. Module 可以说就是依赖的原材料的制造工厂，所有需要被注入的元素的实现都是从Module生产的。
 * 3. 有了 Client 和 Product 就需要将依赖对象传给 Client，这个过程由Component来执行。Component将Module中产生的依赖对象自动注入到 Client 中。
 *
 * @author VanceKing
 * @since 2018/4/26.
 */

public class Dagger2Fragment extends AppBaseFragment {
    @Inject @Named("aaa") DaggerModel mModel;
    @Inject @Named("one") DaggerModel mModel2;
//    @Inject @AQualifier DaggerModel mModel3;

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
        Logger.i(mModel2.toString());
//        Logger.i(mModel3.toString());
    }
}

