package com.king.app.workhelper.drgger2;

import com.king.app.workhelper.fragment.Dagger2Fragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Component: 注入器，作用是将产生的对象注入到需要对象的容器中，供容器使用。
 *
 * @author VanceKing
 * @since 2018/4/26.
 */
@Singleton//表明 Module 中有使用 @Singleton。不写会编译出错。
@PerActivity//表明 Module 中有使用 @PerActivity
@Component(modules = DaggerModule.class)
public interface ModuleComponent {
    void inject(Dagger2Fragment fragment);
}
