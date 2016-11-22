package com.king.applib.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.graphics.BitmapFactory.decodeFile;

/**
 * 文件工具类
 * Created by HuoGuangxu on 2016/10/18.
 */

public class ImageUtil {

    private ImageUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    /**
     * 获取bitmap
     * @param file 文件
     * @return bitmap
     */
    public static Bitmap getBitmap(File file) {
        if (file == null) {
            return null;
        }
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            return BitmapFactory.decodeStream(is);
        } catch (FileNotFoundException e) {
            return null;
        } finally {
            IOUtil.close(is);
        }
    }

    public static File saveBitmap(Bitmap bitmap, String imagePath, Bitmap.CompressFormat format, int quality) {
        if (isBitmapEmpty(bitmap) || StringUtil.isNullOrEmpty(imagePath)) {
            return null;
        }
        File file = FileUtil.createFile(imagePath);
        if (file == null) {
            return null;
        }
        if (quality <= 0 || quality > 100) {
            quality = 100;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(format, quality, bos);
        byte[] bytes = bos.toByteArray();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            return null;
        }finally {
            IOUtil.close(fos);
            IOUtil.close(bos);
        }
        return null;
    }

    /**
     * drawable转bitmap
     * @param drawable drawable对象
     * @return bitmap
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        return drawable == null ? null : ((BitmapDrawable) drawable).getBitmap();
    }

    /**
     * bitmap转drawable
     * @param context context对象
     * @param bitmap bitmap对象
     * @return drawable
     */
    public static Drawable bitmap2Drawable(Context context, Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(context.getResources(), bitmap);
    }

    /**
     * 获取bitmap
     * @param resId 文件
     * @return bitmap
     */
    public static Bitmap getBitmap(Context context, @DrawableRes int resId) {
        return drawable2Bitmap(context.getResources().getDrawable(resId));
    }

    /**
     * 按质量压缩
     * @param src 源图片
     * @param quality 质量
     * @return 质量压缩后的图片
     */
    public static Bitmap compressByQuality(Bitmap src, int quality) {
        return compressByQuality(src, quality, false);
    }

    /**
     * 按质量压缩
     * @param src 源图片
     * @param quality 质量
     * @param recycle 是否回收
     * @return 质量压缩后的图片
     */
    public static Bitmap compressByQuality(Bitmap src, int quality, boolean recycle) {
        if (isBitmapEmpty(src) || quality < 0 || quality > 100) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        byte[] bytes = outputStream.toByteArray();
        if (recycle && !src.isRecycled()) {
            src.recycle();
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 判断bitmap对象是否为空
     * @param src 源图片
     */
    public static boolean isBitmapEmpty(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    private static Bitmap ratingImage(String filePath, Bitmap bitmap) {
        int degree = readPictureDegree(filePath);
        return rotaingImageView(degree, bitmap);
    }

    /**
     * 旋转图片
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * Gets the corresponding path to a file from the given content:// URI
     * @param selectedVideoUri The content:// URI to find the file path from
     * @param contentResolver The content resolver to use to perform the query.
     * @return the file path as a string
     */
    public static String uri2ImageFile(Uri selectedVideoUri, ContentResolver contentResolver) {
        String filePath = "";
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
//      也可用下面的方法拿到cursor
//      Cursor cursor = context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return filePath;
    }

    /**
     * Gets the content:// URI  from the given corresponding path to a file
     * @return content Uri
     */
    public static Uri imageFile2Uri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 获取图片宽度.获取不到返回-1.
     */
    public static int getImageWidth(Context context, @DrawableRes int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, options);
        return options.outWidth;
    }

    /**
     * 获取图片宽度.获取不到返回-1.
     */
    public static int getImageWidth(Context context, String imagePath) {
        if (context == null || StringUtil.isNullOrEmpty(imagePath)) {
            return -1;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        decodeFile(imagePath, options);
        return options.outWidth;
    }

    /**
     * 获取图片高度.获取不到返回-1.
     */
    public static int getImageHeight(Context context, @DrawableRes int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, options);
        return options.outHeight;
    }

    /**
     * 获取图片高度.获取不到返回-1.
     */
    public static int getImageHeight(Context context, String imagePath) {
        if (context == null || StringUtil.isNullOrEmpty(imagePath)) {
            return -1;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        decodeFile(imagePath, options);
        return options.outHeight;
    }

    /**
     * 把图片压缩到200K
     *
     * @param oldpath
     *            压缩前的图片路径
     * @param newPath
     *            压缩后的图片路径
     * @return
     */
    /**
     * 把图片压缩到200K
     * @param oldpath 压缩前的图片路径
     * @param newPath 压缩后的图片路径
     */
    public static File compressFile(String oldpath, String newPath) {
        Bitmap compressBitmap = decodeFile(oldpath);
        Bitmap newBitmap = ratingImage(oldpath, compressBitmap);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        newBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        byte[] bytes = os.toByteArray();

        File file = null;
        try {
            file = getFileFromBytes(bytes, newPath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (newBitmap != null) {
                if (!newBitmap.isRecycled()) {
                    newBitmap.recycle();
                }
                newBitmap = null;
            }
            if (compressBitmap != null) {
                if (!compressBitmap.isRecycled()) {
                    compressBitmap.recycle();
                }
                compressBitmap = null;
            }
        }
        return file;
    }

    /**
     * 把字节数组保存为一个文件
     */
    public static File getFileFromBytes(byte[] b, String outputFile) {
        File ret = null;
        BufferedOutputStream stream = null;
        try {
            ret = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(ret);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            // log.error("helper:get file from byte process error!");
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // log.error("helper:get file from byte process error!");
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    /**
     * 按质量压缩
     * @param bitmap 源图片
     * @param maxByteSize 允许最大值字节数
     * @param recycle 是否回收
     * @return 质量压缩压缩过的图片
     */
    public static Bitmap compressByQuality(Bitmap bitmap, long maxByteSize, boolean recycle) {
        if (isBitmapEmpty(bitmap) || maxByteSize <= 0) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        while (outputStream.toByteArray().length > maxByteSize && quality >= 0) {
            outputStream.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality -= 5, outputStream);
        }
        if (quality < 0){
            return null;
        }
        byte[] bytes = outputStream.toByteArray();
        if (recycle && !bitmap.isRecycled())
            bitmap.recycle();
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
