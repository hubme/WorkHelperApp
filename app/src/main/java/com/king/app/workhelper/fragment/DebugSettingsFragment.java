package com.king.app.workhelper.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author huoguangxu
 * @since 2017/4/11.
 */

public class DebugSettingsFragment extends PreferenceFragment{
    @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        findPreference("sdf");
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
