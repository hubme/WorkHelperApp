package com.king.app.workhelper.drgger2;

import com.king.app.workhelper.fragment.Dagger2Fragment;
import com.king.app.workhelper.model.dagger2.DaggerModel;

import dagger.Module;
import dagger.Provides;

/**
 * Module: 模块，类似快递箱子，在Component接口中通过@Component(modules =xxxx.class),
 * 将容器需要的商品封装起来，统一交给快递员（Component），让快递员统一送到目标容器中。
 *
 * @author VnaceKing
 * @since 2018/4/26.
 */

@Module
public class DaggerModule {
    private Dagger2Fragment fragment;

    public DaggerModule(Dagger2Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    DaggerModel provideStudent(){
        return new DaggerModel();
    }
}
