package com.king.app.workhelper.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;

import com.king.app.workhelper.R;

/**
 * @author VanceKing
 * @since 2018/5/18.
 */
public class LayoutInflaterActivity extends AppCompatActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        View inflate = getLayoutInflater().inflate(R.layout.layout_inflater, null, false);
        setContentView(inflate);

        AppCompatDelegate delegate = getDelegate();
    }
}
