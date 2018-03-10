package com.king.applib.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.king.applib.R;

/**
 * 主module可以通过Intent调用子module的Activity，就像在自己module中一样。<br/>
 * 子module的AndroidManifest.xml会merge到主module中。
 *
 * @author VanceKing
 * @since 2017/3/29.
 */
public class LibActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib);
    }
}
