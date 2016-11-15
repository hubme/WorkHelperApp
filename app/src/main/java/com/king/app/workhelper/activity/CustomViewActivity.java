package com.king.app.workhelper.activity;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.ui.customview.LayerImageView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 自定义View
 * Created by HuoGuangxu on 2016/10/17.
 */

public class CustomViewActivity extends AppBaseActivity {

    @BindView(R.id.layer_image)
    public LayerImageView mLayerImageView;

    @Override
    public int getContentLayout() {
        return R.layout.activity_custom;
    }

    @Override protected void initData() {
        super.initData();
        mLayerImageView.setImageResource(R.mipmap.calc_guide_head);
    }

    @OnClick(R.id.layer_image)
    public void clickLayer() {
        mLayerImageView.setImageResource(R.drawable.little_boy_01);
        mLayerImageView.showLayer();
        mLayerImageView.setLayerColor(R.color.light_orange_ff8800);
        mLayerImageView.setLayerHint("了反思");
        mLayerImageView.setHintSize(R.dimen.ts_huge);
        mLayerImageView.setHintColor(R.color.colorPrimaryDark);
    }
}
