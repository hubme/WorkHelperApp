package com.example.dynamicproxy;

import java.io.FileOutputStream;
import java.io.IOException;

import sun.misc.ProxyGenerator;

/**
 * @author VanceKing
 * @since 2017/6/13.
 */

public class ProxyUtil {
    /**
     * Save proxy class to path.<br/>
     * https://github.com/android-cn/android-open-project-demo/blob/master/java-dynamic-proxy/src/com/codekk/java/test/dynamicproxy/util/ProxyUtils.java
     *
     * @param path           path to save proxy class
     * @param proxyClassName name of proxy class
     * @param interfaces     interfaces of proxy class
     */
    public static boolean saveProxyClass(String path, String proxyClassName, Class[] interfaces) {
        if (proxyClassName == null || path == null) {
            return false;
        }

        // get byte of proxy class 
        byte[] classFile = ProxyGenerator.generateProxyClass(proxyClassName, interfaces);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            out.write(classFile);
            out.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
