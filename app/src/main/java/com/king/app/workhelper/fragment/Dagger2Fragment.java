package com.king.app.workhelper.fragment;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.di.DaggerModule;
import com.king.app.workhelper.di.DaggerModuleComponent;
import com.king.app.workhelper.di.ObjectQualifier;
import com.king.app.workhelper.model.DaggerModel;
import com.king.applib.log.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import dagger.Lazy;

/**
 * 1. Client 里有需要被初始化的元素。需要被初始化的元素必须标上@Inject，只有被标上@Inject的元素才会被自动初始化。@Inject
 * 在Dagger2中一般标记构造方法与成员变量。
 * 2. Module 可以说就是依赖的原材料的制造工厂，所有需要被注入的元素的实现都是从Module生产的。
 * 3. 有了 Client 和 Product 就需要将依赖对象传给 Client，这个过程由Component来执行。Component将Module中产生的依赖对象自动注入到 Client 中。
 * <br/>
 * '@Inject 查找依赖规则
 * 1.该成员变量的依赖会从Module的@Provides方法集合中查找；
 * 2.如果查找不到，则查找成员变量类型是否有@Inject构造方法，并注入构造方法且递归注入该类型的成员变量
 *
 * @author VanceKing
 * @since 2018/4/26.
 */

public class Dagger2Fragment extends AppBaseFragment {
    //@Inject可以标记成员变量，但是这些成员变量要求是包级可见
    @Inject @Named DaggerModel mModel;
    @Inject @Named("one") DaggerModel mModel2;

    @Inject @ObjectQualifier DaggerModel mModel3;
    @Inject @ObjectQualifier(1) DaggerModel mModel4;

    @Inject @Named("per_activity") DaggerModel mModel5;
    @Inject @Named("per_activity") DaggerModel mModel6;

    @Inject @Named("single") DaggerModel mModel7;
    @Inject @Named("single") DaggerModel mModel8;


    // 初始化时就会创建实例对象
    @Inject @ObjectQualifier(2) DaggerModel mLazyProviderContrastModel;
    //使用 Lazy 包装，当使用 get() 时才会创建实例对象
    @Inject @ObjectQualifier(2) Lazy<DaggerModel> mLazyModel;
    //使用 Provider 包装，每次 get() 都会强制调用 Module 中的 Providers 方法，
    // 根据方法实现可能创建多个实例对象，也可能只创建一个对象
    @Inject @ObjectQualifier(2) Provider<DaggerModel> mProviderModel;


    @Override protected int getContentLayout() {
        return R.layout.fragment_dagger2;
    }


    @Override protected void initInitialData() {
        super.initInitialData();
        DaggerModuleComponent.builder()
                .daggerModule(new DaggerModule())
                .build().inject(this);
    }

    @Override protected void initData() {
        super.initData();

        Logger.i(mModel.toString());
        Logger.i(mModel2.toString());
        Logger.i(mModel3.toString());
        Logger.i(mModel4.toString());

        Logger.i(mModel5.toString());
        Logger.i(mModel6.toString());

        Logger.i(mModel7.toString());
        Logger.i(mModel8.toString());

        Logger.i(mLazyProviderContrastModel.toString());
        Logger.i(mLazyModel.get().toString() + " --- " + mLazyModel.get());
        Logger.i(mProviderModel.get().toString() + " --- " + mProviderModel.get().toString());
    }
}

