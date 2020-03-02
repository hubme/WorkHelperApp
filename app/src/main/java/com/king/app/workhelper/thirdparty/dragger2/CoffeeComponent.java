package com.king.app.workhelper.thirdparty.dragger2;

import javax.inject.Singleton;

import dagger.Component;

/**
 * 该接口会在编译时自动生成对应的实现类，这里是 DaggerCoffeeComponent。
 *
 * @author VanceKing
 * @since 2020/2/27.
 */
/*
这里是声明要从CoffeeModule中去找对应的依赖，从 CoffeeModule 中通过 provide 方法来获取对应的对象。
注意:@Component(modules = {AModule.class,BModule.class})可以设置多个Module，
比如 @Component(modules = {MainModule.class}, dependencies = AppConponent.class) 指定依赖的Module和父Component
 */
@Singleton
@Component(modules = CoffeeModule.class)
public interface CoffeeComponent {
    // 提供一个供目标类使用的注入方法,该方法表示要将Module中的管理类注入到哪个类中，这里当然是CoffeeMaker，因为我们要用他俩去生产咖啡
    void inject(CoffeeMaker maker);

}
