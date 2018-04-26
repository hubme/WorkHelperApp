package com.king.app.workhelper.drgger2;

import com.king.app.workhelper.fragment.Dagger2Fragment;

import dagger.Component;

/**
 * Component: 注入器，作用是将产生的对象注入到需要对象的容器中，供容器使用。
 *
 * @author VanceKing
 * @since 2018/4/26.
 */

@Component(modules = DaggerModule.class)
public interface ModuleComponent {
    void inject(Dagger2Fragment fragment);
}
