package com.king.app.workhelper.activity;

import android.content.Intent;
import android.view.View;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.fragment.EntryFragment;
import com.king.app.workhelper.ui.guaid.GuideHelper;
import com.king.applib.log.Logger;
import com.squareup.otto.Subscribe;

public class MainActivity extends AppBaseActivity {

    @Override
    public int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();

        EntryFragment mEntryFragment = new EntryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mEntryFragment).commit();
    }

    @Subscribe
    public void receiveHaHa(String text) {
        Logger.i("text: " + text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showUserGuide() {
        GuideHelper guideHelper = new GuideHelper(this);
        GuideHelper.TipData tipData = new GuideHelper.TipData(R.mipmap.guaid_tip, new View(this));
        guideHelper.addPage(tipData);
        guideHelper.show(false);
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
