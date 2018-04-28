package com.king.app.workhelper.drgger2;

import com.king.app.workhelper.app.WorkHelperApp;

import javax.inject.Singleton;

import dagger.Component;

/**
 * 指明 Component 在哪些Module中查找依赖。通过 Processor 自动生成实现类。
 *
 * @author VanceKing
 * @since 2018/4/27.
 */
@Singleton//标明该Component中有Module使用了@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    //注入方法，在 Client 中调用，完成对象的赋值工作。
    void inject(WorkHelperApp application);
}
