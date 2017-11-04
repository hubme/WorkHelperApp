package com.king.app.workhelper.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.applib.base.dialog.BaseCenterDialog;
import com.king.applib.util.StringUtil;

/**
 * @author VanceKing
 * @since 2017/7/12.
 */

public class SimpleDialog extends BaseCenterDialog {
    private static final String KEY_DIALOG_TITLE = "KEY_DIALOG_TITLE";
    private static final String KEY_DIALOG_MESSAGE = "KEY_DIALOG_MESSAGE";
    private static final String KEY_DIALOG_NEGATIVE_TEXT = "KEY_DIALOG_NEGATIVE_TEXT";
    private String title;
    private String message;
    private String negativeText;

    @Override public int getLayoutRes() {
        return R.layout.layout_simple_dialog_fragment;
    }

    @Override public void bindView(View v) {
        TextView mTitleTv = (TextView) v.findViewById(R.id.tv_dialog_title);
        TextView mMessageTv = (TextView) v.findViewById(R.id.tv_dialog_message);

        if (StringUtil.isNotNullOrEmpty(title)) {
            mTitleTv.setVisibility(View.VISIBLE);
            mTitleTv.setText(title);
        } else {
            mTitleTv.setVisibility(View.GONE);
        }

        if (StringUtil.isNotNullOrEmpty(message)) {
            mMessageTv.setText(message);
        }
    }

    @Override public boolean getCancelOutside() {
        return false;
    }

    @Override protected void getArguments(Bundle bundle) {
        super.getArguments(bundle);
        title = bundle.getString(KEY_DIALOG_TITLE);
        message = bundle.getString(KEY_DIALOG_MESSAGE);
        negativeText = bundle.getString(KEY_DIALOG_NEGATIVE_TEXT);
    }
    
    public static class Builder{
        private String title;
        private String message;
        private String negativeText;
        
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setNegativeButton(String text, final DialogInterface.OnClickListener listener) {
            negativeText = text;
            return this;
        }

        private Bundle buildArguments() {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_DIALOG_TITLE, title);
            bundle.putString(KEY_DIALOG_MESSAGE, message);
            bundle.putString(KEY_DIALOG_NEGATIVE_TEXT, negativeText);
            return bundle;
        }

        public SimpleDialog build() {
            SimpleDialog dialog = new SimpleDialog();
            dialog.setArguments(buildArguments());
            return dialog;
        }
    }
}
