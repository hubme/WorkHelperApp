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
import android.support.annotation.NonNull;

import com.king.applib.log.Logger;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.graphics.Bitmap.createBitmap;
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
     *
     * @param filePath 文件路径
     * @return bitmap
     */
    public static Bitmap getBitmap(String filePath) {
        if (StringUtil.isNullOrEmpty(filePath)) {
            return null;
        }
        return BitmapFactory.decodeFile(filePath);
    }

    /**
     * 获取bitmap
     *
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

    /**
     * 保存Bitmap到File
     *
     * @param bitmap    Bitmap
     * @param imagePath 保存文件的绝对路径
     * @param format    图片格式
     * @param quality   压缩质量(0, 100]
     * @return File
     */
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
        } catch (Exception e) {
            return null;
        } finally {
            IOUtil.close(fos);
            IOUtil.close(bos);
        }
        return file;
    }

    /**
     * 图片文件压缩到指定宽高后保存.
     *
     * @param file      file对象
     * @param imagePath 保存图片的全路径
     * @param maxWidth  图片最大宽度
     * @param maxHeight 图片的最大高度
     * @return 保存文件的对象
     */
    public static File compressBySampling(File file, String imagePath, int maxWidth, int maxHeight) {
        Bitmap bitmap = ImageUtil.getBitmap(file, maxWidth, maxHeight);
        return ImageUtil.saveBitmap(bitmap, imagePath, Bitmap.CompressFormat.JPEG, 100);
    }

    /**
     * drawable转bitmap
     *
     * @param drawable drawable对象
     * @return bitmap
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        return drawable == null ? null : ((BitmapDrawable) drawable).getBitmap();
    }

    /**
     * bitmap转drawable
     *
     * @param context context对象
     * @param bitmap  bitmap对象
     * @return drawable
     */
    public static Drawable bitmap2Drawable(Context context, Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(context.getResources(), bitmap);
    }

    /**
     * 获取bitmap
     *
     * @param resId 文件
     * @return bitmap
     */
    public static Bitmap getBitmap(Context context, @DrawableRes int resId) {
        return drawable2Bitmap(context.getResources().getDrawable(resId));
    }

    /**
     * 按质量压缩
     *
     * @param src     源图片
     * @param quality 质量
     * @return 质量压缩后的图片
     */
    public static Bitmap compressByQuality(Bitmap src, int quality) {
        return compressByQuality(src, quality, false);
    }

    /**
     * 按质量压缩
     *
     * @param src     源图片
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

    public static Bitmap compressBySize(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //这里压缩options%，把压缩后的数据存放到baos中
            if (options > 0) {
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);//注意quality must be [0, 100]
            }
            options -= 10;
            Logger.i("options : " + options);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        return BitmapFactory.decodeStream(isBm, null, null);
    }


    /**
     * 判断bitmap对象是否为空
     *
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
     *
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 读取图片属性：旋转的角度
     *
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
     *
     * @param selectedVideoUri The content:// URI to find the file path from
     * @param contentResolver  The content resolver to use to perform the query.
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
     *
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

        return file;
    }

    /**
     * 按质量压缩到指定大小的图片
     *
     * @param bitmap      源图片
     * @param maxByteSize 允许最大值字节数
     * @param recycle     是否回收
     * @return 压缩过的图片
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
            if (quality <= 0) {
                break;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality -= 5, outputStream);
        }
        byte[] bytes = outputStream.toByteArray();
        if (recycle && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 计算采样大小
     *
     * @param options   选项
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return 采样大小
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int maxWidth, int maxHeight) {
        if (maxWidth == 0 || maxHeight == 0) {
            return 1;
        }
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        while ((height >>= 1) >= maxHeight && (width >>= 1) >= maxWidth) {
            inSampleSize <<= 1;
        }

        return inSampleSize;
    }


    /**
     * 获取bitmap
     *
     * @param file      文件
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return bitmap
     */
    public static Bitmap getBitmap(File file, int maxWidth, int maxHeight) {
        if (file == null || maxWidth <= 0 || maxHeight <= 0) {
            return null;
        }
        InputStream inputStream = null;
        InputStream decodeStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
            Logger.i("图片采样率：" + options.inSampleSize);
            options.inJustDecodeBounds = false;

            /*
            创建新的InputStream，以前的InputStream无法保存Bitmap。
            The problem was that once you've used an InputStream from a HttpUrlConnection,
            you can't rewind and use the same InputStream again.
            Therefore you have to create a new InputStream for the actual sampling of the image.
             */
            decodeStream = new BufferedInputStream(new FileInputStream(file));
            return BitmapFactory.decodeStream(decodeStream, null, options);
        } catch (FileNotFoundException e) {
            return null;
        } finally {
            IOUtil.close(inputStream);
            IOUtil.close(decodeStream);
        }
    }

    /** 获取一定透明度的图片 */
    public static Bitmap getTransparentBitmap(@NonNull Bitmap source, int number) {
        int[] argb = new int[source.getWidth() * source.getHeight()];
        // 获得图片的ARGB值
        source.getPixels(argb, 0, source.getWidth(), 0, 0, source.getWidth(), source.getHeight());
        for (int i = 0; i < argb.length; i++) {
            argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);
        }
        source = createBitmap(argb, source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        return source;
    }

}
