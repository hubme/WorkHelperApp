package com.example.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author VanceKing
 * @since 2018/11/5.
 */
public class PathClassLoader extends ClassLoader {
    private String classPath;

    public PathClassLoader(String classPath) {
        this.classPath = classPath;
    }

    @Override protected Class<?> findClass(String s) throws ClassNotFoundException {
        byte[] classData = getData(s);
        if (classData == null) {
            throw new ClassNotFoundException();
        } else {
            return defineClass(s, classData, 0, classData.length);
        }
    }

    private byte[] getData(String className) {
        String path = classPath + File.separatorChar + className.replace('.', File.separatorChar) + ".class";
        try {
            InputStream is = new FileInputStream(path);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int num;
            while ((num = is.read(buffer)) != -1) {
                stream.write(buffer, 0, num);
            }
            return stream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ClassLoader pcl = new PathClassLoader("F:\\Android\\Github\\WorkHelperApp\\javalib\\build\\classes\\java\\main");
        Class c = pcl.loadClass("com.example.classloader.TestClassLoad");//注意要包括包名
        System.out.println(c.newInstance());//打印"类加载成功"
    }
}