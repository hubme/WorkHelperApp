package com.king.app.workhelper.thirdparty.dragger2;

import dagger.Component;

/**
 * @author VanceKing
 * @since 2020/2/28.
 */
@ActivityScope
@Component(dependencies = BaseComponent.class)
public interface HeaterComponent {
    // 因为这里HeaterComponent依赖了BaseComponent，所以这里就有proviceHetear方法
    void inject(ActivityA maker);

    void inject(ActivityB maker);
}
