package com.king.app.workhelper.fragment;

import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author huoguangxu
 * @since 2017/3/29.
 */

public class CombineLatestSample extends AppBaseFragment {
    @BindView(R.id.et_phone_number) EditText mPhoneNumberEt;
    @BindView(R.id.et_pwd) EditText mPwdEt;
    @BindView(R.id.btn_login) Button mLoginBtn;
    
    private Disposable mSubscribe;

    @Override protected int getContentLayout() {
        return R.layout.fragment_combine_latest_sample;
    }

    @Override protected void initData() {
        super.initData();

        Observable<CharSequence> numberObservable = RxTextView.textChanges(mPhoneNumberEt).skip(1);
        Observable<CharSequence> pwdObservable = RxTextView.textChanges(mPwdEt).skip(1);
        mSubscribe = Observable.combineLatest(numberObservable, pwdObservable, (number, pwd) -> {
            boolean a = (number.toString().trim().length() == 11);
            final int length = pwd.toString().trim().length();
            boolean b = (length >= 6 && length <= 11);
            return a && b;
        }).subscribe(aBoolean -> mLoginBtn.setEnabled(aBoolean));
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mSubscribe.dispose();
    }
}
