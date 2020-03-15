package com.king.app.workhelper.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.king.app.workhelper.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2018/12/13.
 */
public class LifeCircleEntryActivity extends AppCompatActivity {
    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_circle_entry);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_open)
    public void onOpenClick(TextView textView) {
        openActivity(LifeCircleActivity.class);

    }

    //A打开透明主题的B，A不会执行onStop()方法。
    @OnClick(R.id.tv_open_transparent)
    public void onOpenTransparentClick(TextView textView) {
        openActivity(TransparentActivity.class);
    }

    //不影响生命周期
    @OnClick(R.id.tv_open_dialog)
    public void onDialogClick(TextView textView) {
        new AlertDialog.Builder(this)
                .setMessage("弹窗")
                .create()
                .show();
    }

    protected void openActivity(@NonNull Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}
