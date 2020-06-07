/**
 *
 * native结果数据返回格式:
 * var resultData = {
    code: 0,//0成功，1失败
    msg: 'success',//提示消息
    data: {}//数据,无数据时为空对象，不是 null
};
 协定协议:jsbridge://app.king.com:1234/showToast?params={"message":"hello"};
 1234: 请求码，区分同一方法；
 showToast: Native 方法
 params: 请求参数，json字符串
 */
(function () {
    var doc = document;
    var win = window;
    var ua = win.navigator.userAgent;
    var JS_BRIDGE_PROTOCOL_SCHEMA = "jsbridge";
    var JS_BRIDGE_PROTOCOL_HOST = "app.king.com";
    var increase = 1;
    var JsBridge = win.JsBridge || (win.JsBridge = {});

    var ExposeMethod = {
        callMethod: function (method, requestCode, param, callback) {
            if (typeof callback !== 'function') {
                callback = null;
            }
            PrivateMethod.registerCallback(requestCode, callback);
            PrivateMethod.callNativeMethod(method, requestCode, param);
        },

        onComplete: function (requestCode, result) {
            PrivateMethod.onNativeComplete(requestCode, result);
        }

    };

    var PrivateMethod = {
        callbacks: {},
        registerCallback: function (requestCode, callback) {
            if (callback) {
                PrivateMethod.callbacks[requestCode] = callback;
            }
        },
        getCallback: function (requestCode) {
            var call = {};
            if (PrivateMethod.callbacks[requestCode]) {
                call.callback = PrivateMethod.callbacks[requestCode];
            } else {
                call.callback = null;
            }
            return call;
        },
        unRegisterCallback: function (requestCode) {
            if (PrivateMethod.callbacks[requestCode]) {
                delete PrivateMethod.callbacks[requestCode];
            }
        },
        onNativeComplete: function (requestCode, result) {
            var resultJson = PrivateMethod.str2Json(result);
            var callback = PrivateMethod.getCallback(requestCode).callback;
            PrivateMethod.unRegisterCallback(requestCode);
            if (callback) {
                callback && callback(resultJson);
            }
        },
        str2Json: function (str) {
            if (str && typeof str === 'string') {
                try {
                    return JSON.parse(str);
                } catch (e) {
                    return {
                            code: 1,
                            msg: 'params parse error!'
                    };
                }
            } else {
                return str || {};
            }
        },
        json2Str: function (param) {
            if (param && typeof param === 'object') {
                return JSON.stringify(param);
            } else {
                return param || '';
            }
        },
        callNativeMethod: function (method, requestCode, param) {
            if (PrivateMethod.isAndroid()) {
                var jsonStr = PrivateMethod.json2Str(param);
                var uri = JS_BRIDGE_PROTOCOL_SCHEMA + "://" + JS_BRIDGE_PROTOCOL_HOST + ":" + requestCode + "/" + method + "?params=" + jsonStr;
                win.prompt(uri, "");
            }
        },
        isAndroid: function () {
            var tmp = ua.toLowerCase();
            var android = tmp.indexOf("android") > -1;
            return !!android;
        },
        isIos: function () {
            var tmp = ua.toLowerCase();
            var ios = tmp.indexOf("iphone") > -1;
            return !!ios;
        }
    };
    for (var index in ExposeMethod) {
        if (ExposeMethod.hasOwnProperty(index)) {
            if (!Object.prototype.hasOwnProperty.call(JsBridge, index)) {
                JsBridge[index] = ExposeMethod[index];
            }
        }
    }
})();



