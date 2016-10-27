package com.king.app.workhelper.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.support.v7.app.AlertDialog;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.applib.log.Logger;

import butterknife.BindView;

/**
 * Created by HuoGuangxu on 2016/10/25.
 */

public class WebViewLocateActivity extends AppBaseActivity {
    @BindView(R.id.web_view)
    public WebView webView;

    @Override public int getContentLayout() {
        return R.layout.activity_webview;
    }

    @Override protected void initData() {
        super.initData();
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setGeolocationDatabasePath(getFilesDir().getPath());
        Logger.i(getFilesDir().getPath());

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsHidePrompt() {
                super.onGeolocationPermissionsHidePrompt();
                Logger.i("onGeolocationPermissionsHidePrompt");
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                new AlertDialog.Builder(WebViewLocateActivity.this)
                        .setMessage("Allow to access location information?")
                        .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) {
                                callback.invoke(origin, true, true);
                            }
                        })
                        .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) {
                                callback.invoke(origin, false, false);
                            }
                        }).show();
                Logger.i("onGeolocationPermissionsShowPrompt");
            }
        });
        webView.loadUrl("file:///android_asset/geolocation.html");

        testGeolocationOK();
    }

    private void testGeolocationOK() {
        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean gpsProviderOK = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkProviderOK = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean geolocationOK = gpsProviderOK && networkProviderOK;
        Logger.i("gpsProviderOK = " + gpsProviderOK + "; networkProviderOK = " + networkProviderOK + "; geoLocationOK=" + geolocationOK);
    }
}
