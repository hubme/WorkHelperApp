package com.king.app.workhelper.dialog;

import android.view.View;
import android.widget.Toast;

import com.king.app.workhelper.R;
import com.king.applib.dialog.BaseBottomDialog;

public class ShareBottomDialog extends BaseBottomDialog {

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_share;
    }

    @Override
    public void bindView(View v) {
        View weChatView = v.findViewById(R.id.tv_we_chat);
        weChatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "哈哈哈", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public float getDimAmount() {
        return 0.6f;
    }
}
