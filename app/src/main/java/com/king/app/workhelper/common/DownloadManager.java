package com.king.app.workhelper.common;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.king.app.workhelper.R;
import com.king.applib.constant.GlobalConstants;
import com.king.applib.log.Logger;
import com.king.applib.util.AppUtil;
import com.king.applib.util.DateTimeUtil;
import com.king.applib.util.FileUtil;
import com.king.applib.util.IOUtil;
import com.king.applib.util.StringUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 文件下载管理类。目前只支持单线程单文件下载，包含通知栏提示。
 * Created by HuoGuangxu on 2016/10/20.
 */
// TODO: 2016/10/21 1.多文件下载 2.暂停 3.多线程
public class DownloadManager {
    public static final String TAG = DownloadManager.class.getSimpleName();

    // 下载进度消息发送间隔时间(ms)
    private static final int DOWNLOAD_MSG_INTERVAL = 400;
    private Context mContext;
    private final OkHttpClient mOkHttpClient;
    private DownloadCallback mDownloadCallback;

    private Notification mNotification;
    private RemoteViews mRemoteViews;
    private NotificationManagerCompat mNoticeManager;
    private File mDestFile;
    private File mTempFile;
    private FileDownloadRequest mDownloadRequest;

    public DownloadManager(Context context) {
        mContext = context.getApplicationContext();
        mOkHttpClient = OkHttpUtils.getInstance().getOkHttpClient();
    }

    public void setDownloadCallback(DownloadCallback callback) {
        mDownloadCallback = callback;
    }

    //下载回调接口
    public interface DownloadCallback {
        void complete();

        void failed();

        void pause();

        void downloading();
    }

    /**
     * 下载Request.必须提供下载地址、文件保存目录和文件名.
     */
    public static class FileDownloadRequest implements Parcelable {
        /** 文件下载地址 */
        private String url;
        /** 文件保存目录,不包含文件名 */
        private String dir;
        /** 文件名 */
        private String fileName;
        /** 通知栏显示时候点击事件 */
        private PendingIntent notificationClickIntent;
        /** 是否显示通知 */
        private boolean showNotification;
        /** 通知id */
        private int notificationId;

        public FileDownloadRequest(String url, String dir, String fileName) {
            this.url = url;
            this.dir = dir;
            this.fileName = fileName;
        }

        protected FileDownloadRequest(Parcel in) {
            url = in.readString();
            dir = in.readString();
            fileName = in.readString();
            notificationClickIntent = in.readParcelable(PendingIntent.class.getClassLoader());
            showNotification = in.readByte() != 0;
            notificationId = in.readInt();
        }

        public static final Creator<FileDownloadRequest> CREATOR = new Creator<FileDownloadRequest>() {
            @Override
            public FileDownloadRequest createFromParcel(Parcel in) {
                return new FileDownloadRequest(in);
            }

            @Override
            public FileDownloadRequest[] newArray(int size) {
                return new FileDownloadRequest[size];
            }
        };

        public FileDownloadRequest setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public FileDownloadRequest setDir(String parentDir) {
            this.dir = parentDir;
            return this;
        }

        public FileDownloadRequest setUrl(String url) {
            this.url = url;
            return this;
        }

        public FileDownloadRequest setNotificationClickIntent(PendingIntent notificationClickIntent) {
            this.notificationClickIntent = notificationClickIntent;
            return this;
        }

        public FileDownloadRequest showNotification(boolean showNotification) {
            this.showNotification = showNotification;
            return this;
        }

        public FileDownloadRequest setNotificationId(int notificationId) {
            this.notificationId = notificationId;
            return this;
        }

        @Override public int describeContents() {
            return 0;
        }

        @Override public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(url);
            parcel.writeString(dir);
            parcel.writeString(fileName);
            parcel.writeParcelable(notificationClickIntent, i);
            parcel.writeByte((byte) (showNotification ? 1 : 0));
            parcel.writeInt(notificationId);
        }

        volatile boolean pause;
    }

    private void updateNotificationComplete() {
        if (mNotification == null || mRemoteViews == null) {
            return;
        }
        mRemoteViews.setProgressBar(R.id.progress, 100, 100, false);
        mRemoteViews.setTextViewText(R.id.progress_text, "100%");
        mRemoteViews.setTextViewText(R.id.status, "下载完成，点击安装");
        mNoticeManager.notify(mDownloadRequest.notificationId, mNotification);
    }

    /**
     * 更新通知栏下载进度
     * @param notificationId 通知id
     * @param progress       当前进度
     * @param totalBytes     总需要下载字节数
     * @param currentBytes   本次已下载字节数
     * @param costTime       当前下载耗费时间 单位毫秒
     */
    private void updateNotificationProgress(int notificationId, float progress, long totalBytes, long currentBytes, long costTime) {
        if (mNotification == null || mRemoteViews == null) {
            return;
        }

        mRemoteViews.setProgressBar(R.id.progress, 100, (int) (progress * 100), false);
        mRemoteViews.setTextViewText(R.id.progress_text, String.format(Locale.CHINA, "%.1f%%", progress * 100));
        mRemoteViews.setTextViewText(R.id.status, buildNotificationText(totalBytes, currentBytes, costTime));
        mNoticeManager.notify(notificationId, mNotification);
    }

    //生成通知栏上的文字.//剩余26秒 下载速度：1.19M/s
    private String buildNotificationText(long totalBytes, long currentBytes, long costTime) {
        double curSpeed = (costTime == 0 ? 0L : currentBytes / (costTime / 1000d));//bytes/s
        long leftSec = curSpeed == 0 ? 0 : (long) ((totalBytes - currentBytes) / curSpeed);

        return String.format(Locale.getDefault(), "剩余%s 下载速度：%s", DateTimeUtil.getUnitTime(leftSec * DateUtils.SECOND_IN_MILLIS), getNetSpeed(curSpeed));

    }

    /**
     * 每秒多少bytes.
     * @param bytesPerSecond 一秒的字节数
     */
    private String getNetSpeed(double bytesPerSecond) {
        if (bytesPerSecond < FileUtil.KB_IN_BYTES) {
            return " bytes/s";
        } else if (bytesPerSecond < FileUtil.MB_IN_BYTES) {
            return String.format(Locale.getDefault(), "%.2f K/s", bytesPerSecond / FileUtil.KB_IN_BYTES);
        } else if (bytesPerSecond < FileUtil.GB_IN_BYTES) {
            return String.format(Locale.getDefault(), "%.2f M/s", bytesPerSecond / FileUtil.MB_IN_BYTES);
        } else {
            return String.format(Locale.getDefault(), "%.2f G/s", bytesPerSecond / FileUtil.GB_IN_BYTES);
        }
    }

    /**
     * 取消指定的通知
     */
    private void cancelNotification(int id) {
        if (mNotification != null) {
            mNoticeManager.cancel(id);
            mNotification = null;
            mRemoteViews = null;
        }
    }

    /**
     * 初始化通知栏进度显示
     */
    private void initDownloadProgress(long fileSize) {
        mNoticeManager = NotificationManagerCompat.from(mContext);
        if (!mNoticeManager.areNotificationsEnabled()) {
            cancelNotification(mDownloadRequest.notificationId);
            return;
        }
        if (mNotification != null && mRemoteViews != null) {
            updateNotificationProgress(mDownloadRequest.notificationId, 0, 0, 0, 1);
            return;
        }

        mRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.view_download_notification);
        mRemoteViews.setTextViewText(R.id.file_name, mContext.getString(R.string.app_name));
        mRemoteViews.setTextViewText(R.id.file_size, Formatter.formatFileSize(mContext, fileSize));

        mNotification = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.mipmap.downloading)
                .setContentIntent(mDownloadRequest.notificationClickIntent)
                .setContent(mRemoteViews)
                .setAutoCancel(false)
                .build();

        mNoticeManager.notify(mDownloadRequest.notificationId, mNotification);
    }

    /**
     * 当前下载进度信息
     */
    public static class DownloadStatus {
        public final int status; // 0下载中，1下载完成，2下载失败，3下载暂停
        public final float percent; // 0-1

        public DownloadStatus(int status, float percent) {
            this.status = status;
            this.percent = percent;
        }

        @Override public String toString() {
            return "DownloadStatus{" +
                    "status=" + status +
                    ", percent=" + percent +
                    '}';
        }
    }

    private boolean checkRequest(FileDownloadRequest request) {
        if (request == null || StringUtil.isNullOrEmpty(request.url) || StringUtil.isNullOrEmpty(request.fileName)) {
            return false;
        }
        if (!StringUtil.isNullOrEmpty(request.dir)) {
            File fileDir = FileUtil.createDir(request.dir);
            if (fileDir == null) {
                return false;
            }
        }
        return true;
    }

    private void downloadFileTask() {
        //可能是老包,客户端无法确认是要下载的文件。
        // 请求服务器检查，如果是要下载的文件，不会重新下载；如果不是要下载的文件，重新下载。
        final boolean fileExists = mDestFile.exists() && mDestFile.isFile();

        mTempFile = FileUtil.getFileByPath(mDestFile.getAbsolutePath() + ".tmp");
        final long startLen = mTempFile != null ? mTempFile.length() : 0;
        String lastTag = getLastTag(mDestFile);
        Logger.log(Logger.INFO, TAG, "startLen: " + startLen + "; lastTag: " + lastTag);

        okhttp3.Request.Builder reqBuilder = new okhttp3.Request.Builder();
        if (!TextUtils.isEmpty(lastTag)) {
            if (fileExists) { //如果原文件存在，则比较ETag值，不同则重新下载
                reqBuilder.header("If-None-Match", lastTag);
            } else {
                reqBuilder.header("If-Range", lastTag);
            }
        }
        reqBuilder.header("Range", "bytes=" + startLen + "-");

        Request request = reqBuilder.get().url(mDownloadRequest.url).tag(mDownloadRequest.url).build();

        /*mOkHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Logger.log(Logger.INFO, TAG, Thread.currentThread().toString());
                Logger.log(Logger.INFO, TAG, "onFailure");
                onDownloadFailed();
            }

            @Override public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Logger.log(Logger.INFO, TAG, Thread.currentThread().toString());
                if (response.isSuccessful()) {
                    Logger.log(Logger.INFO, TAG, "receivedContent start");
                    receivedContent(startLen, response);
                    Logger.log(Logger.INFO, TAG, "receivedContent end");
                } else {
                    unreceivedContent(response.code());
                    Logger.log(Logger.INFO, TAG, "unreceivedContent");
                }
            }
        });*/

        try {
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Logger.log(Logger.INFO, TAG, "receivedContent start");
                receivedContent(startLen, response);
                Logger.log(Logger.INFO, TAG, "receivedContent end");
            } else {
                unreceivedContent(response.code());
                Logger.log(Logger.INFO, TAG, "unreceivedContent");
            }

        } catch (IOException e) {

        }

    }

    private void unreceivedContent(int responseCode) {
        switch (responseCode) {
            case GlobalConstants.HTTP_RESPONSE_CODE.NON_CONFORMANCE:
                Logger.log(Logger.INFO, TAG, "请求范围不符合要求，删除临时文件，重新下载.");
                onDownloadFailed();
                clearTempFiles();
                downloadFileTask();
                break;

            case GlobalConstants.HTTP_RESPONSE_CODE.UNMODIFIED:
                Logger.log(Logger.INFO, TAG, "文件未改变，不下载");
                onDownloadSuccess();
                break;
            default:
                break;
        }
    }

    private void clearTempFiles() {
        FileUtil.deleteFile(mDestFile);
        FileUtil.deleteFile(mTempFile);
    }

    /**
     * 下载文件,断点下载
     */
    public void downloadFile(FileDownloadRequest request) {
        if (!checkRequest(request)) {//检查下载请求
            return;
        }
        mDownloadRequest = request;
        //检查保存文件的路径
        mDestFile = FileUtil.getFileByPath(request.dir + "/" + request.fileName);
        if (mDestFile == null) {
            Logger.log(Logger.INFO, TAG, "文件保存路径为null，不下载");
            return;
        }
        Logger.log(Logger.INFO, TAG, "文件保存路径：" + mDestFile.getAbsolutePath());

        downloadFileTask();
    }

    private void receivedContent(long startLength, Response response) {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            inputStream = response.body().byteStream();

            final long contentLength = Long.valueOf(response.header("Content-Length"));//请求的内容长度
            boolean supportContinue = (response.code() == GlobalConstants.HTTP_RESPONSE_CODE.PARTIAL_CONTENT);
            final long totalLength = supportContinue ? startLength + contentLength : contentLength;
            saveLastTag(mDestFile, response.header("ETag"));

            Logger.log(Logger.INFO, TAG, String.format("初始文件的大小:%s, 文件总大小: %s, 是否支持断点续传: %b",
                    Formatter.formatFileSize(mContext, startLength), Formatter.formatFileSize(mContext, totalLength), supportContinue));

            outputStream = new FileOutputStream(mTempFile, supportContinue);

            if (mDownloadRequest.showNotification) {
                initDownloadProgress(totalLength);
            }

            final int BUF_SIZE = 1024 * 4;
            byte[] buffer = new byte[BUF_SIZE];
            final long beginTime = System.currentTimeMillis();//记录下载开始时间
            long lastTime = beginTime;//记录上次更新时间

            int len;
            while ((len = inputStream.read(buffer, 0, BUF_SIZE)) != -1) {
                outputStream.write(buffer, 0, len);
                startLength += len;

                if (mDownloadRequest.pause) {
                    Logger.log(Logger.INFO, TAG, new DownloadStatus(3, startLength / totalLength).toString());
                    break;
                } else {
                    long nt = System.currentTimeMillis();
                    if (nt - lastTime >= DOWNLOAD_MSG_INTERVAL) {
                        lastTime = nt;
                        float progress = (float) startLength / totalLength;
                        updateNotificationProgress(mDownloadRequest.notificationId, progress, totalLength, startLength, nt - beginTime);
                        Logger.log(Logger.INFO, TAG, "totalLength: " + totalLength + "bytes; startLength: "
                                + startLength + "bytes; costTime: " + (nt - beginTime) + "millis; percent: " + (float) startLength / totalLength);
                        if (mDownloadCallback != null) {
                            mDownloadCallback.downloading();
                        }
                    }
                }
            }
            outputStream.flush();

            if (mTempFile.length() == totalLength) {
                Logger.log(Logger.INFO, TAG, "文件下载成功。" + new DownloadStatus(1, 1).toString());

                // TODO: 2016/12/8 文件校验
                boolean renameSuccess = mTempFile.renameTo(mDestFile);
                if (!renameSuccess) {
                    return;
                }
                onDownloadSuccess();
            } else {
                clearTempFiles();
                downloadFileTask();
            }
        } catch (Exception e) {
            onDownloadFailed();
        } finally {
            IOUtil.close(inputStream);
            IOUtil.close(outputStream);
        }
    }

    //文件下载成功。1.文件没有改变，但是没有下载.2.文件下载成功
    private void onDownloadSuccess() {
        cancelNotification(mDownloadRequest.notificationId);
        if (mDownloadRequest.showNotification) {//通知栏提示自动安装
            AppUtil.installApk(mContext, mDestFile);
        }
        if (mDownloadCallback != null) {
            mDownloadCallback.complete();
        }
    }

    private void onDownloadFailed() {
        if (mDownloadCallback != null) {
            mDownloadCallback.failed();
        }
    }

    /** 获取保存的下载文件的ETag值 */
    private String getLastTag(File tmpFile) {
        FileReader fr = null;
        try {
            File tagFile = FileUtil.getFileByPath(tmpFile.getAbsolutePath() + "_");
            if (tagFile == null || !tagFile.exists()) {
                return null;
            }

            fr = new FileReader(tagFile);
            BufferedReader br = new BufferedReader(fr);
            return br.readLine();
        } catch (Exception e) {
            return null;
        } finally {
            IOUtil.close(fr);
        }
    }

    /** 保存文件ETag值 **/
    private boolean saveLastTag(File file, String tag) {
        FileWriter fw = null;
        try {
            File tagFile = FileUtil.createFile(file.getAbsolutePath() + "_");
            if (tagFile == null) {
                return false;
            }
            if (TextUtils.isEmpty(tag)) {
                return tagFile.delete();
            }
            fw = new FileWriter(tagFile, false);
            BufferedWriter br = new BufferedWriter(fw);
            br.write(tag);
            br.flush();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            IOUtil.close(fw);
        }
    }
}
