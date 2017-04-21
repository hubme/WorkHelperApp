package com.king.app.workhelper.fragment;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.king.app.workhelper.R;

/**
 * OkHttp log interceptor 设置
 *
 * @author huoguangxu
 * @since 2017/4/21.
 */

public class HttpLogSettingFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
    private Preference mCloseLogPref;
    private Preference mRequestHeaderPref;
    private Preference mResponseHeaderPref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs_debug_settings);

        initPrefs();
    }

    private void initPrefs() {
        mCloseLogPref = findPreference("log_none");
        mRequestHeaderPref =  findPreference("log_request_headers");
        mResponseHeaderPref =  findPreference("log_response_headers");

//        mCloseLogPref.setOnPreferenceChangeListener(this);
//        mRequestHeaderPref.setOnPreferenceChangeListener(this);
//        mResponseHeaderPref.setOnPreferenceChangeListener(this);
        
    }

    @Override public boolean onPreferenceChange(Preference preference, Object newValue) {

        return false;
    }
}
