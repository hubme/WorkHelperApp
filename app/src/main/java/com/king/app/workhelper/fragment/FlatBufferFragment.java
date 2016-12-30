package com.king.app.workhelper.fragment;

import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.model.flatmodel.PeopleList;
import com.king.app.workhelper.model.jsonmodel.PeopleListJson;
import com.king.applib.log.Logger;
import com.king.applib.util.ExtendUtil;
import com.king.applib.util.JsonUtil;

import java.nio.ByteBuffer;

import butterknife.BindView;
import butterknife.OnClick;

import static com.antfortune.freeline.FreelineCore.getApplication;

/**
 * FlatBuffer和Json对比
 * Created by HuoGuangxu on 2016/12/22.
 */

public class FlatBufferFragment extends AppBaseFragment {
    @BindView(R.id.textViewFlat)
    public TextView textViewFlat;
    @BindView(R.id.textViewJson)
    public TextView textViewJson;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_flatbuffer;
    }

    @OnClick(R.id.btn_flat_buffer)
    public void loadFromFlatBuffer() {
        byte[] buffer = ExtendUtil.readRawResource(getApplication(), R.raw.sample_flatbuffer);//由于raw文件太大，用空文件代替
        long startTime = System.currentTimeMillis();
        ByteBuffer bb = ByteBuffer.wrap(buffer);
        PeopleList peopleList = PeopleList.getRootAsPeopleList(bb);
        long timeTaken = System.currentTimeMillis() - startTime;
        String logText = "FlatBuffer : " + timeTaken + "ms";
        textViewFlat.setText(logText);
        Logger.i("loadFromFlatBuffer " + logText);
    }

    @OnClick(R.id.btn_json)
    public void loadFromJson() {
        String jsonText = new String(ExtendUtil.readRawResource(getApplication(), R.raw.sample_json));
        long startTime = System.currentTimeMillis();
//        PeopleListJson peopleList = new Gson().fromJson(jsonText, PeopleListJson.class);
        PeopleListJson peopleList = JsonUtil.decode(jsonText, PeopleListJson.class);
        long timeTaken = System.currentTimeMillis() - startTime;
        String logText = "Json : " + timeTaken + "ms";
        textViewJson.setText(logText);
        Logger.i("loadFromJson " + logText);
    }
}
