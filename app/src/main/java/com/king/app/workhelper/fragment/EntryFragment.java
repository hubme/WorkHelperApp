package com.king.app.workhelper.fragment;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;

import butterknife.OnClick;

/**
 * 测试入口Fragment
 * Created by HuoGuangxu on 2016/11/10.
 */

public class EntryFragment extends AppBaseFragment {
    public final String TAG = EntryFragment.class.getSimpleName();

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_entry;
    }

    @OnClick(R.id.tv_permission)
    public void permissionClick() {
        clickedOn(new PermissionFragment());
    }

    private void clickedOn(@NonNull Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(TAG)
                .replace(R.id.layout_container, fragment, TAG)
                .commit();
    }
}
