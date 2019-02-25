package com.king.app.workhelper.activity;

import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.king.app.workhelper.R;
import com.king.app.workhelper.arouter.HelloService;
import com.king.app.workhelper.arouter.IService;
import com.king.app.workhelper.common.AppBaseActivity;

import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2019/2/19.
 */

@Route(path = "/activity/arouter")
public class ARouterSampleActivity extends AppBaseActivity {
    @Autowired(name = "/service/hello")
    IService mHelloService;
    
    @Autowired
    String name;
    
    @Override protected int getContentLayout() {
        return R.layout.activity_arouter_sample;
    }

    @Override protected void initData() {
        ARouter.getInstance().inject(this);
        HelloService service1 = ARouter.getInstance().navigation(HelloService.class);
        HelloService service2 = (HelloService) ARouter.getInstance().build("/service/hello").navigation();
    }

    @OnClick(R.id.tv_provider_service)
    public void onProviderServiceClick(TextView textView) {
        mHelloService.sayHello(this);
    }
}
