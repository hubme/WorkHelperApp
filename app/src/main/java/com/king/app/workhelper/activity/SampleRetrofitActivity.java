package com.king.app.workhelper.activity;

import android.widget.Button;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.applib.log.Logger;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by VanceKing on 2016/10/10 0010.
 * http://mp.weixin.qq.com/s?__biz=MzI5NjQxNDE3Ng==&mid=2247483665&idx=1&sn=c87127b2617e11fe52e36d7144a613ee&mpshare=1&scene=1&srcid=1010ClfxSMqG3vMfgRd8mOcD#rd
 */

public class SampleRetrofitActivity extends AppBaseActivity {
    @BindView(R.id.btn_retrofit)
    Button mRetrofitBtn;

    @Override
    public int getContentLayout() {
        return R.layout.activity_sample_retrofit;
    }

    @OnClick(R.id.btn_retrofit)
    public void retrofitRequest() {
        Logger.i("retrofix request");
    }

    private void getMovie() {

    }
}
