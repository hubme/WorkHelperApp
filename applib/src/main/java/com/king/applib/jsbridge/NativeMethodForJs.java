package com.king.applib.jsbridge;

import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.Toast;

import com.king.applib.util.JsonUtil;

class NativeMethodForJs {
    private static final int CODE_SUCCESS = 0;
    private static final int CODE_FAILURE = 1;
    private static final int CODE_NOT_FOUND = 404;

    static void nativeMethodNotFound(WebView webView, String method, String requestCode,
                                     String params) {
        JsRequest<Void> request = JsonUtil.decode(params, JsRequest.class);
        if (checkParams(request)) {
            return;
        }
        JsResult<Void> result = new JsResult<>();
        result.setCode(CODE_NOT_FOUND);
        result.setMessage(method + " native method not found!");
        JsCallback.call(webView, requestCode, JsonUtil.encode(result));
    }

    public static void showToast(final WebView webView, String requestCode, final String params) {
        Toast.makeText(webView.getContext(), params, Toast.LENGTH_SHORT).show();
        JsResult<Message> result = new JsResult<>();
        result.setCode(CODE_SUCCESS);
        result.setMessage("success");
        result.setRequestCode(parseInteger(requestCode));
        result.setData(new Message("成功了"));
        JsCallback.call(webView, requestCode, JsonUtil.encode(result));
    }

    private static class Message {
        private String message;

        public Message(String message) {
            this.message = message;
        }
    }

    private static boolean checkParams(JsRequest request) {
        return request == null || TextUtils.isEmpty(request.getFunction());
    }

    private static int parseInteger(String integer) {
        try {
            return Integer.parseInt(integer);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
