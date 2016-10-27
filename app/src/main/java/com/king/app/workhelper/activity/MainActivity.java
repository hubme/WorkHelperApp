package com.king.app.workhelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.constant.GlobalConstant;
import com.king.app.workhelper.model.entity.Person;
import com.king.applib.log.Logger;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends AppBaseActivity {
    @BindView(R.id.hello_world)
    TextView mHelloView;


    @Override
    public int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();
        mHelloView.setText(Html.fromHtml(getString(R.string.html_text)));
    }

    @OnClick(R.id.hello_world)
    public void printHelloWorld() {
//        startViewPagerActivity();
//        onPickImage();

    }

    @Subscribe
    public void receiveHaHa(String text) {
        Logger.i("text: " + text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void startViewPagerActivity() {
        Bundle bundle = new Bundle();
        ArrayList<Person> arrayList = new ArrayList<>();
        arrayList.add(new Person("000"));
        arrayList.add(new Person("111"));
        bundle.putSerializable(GlobalConstant.BUNDLE_PARAMS_KEY.EXTRA_KEY, null);

        startActivity(bundle, ViewPagerActivity.class);
    }

    /**
     * 发送信息到指定的邮箱.会打开符合Intent的Activity.
     */
    private void onSendEmail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        //i.setType("text/plain"); //模拟器
        i.setType("message/rfc822"); //真机
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"zhangdeyi@oschina.net"});
        i.putExtra(Intent.EXTRA_SUBJECT, "用户反馈-git@osc Android客户端");
        i.putExtra(Intent.EXTRA_TEXT, "邮件内容");
        startActivity(Intent.createChooser(i, "send email to me..."));
    }

    /**
     * 选择相册中的图片
     */
    private void onPickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "选择图片"), 0);
    }
}
