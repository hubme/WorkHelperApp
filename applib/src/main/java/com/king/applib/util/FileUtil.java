package com.king.applib.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 文件工具类。读写sdcard记得申请权限.
 * Created by HuoGuangxu on 2016/10/19.
 */

public class FileUtil {
    /**
     * 获取全路径中的文件拓展名.
     * @param file 文件
     * @return 文件拓展名
     */
    public static String getFileExtension(File file) {
        if (file == null)
            return null;
        return getFileExtension(file.getAbsolutePath());
    }

    /**
     * 获取全路径中的文件拓展名.eg: jpg、apk、txt.
     * @param filePath 文件路径
     * @return 文件拓展名
     */
    public static String getFileExtension(String filePath) {
        if (StringUtil.isNullOrEmpty(filePath)) {
            return "";
        }
        int lastComma = filePath.lastIndexOf('.');
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastComma == -1 || lastSep >= lastComma) {
            return "";
        }
        return filePath.substring(lastComma + 1);
    }

    /**
     * 根据文件路径创建文件.如果文件已经存在，则删除.
     * @param path 文件全路径
     * @return 文件对象
     */
    public static File createFile(String path) {
        if (!ExtendUtil.isSDCardAvailable() || StringUtil.isNullOrEmpty(path)) {
            return null;
        }
        File file = new File(path);
        file.deleteOnExit();
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            if (!parentFile.mkdirs()) {
                return null;
            }
        }
        return file;
    }

    /**
     * 根据文件目录和文件名称文件文件,如果文件已经存在，则删除。
     * @param dir 文件目录
     * @param name 文件名称
     * @return 文件对象
     */
    public static File createFile(String dir, String name) {
        if (StringUtil.isNullOrEmpty(dir) || StringUtil.isNullOrEmpty(name)) {
            return null;
        }
        return createFile(dir + File.separator + name);
    }

    /**
     * 创建文件.如果指定文件存在，则删除。
     * @param dirFile 文件夹对象
     * @param name 文件名称
     * @return 文件对象
     */
    public static File createFile(File dirFile, String name) {
        if (dirFile == null || !dirFile.isDirectory() || StringUtil.isNullOrEmpty(name)) {
            return null;
        }
        return createFile(dirFile.getAbsolutePath(), name);
    }


    /**
     * 创建文件目录
     * @param dir 文件目录
     * @return 文件对象
     */
    public static File createDir(String dir) {
        if (StringUtil.isNullOrEmpty(dir)) {
            return null;
        }
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            if (!dirFile.mkdirs()) {
                return null;
            }
        }
        return dirFile;
    }

    /**
     * 保存字符串到文件
     * @param file 文件
     * @param content 保存的字符串
     */
    public static void writeToFile(File file, String content) {
        if (file == null || StringUtil.isNullOrEmpty(content)) {
            return;
        }
        BufferedWriter bufferedWriter = null;
        try {
            FileWriter writer = new FileWriter(file);
            bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(content);
            bufferedWriter.flush();
        } catch (IOException e) {
            //
        } finally {
            IOUtil.close(bufferedWriter);
        }
    }
}
