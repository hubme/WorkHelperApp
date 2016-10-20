package com.king.applib.util;

import java.io.File;

/**
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

}
