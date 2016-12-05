package com.king.app.workhelper.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;

import butterknife.BindView;

/**
 * Toolbar 使用.
 * see also:http://www.jianshu.com/p/79604c3ddcae
 * Created by HuoGuangxu on 2016/12/5.
 */

public class ToolbarActivity extends AppBaseActivity {
    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @Override
    protected void initInitialData() {
        super.initInitialData();
        setSupportActionBar(mToolbar);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //方式二添加菜单,菜单键
        /*getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu.add("添加");*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void initData() {
        super.initData();
        mToolbar.setNavigationIcon(R.mipmap.arrow_left_blue);
        mToolbar.setLogo(R.mipmap.ic_launcher);
        mToolbar.setTitle("标题");
        mToolbar.setSubtitle("副标题");

        //方式一添加菜单，屏幕右上角
        mToolbar.inflateMenu(R.menu.menu_toolbar);
        Menu menu = mToolbar.getMenu();
        menu.add("添加");

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.editor:
                        Toast.makeText(ToolbarActivity.this, "编辑", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
        mToolbar.setOverflowIcon(getResources().getDrawable(R.mipmap.ic_setting));

        //设置溢出菜单样式
        mToolbar.setPopupTheme(R.style.OverflowMenuStyle);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                finish();
            }
        });
    }

    public static void startToolbarActivity(Context context) {
        Intent intent = new Intent(context, ToolbarActivity.class);
        context.startActivity(intent);
    }
}
