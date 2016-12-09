package com.king.applib.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

/**
 * 文件工具类。读写sdcard记得申请权限.
 * Created by HuoGuangxu on 2016/10/19.
 */

public class FileUtil {
    private FileUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    public static final long KB_IN_BYTES = 1024;
    public static final long MB_IN_BYTES = KB_IN_BYTES * 1024;
    public static final long GB_IN_BYTES = MB_IN_BYTES * 1024;
    public static final long TB_IN_BYTES = GB_IN_BYTES * 1024;
    public static final long PB_IN_BYTES = TB_IN_BYTES * 1024;

    /**
     * 根据文件路径获取文件.
     */
    public static File getFileByPath(String filePath) {
        return StringUtil.isNullOrEmpty(filePath) ? null : new File(filePath);
    }

    /*public static File getFileByPath(Context context, @DrawableRes int resId) {
        Bitmap bitmap = ImageUtil.getBitmap(context, resId);
        ImageUtil.
        return StringUtil.isNullOrEmpty(filePath) ? null : new File(filePath);
    }*/

    /**
     * 根据文件路径判断文件是否存在
     */
    public static boolean isFileExists(String filePath) {
        return isFileExists(getFileByPath(filePath));
    }

    /**
     * 判断文件是否存在
     */
    public static boolean isFileExists(File file) {
        return file != null && file.exists();
    }

    /**
     * 获取全路径中的文件拓展名.
     * @param file 文件
     * @return 文件拓展名
     */
    public static String getFileExtension(File file) {
        return file == null ? "" : getFileExtension(file.getAbsolutePath());
    }

    /**
     * 获取文件拓展名.eg: jpg、apk、txt.
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
     * 根据文件路径创建全新的文件.如果文件已经存在，则删除.
     * @param path 文件全路径
     * @return 文件对象
     */
    public static File createFile(String path) {
        if (!ExtendUtil.isSDCardAvailable() || StringUtil.isNullOrEmpty(path)) {
            return null;
        }
        File file = new File(path);
        if (isFileExists(file)) {
            if (!file.delete()) {
                return null;
            }
        }
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            if (!parentFile.mkdirs()) {
                return null;
            }
        }
        return file;
    }

    /**
     * 根据文件目录和文件名称创建文件,如果文件已经存在，则删除。
     * @param dir  文件目录
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
     * @param name    文件名称
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
     * @param file    文件
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

    /**
     * 返回byte的数据大小对应的格式化大小文字
     * @param size 文件大小
     * @return 格式化后的文字
     */
    public static String formatFileSize(long size) {
        if (size < KB_IN_BYTES) {
            return String.format(Locale.getDefault(), "%.2d bytes", size);
        } else if (size < MB_IN_BYTES) {
            return String.format(Locale.getDefault(), "%.2f KB", size / KB_IN_BYTES);
        } else if (size < GB_IN_BYTES) {
            return String.format(Locale.getDefault(), "%.2d MB", size / MB_IN_BYTES);
        } else if (size < TB_IN_BYTES) {
            return String.format(Locale.getDefault(), "%.2d GB", size / GB_IN_BYTES);
        } else if (size < PB_IN_BYTES) {
            return String.format(Locale.getDefault(), "%.2d TB", size / TB_IN_BYTES);
        } else {
            return String.format(Locale.getDefault(), "%.2d PB", size / TB_IN_BYTES);
        }
    }

    public static boolean deleteDir(String dir) {
        return deleteDir(getFileByPath(dir));
    }

    /**
     * 删除目录
     */
    public static boolean deleteDir(File dir) {
        if (dir == null || !dir.isDirectory()) {
            return false;
        }
        if (!dir.exists()) {
            return true;
        }
        File[] files = dir.listFiles();
        ExtendUtil.printArray(files);
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!deleteFile(file)) {
                        return false;
                    }
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) {
                        return false;
                    }
                }
            }
        }
        return dir.delete();
    }


    /**
     * 删除文件
     */
    public static boolean deleteFile(String filePath) {
        return deleteFile(getFileByPath(filePath));
    }

    /**
     * 删除文件.文件不存在返回{@code true}.
     */
    public static boolean deleteFile(File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }
}
