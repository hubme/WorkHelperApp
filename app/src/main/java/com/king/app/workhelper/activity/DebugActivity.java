package com.king.app.workhelper.activity;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;

import com.king.app.workhelper.R;
import com.king.applib.log.Logger;

/**
 * Debug设置
 *
 * @author huoguangxu
 * @since 2017/4/11.
 */

public class DebugActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener{

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs_debug_settings);

        initPrefs();
    }

    private void initPrefs() {
        Preference key_domain_release = findPreference("key_domain_release");
        Preference key_domain_debug = findPreference("key_domain_debug");
        Preference key_domain_custom = findPreference("key_domain_custom");

        key_domain_release.setOnPreferenceChangeListener(this);
        key_domain_debug.setOnPreferenceChangeListener(this);
        key_domain_custom.setOnPreferenceChangeListener(this);
    }

    @Override public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean checked = (boolean) newValue;
        switch (preference.getKey()) {
            case "key_domain_release":
                Logger.i("key_domain_release: "+checked);
                break;
            case "key_domain_debug":
                Logger.i("key_domain_debug: "+checked);

                break;
            case "key_domain_custom":
                Logger.i("key_domain_custom: "+checked);

                break;
        }
        return false;
    }

    @Override public void finish() {
        super.finish();
        //返回上个Activity。
        //第一个参数作(enterAnim)用于上一个Activity，第二个参数(exitAnim)作用于当前Activity
        overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.alpha_disappear);
    }
}
