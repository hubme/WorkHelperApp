package com.king.applib.builder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 用于构建Intent和启动Activity.
 * see also:{@link android.support.v4.app.ShareCompat.IntentBuilder IntentBuilder}
 *
 * @author VanceKing
 * @since 2017/3/8.
 */
public class IntentBuilder {

    private Intent intent;
    private Context mContext;

    public <T extends Activity> IntentBuilder(@NonNull Context context, @NonNull Class<T> clazz) {
        mContext = context;
        intent = new Intent(mContext, clazz);
    }

    public <T extends Serializable> IntentBuilder put(@NonNull String key, T value) {
        intent.putExtra(key, value);
        return this;
    }

    public IntentBuilder put(@NonNull String key, Parcelable value) {
        intent.putExtra(key, value);
        return this;
    }

    public IntentBuilder put(@NonNull String key, Parcelable[] value) {
        intent.putExtra(key, value);
        return this;
    }

    public <T extends Parcelable> IntentBuilder put(@NonNull String key, ArrayList<T> value) {
        intent.putExtra(key, value);
        return this;
    }

    public IntentBuilder put(Bundle bundle) {
        intent.putExtras(bundle);
        return this;
    }

    public IntentBuilder remove(@NonNull String key) {
        intent.removeExtra(key);
        return this;
    }

    public IntentBuilder setFlags(int flags) {
        intent.setFlags(flags);
        return this;
    }

    public IntentBuilder addFlags(int flags) {
        intent.addFlags(flags);
        return this;
    }

    public Intent build() {
        return intent;
    }

    public void start() {
        mContext.startActivity(intent);
    }

    public void startForResult(@NonNull Activity activity, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void startForResult(@NonNull Activity activity, int requestCode, @Nullable Bundle options) {
        activity.startActivityForResult(intent, requestCode, options);
    }
}
