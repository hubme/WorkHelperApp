package com.king.app.workhelper.fragment;

import android.widget.Button;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.log.Logger;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Retrofit.
 * http://mp.weixin.qq.com/s?__biz=MzI5NjQxNDE3Ng==&mid=2247483665&idx=1&sn=c87127b2617e11fe52e36d7144a613ee&mpshare=1&scene=1&srcid=1010ClfxSMqG3vMfgRd8mOcD#rd
 * Created by VanceKing on 2016/11/26 0026.
 */

public class RetrofitSampleFragment extends AppBaseFragment {
    @BindView(R.id.btn_retrofit)
    Button mRetrofitBtn;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_sample_retrofit;
    }

    @OnClick(R.id.btn_retrofit)
    public void retrofitRequest() {
        Logger.i("retrofix request");
        getMovie();
    }

    private void getMovie() {

    }
}
