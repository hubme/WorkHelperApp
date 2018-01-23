package com.king.app.workhelper.activity;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.ui.customview.PaymentChart;
import com.king.applib.util.JsonUtil;

import java.util.List;

import butterknife.BindView;

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

public class TestActivity extends AppBaseActivity {
    @BindView(R.id.chart_payment) PaymentChart mPaymentChart;

    @Override protected int getContentLayout() {
        return R.layout.activity_test;
    }

    @Override protected void initContentView() {
        super.initContentView();
        mPaymentChart.setData(fakeData());

    }

    private List<PaymentChart.PaymentYearModel> fakeData() {

//        String DATA = "[{\"year\":2016,\"data\":[{\"month\":7,\"pay\":235.0,\"count\":1},{\"month\":8,\"pay\":535.0,\"count\":1},{\"month\":9,\"pay\":235.0,\"count\":0},{\"month\":10,\"pay\":235.0,\"count\":0},{\"month\":11,\"pay\":235.0,\"count\":0},{\"month\":12,\"pay\":565.0,\"count\":3}]},{\"year\":2017,\"data\":[{\"month\":1,\"pay\":235.0,\"count\":1},{\"month\":2,\"pay\":345.0,\"count\":1},{\"month\":3,\"pay\":345.0,\"count\":0},{\"month\":4,\"pay\":345.0,\"count\":1},{\"month\":5,\"pay\":345.0,\"count\":1},{\"month\":6,\"pay\":345.0,\"count\":1},{\"month\":7,\"pay\":345.0,\"count\":1},{\"month\":8,\"pay\":345.0,\"count\":1}]}]";
//        String DATA = "[{\"year\":2016,\"data\":[{\"month\":7,\"pay\":235.0,\"count\":1},{\"month\":8,\"pay\":235.0,\"count\":1},{\"month\":9,\"pay\":235.0,\"count\":0},{\"month\":10,\"pay\":235.0,\"count\":0},{\"month\":11,\"pay\":235.0,\"count\":0},{\"month\":12,\"pay\":565.0,\"count\":3}]}]";
        String DATA = "[{\"data\":[{\"month\":8,\"count\":1,\"pay\":2290},{\"month\":9,\"count\":1,\"pay\":2290},{\"month\":10,\"count\":1,\"pay\":2290},{\"month\":11,\"count\":1,\"pay\":2290},{\"month\":12,\"count\":1,\"pay\":2290}],\"year\":\"2015\"},{\"data\":[{\"month\":1,\"count\":1,\"pay\":2290},{\"month\":2,\"count\":1,\"pay\":2290},{\"month\":3,\"count\":1,\"pay\":2290},{\"month\":4,\"count\":1,\"pay\":2290},{\"month\":5,\"count\":1,\"pay\":2290},{\"month\":6,\"count\":1,\"pay\":2290},{\"month\":7,\"count\":1,\"pay\":2290},{\"month\":8,\"count\":1,\"pay\":2494},{\"month\":9,\"count\":1,\"pay\":2494},{\"month\":10,\"count\":1,\"pay\":2494},{\"month\":11,\"count\":1,\"pay\":2494},{\"month\":12,\"count\":1,\"pay\":2494}],\"year\":\"2016\"},{\"data\":[{\"month\":1,\"count\":1,\"pay\":2494},{\"month\":2,\"count\":1,\"pay\":2494},{\"month\":3,\"count\":1,\"pay\":2494},{\"month\":4,\"count\":1,\"pay\":2494},{\"month\":5,\"count\":1,\"pay\":2494},{\"month\":6,\"count\":1,\"pay\":2494},{\"month\":7,\"count\":1,\"pay\":2494},{\"month\":8,\"count\":1,\"pay\":2732},{\"month\":9,\"count\":2,\"pay\":5464},{\"month\":10,\"count\":0,\"pay\":0},{\"month\":11,\"count\":1,\"pay\":2732},{\"month\":12,\"count\":1,\"pay\":2732}],\"year\":\"2017\"}]";
        List<PaymentChart.PaymentYearModel> models = JsonUtil.decodeToList(DATA, PaymentChart.PaymentYearModel.class);

        return models;
    }
}
