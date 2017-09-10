package com.king.app.workhelper.common;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * TextWatcher抽象实现类，避免每次都要实现三个接口。
 * see also:{@link android.animation.AnimatorListenerAdapter AnimatorListenerAdapter}
 * Created by WorkHelperApp on 2016/11/1.
 */

public abstract class TextWatcherAdapter implements TextWatcher {
    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override public void afterTextChanged(Editable s) {

    }
}
