package com.king.applib.util;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * View 相关工具类.
 *
 * @author VanceKing
 * @since 2018/1/17.
 */
public class ViewUtil {
    private ViewUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    /**
     * 如果 TextView 为 null 或 TextView 的内容为“”/null,则返回 defaultText.
     */
    public static String getText(TextView textView, String defaultText) {
        if (textView == null) {
            return defaultText;
        }
        String string = textView.getText().toString();
        if (string.trim().isEmpty()) {
            return defaultText;
        }
        return string;
    }

    /**
     * 如果 TextView 为 null,或 TextView 的内容为“”/null,则返回空字符串 "".
     */
    public static String getText(TextView textView) {
        return getText(textView, "");
    }

    /**
     * View 设置背景的兼容方法
     */
    public static void setViewBackground(View view, Drawable drawable) {
        if (view == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    /**
     * 销毁WebView，避免内存泄露.
     */
    public static void destroyWebView(WebView webView) {
        destroyWebView(webView, false);
    }

    /**
     * 销毁WebView，避免内存泄露.<br/>
     *
     * @param clearCache 是否清空缓存.注意：所有WebView公用缓存，应用最后显示的WebView才可以使用.
     * @see <a href="https://stackoverflow.com/questions/17418503/destroy-webview-in-android">stackoverflow</a>
     */
    public static void destroyWebView(WebView webView, boolean clearCache) {
        if (webView != null) {
            final ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }

            webView.clearHistory();
            // NOTE: clears RAM cache, if you pass true, it will also clear the disk cache.
            // Probably not a great idea to pass true if you have other WebViews still alive.
            webView.clearCache(clearCache);
            // Loading a blank page is optional, but will ensure that the WebView isn't doing anything when you destroy it.
            //webView.loadUrl("about:blank");

            webView.onPause();
            webView.removeAllViews();
            webView.destroyDrawingCache();

            webView.destroy();
            webView = null;
        }
    }

    /**
     * hook View 的点击事件.
     */
    @SuppressLint({"DiscouragedPrivateApi", "PrivateApi"})
    public static void hook(final View view, ProxyOnClickListener proxyOnClickListener) {
        if (proxyOnClickListener == null) {
            return;
        }
        try {
            // 反射执行View类的getListenerInfo()方法，拿到v的mListenerInfo对象，这个对象就是点击事件的持有者
            Method method = View.class.getDeclaredMethod("getListenerInfo");
            method.setAccessible(true);//由于getListenerInfo()方法并不是public的，所以要加这个代码来保证访问权限
            Object mListenerInfo = method.invoke(view);//这里拿到的就是mListenerInfo对象，也就是点击事件的持有者

            // 要从这里面拿到当前的点击事件对象
            Class<?> listenerInfoClz = Class.forName("android.view.View$ListenerInfo");// 这是内部类的表示方法
            Field field = listenerInfoClz.getDeclaredField("mOnClickListener");
            final View.OnClickListener onClickListenerInstance = (View.OnClickListener) field.get(mListenerInfo);//取得真实的mOnClickListener对象

            // 使用动态代理的方式
            /*Object proxyOnClickListener = Proxy.newProxyInstance(view.getClass().getClassLoader(),
                    new Class[]{View.OnClickListener.class}, new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            Log.i("aaa", "点击事件被hook到了");//加入自己的逻辑
                            return method.invoke(onClickListenerInstance, args);//执行被代理的对象的逻辑
                        }
                    });*/
            // 3. 用我们自己的点击事件代理类，设置到"持有者"中
            field.set(mListenerInfo, proxyOnClickListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static abstract class ProxyOnClickListener implements View.OnClickListener {
        private final View.OnClickListener mOriginalClickListener;

        public ProxyOnClickListener(View.OnClickListener originalClickListener) {
            this.mOriginalClickListener = originalClickListener;
        }

        @Override
        public void onClick(View v) {
            if (mOriginalClickListener != null) {
                mOriginalClickListener.onClick(v);
                onProxyClick(v);
            }
        }

        public abstract void onProxyClick(View v);
    }

}
