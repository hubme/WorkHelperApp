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
    @Override
    protected int getContentLayout() {
        return R.layout.fragment_entry;
    }

    @OnClick(R.id.tv_permission)
    public void permissionClick() {
        clickedOn(new PermissionFragment());
    }

    private void clickedOn(@NonNull Fragment fragment) {
        final String tag = fragment.getClass().toString();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(tag)
                .replace(R.id.layout_content, fragment, tag)
                .commit();
    }
}
