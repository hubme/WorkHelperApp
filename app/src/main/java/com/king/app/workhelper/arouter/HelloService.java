package com.king.app.workhelper.arouter;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.biz_export.IHomeService;

/**
 * @author VanceKing
 * @since 2019/2/19.
 */

@Route(path = "/service/hello", name = "测试服务")
public class HelloService implements IHomeService {
    @Override public void sayHello(Context context) {
        Toast.makeText(context, "Hello!", Toast.LENGTH_SHORT).show();
    }

    @Override public void init(Context context) {

    }
}
