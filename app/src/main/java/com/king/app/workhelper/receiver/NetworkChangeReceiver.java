package com.king.app.workhelper.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.king.applib.log.Logger;
import com.king.applib.util.NetworkUtil;

/**
 * 网络变化的广播.
 * Created by HuoGuangxu on 2016/12/16.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onReceive(final Context context, final Intent intent) {
        /*ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            manager.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {
                @Override public void onAvailable(Network network) {
                    super.onAvailable(network);
                }
            });
        }*/
        //注意：运行在主线程，不要做耗时操作.
        if (NetworkUtil.isNetworkAvailable(context)) {
            Logger.i("网络类型： " + NetworkUtil.getNetWorkType(context));
        }

    }
}