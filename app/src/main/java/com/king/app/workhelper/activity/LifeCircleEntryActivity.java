package com.king.app.workhelper.activity;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.databinding.ActivityLifeCircleEntryBinding;

/**
 * @author VanceKing
 * @since 2018/12/13.
 */
public class LifeCircleEntryActivity extends AppBaseActivity {

    private ActivityLifeCircleEntryBinding mViewBinding;

    @Nullable
    @Override
    protected View getContentView() {
        mViewBinding = ActivityLifeCircleEntryBinding.inflate(getLayoutInflater());
        return mViewBinding.getRoot();
    }

    @Override
    protected void initContentView() {
        super.initContentView();

        setViewClickListeners(mViewBinding.tvOpen, mViewBinding.tvOpenTransparent, mViewBinding.tvOpenDialog);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        final int id = v.getId();
        if (id == R.id.tv_open) {
            openActivity(LifeCircleActivity.class);
        } else if (id == R.id.tv_open_transparent) {//A打开透明主题的B，A不会执行onStop()方法。
            openActivity(TransparentActivity.class);
        } else if (id == R.id.tv_open_dialog) {//AlertDialog 不影响生命周期
            new AlertDialog.Builder(this)
                    .setMessage("弹窗")
                    .create()
                    .show();
        }
    }

    protected void openActivity(@NonNull Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}
