package com.king.app.workhelper.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;

import butterknife.BindView;

/**
 * see also:http://www.jianshu.com/p/79604c3ddcae
 * Toolbar 使用.
 *
 * @author huoguangxu
 * @since 2016/12/5.
 */

public class ToolbarActivity extends AppBaseActivity {
    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.vs_toolbar_custom)
    public ViewStub mCustomToolbar;

    @Override
    public int getContentLayout() {
        return R.layout.activity_toolbar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //方式二添加菜单,菜单键
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu.add("添加");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editor:
                showToast("编辑");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
//        mCustomToolbar.setLayoutResource(R.layout.toolbar_custom);
//        View view = mCustomToolbar.inflate();

        TextView subView = new TextView(this);
        subView.setText("我的订单");
        subView.setTextColor(getResources().getColor(R.color.white_f5f5f5));
        subView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.ts_huge));
        subView.setLayoutParams(new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
//        mToolbar.addView(subView);

        Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT, Gravity.END);
        params.setMargins(3, 3, 50, 4);//设置外边界
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.ic_search);
        imageView.setLayoutParams(params);
        mToolbar.addView(imageView);

        //方式一添加菜单，屏幕右上角
        /*mToolbar.inflateMenu(R.menu.menu_toolbar);
        Menu menu = mToolbar.getMenu();
        menu.add("添加");*/

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.editor:
                        showToast("编辑");
                        return true;
                    case R.id.share:
                        showToast("分享");
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected String getActivityTitle() {
        return super.getActivityTitle();
    }

    private void toolbarUseSapmle() {
        mToolbar.setNavigationIcon(R.mipmap.arrow_left_blue);
        mToolbar.setLogo(R.mipmap.ic_launcher);
        mToolbar.setTitle("标题");
        mToolbar.setSubtitle("副标题");

        //替换Toolbar右上角的菜单图标
        mToolbar.setOverflowIcon(getResources().getDrawable(R.mipmap.ic_setting));

        //设置溢出菜单样式
        mToolbar.setPopupTheme(R.style.OverflowMenuStyle);
    }

    public static void startToolbarActivity(Context context) {
        Intent intent = new Intent(context, ToolbarActivity.class);
        context.startActivity(intent);
    }
}
