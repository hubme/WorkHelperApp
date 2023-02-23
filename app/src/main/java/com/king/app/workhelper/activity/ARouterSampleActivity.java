package com.king.app.workhelper.activity;

import android.util.Log;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.king.app.workhelper.R;
import com.example.biz_export.home.IHomeService;
import com.king.app.workhelper.common.AppBaseActivity;

import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2019/2/19.
 */

@Route(path = "/activity/arouter")
public class ARouterSampleActivity extends AppBaseActivity {
    //方式1
    @Autowired(name = "/service/hello")
    IHomeService mHelloService;
    
    @Autowired
    String name;
    
    @Override protected int getContentLayout() {
        return R.layout.activity_arouter_sample;
    }

    @Override protected void initData() {
        ARouter.getInstance().inject(this);
        // 方式2
        //IService service1 = ARouter.getInstance().navigation(IService.class);
        // 方式3
        //IService service2 = (IService) ARouter.getInstance().build("/service/hello").navigation();
    }

    @OnClick(R.id.tv_provider_service)
    public void onProviderServiceClick(TextView textView) {
        Log.i(TAG, "onProviderServiceClick.name 传递的参数是：" + name);
        mHelloService.sayHello(this);
    }
}
