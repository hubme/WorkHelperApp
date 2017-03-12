package com.king.applib.util;

import android.accounts.NetworkErrorException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Http请求工具类
 * @author VanceKing
 * @since 2017/3/12 0012.
 */

public class HttpUtil {
    private HttpUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    public static String post(String url, String content) {
        if (StringUtil.isAnyEmpty(url, content)) {
            return null;
        }
        HttpURLConnection conn = null;
        try {
            // 创建一个URL对象
            URL mURL = new URL(url);
            // 调用URL的openConnection()方法,获取HttpURLConnection对象
            conn = (HttpURLConnection) mURL.openConnection();

            conn.setRequestMethod("POST");// 设置请求方法为post
            conn.setReadTimeout(5000);// 设置读取超时为5秒
            conn.setConnectTimeout(10000);// 设置连接网络超时为10秒
            conn.setDoOutput(true);// 设置此方法,允许向服务器输出内容

            // 获得一个输出流,向服务器写数据,默认情况下,系统不允许向服务器输出内容
            OutputStream out = conn.getOutputStream();// 获得一个输出流,向服务器写数据
            out.write(content.getBytes());
            out.flush();
            out.close();

            int responseCode = conn.getResponseCode();// 调用此方法就不必再使用conn.connect()方法
            if (responseCode == 200) {
                InputStream is = conn.getInputStream();
                return getStringFromInputStream(is);
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();// 关闭连接
            }
        }
        return null;
    }

    public static String get(String url) {
        if (StringUtil.isNullOrEmpty(url)) {
            return null;
        }
        HttpURLConnection conn = null;
        try {
            // 利用string url构建URL对象
            URL mURL = new URL(url);
            conn = (HttpURLConnection) mURL.openConnection();

            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {

                InputStream is = conn.getInputStream();
                return getStringFromInputStream(is);
            } else {
                throw new NetworkErrorException("response status is " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return null;
    }

    private static String getStringFromInputStream(InputStream is) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[512];
        int len = -1;
        try {
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            return os.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.close(is);
            IOUtil.close(os);
        }
        return null;
    }
}
