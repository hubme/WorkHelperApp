package com.king.app.workhelper.common;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.king.app.workhelper.R;
import com.king.applib.constant.GlobalConstants;
import com.king.applib.log.Logger;
import com.king.applib.util.FileUtil;
import com.king.applib.util.IOUtil;
import com.king.applib.util.StringUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 文件下载管理类。
 * Created by HuoGuangxu on 2016/10/20.
 */
// TODO: 2016/10/21 1.支持多文件下载 2.添加状态回调id
public class DownloadManager {
    public static final String TAG = DownloadManager.class.getSimpleName();
    public static final String ACTION_DOWNLOAD_FILE = "ACTION_DOWNLOAD_FILE";

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

    public DownloadManager(Context context) {
        mContext = context.getApplicationContext();// NOTICE: 2016/12/8 会不会内存泄露
        mOkHttpClient = new OkHttpClient();
    }

    public void setDownloadCallback(DownloadCallback callback) {
        mDownloadCallback = callback;
    }

    //下载回调接口
    public interface DownloadCallback {
        void complete();

        void failed();

        void pause();
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(url);
            parcel.writeString(dir);
            parcel.writeString(fileName);
            parcel.writeParcelable(notificationClickIntent, i);
            parcel.writeByte((byte) (showNotification ? 1 : 0));
            parcel.writeInt(notificationId);
        }

        volatile boolean pause;
    }

    /**
     * 更新通知栏下载进度
     * @param notificationId 通知id
     * @param progress 当前进度
     * @param totalBytes 总需要下载字节数
     * @param currentBytes 本次已下载字节数
     * @param costTime 当前下载耗费时间 单位毫秒
     */
    private void updateDownloadProgress(int notificationId, float progress, long totalBytes, long currentBytes, long costTime) {
        if (mNotification == null || mRemoteViews == null) {
            return;
        }

        mRemoteViews.setProgressBar(R.id.progress, 100, (int) (progress * 100), false);
        mRemoteViews.setTextViewText(R.id.progress_text, String.format(Locale.CHINA, "%.1f", progress * 100) + "%");
        mRemoteViews.setTextViewText(R.id.status, buildNotificationText(totalBytes, currentBytes, costTime));
        mNoticeManager.notify(notificationId, mNotification);
    }

    //生成通知栏上的文字
    private String buildNotificationText(long totalBytes, long currentBytes, long costTime) {
        StringBuilder sb = new StringBuilder();
        sb.append("剩余");

        double curSpeed = currentBytes / (costTime * 1000);
        int leftSec = (int) ((totalBytes - currentBytes) / curSpeed + 0.5);
        if (leftSec > 60) {
            sb.append(leftSec / 60).append("分钟");
        } else {
            sb.append(leftSec).append("秒");
        }
        sb.append(" 下载速度：");

        if (curSpeed < 1024) {
            sb.append((int) curSpeed).append(" bytes/s");
        } else if (curSpeed < 1024 * 1024) {
            sb.append(String.format(Locale.getDefault(), "%.2f", curSpeed / 1024)).append(" K/s");
        } else if (curSpeed < 1024 * 1024 * 1024) {
            sb.append(String.format(Locale.getDefault(), "%.2f", curSpeed / 1024 / 1024)).append(" M/s");
        } else {
            sb.append(String.format(Locale.getDefault(), "%.2f", curSpeed / 1024 / 1024 / 1024)).append(" G/s");
        }

        return sb.toString();
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
     * @param request Request
     */
    private void initDownloadProgress(FileDownloadRequest request, long fileSize) {
        mNoticeManager = NotificationManagerCompat.from(mContext);
        if (!mNoticeManager.areNotificationsEnabled()) {
            cancelNotification(request.notificationId);
            return;
        }
        if (mNotification != null && mRemoteViews != null) {
            updateDownloadProgress(request.notificationId, 0, 0, 0, 1);
            return;
        }

        mRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.view_download_notification);
        mRemoteViews.setTextViewText(R.id.file_name, request.fileName);
        mRemoteViews.setTextViewText(R.id.file_size, Formatter.formatFileSize(mContext, fileSize));

        mNotification = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(request.notificationClickIntent)
                .setContent(mRemoteViews)
                .build();

        mNoticeManager.notify(request.notificationId, mNotification);
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

        @Override
        public String toString() {
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

    private void downloadFileTask(final FileDownloadRequest downloadRequest, final long startLen, String lastTag, boolean fileExists) {
        Request.Builder reqBuilder = new Request.Builder();
        if (!TextUtils.isEmpty(lastTag)) {
            if (fileExists) { //如果原文件存在，则比较ETag值，不同则重新下载
                reqBuilder.header("If-None-Match", lastTag);
            } else {
                reqBuilder.header("If-Range", lastTag);
            }
        }
        reqBuilder.header("Range", "bytes=" + startLen + "-");
        Request request = reqBuilder.get().url(downloadRequest.url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.log(Logger.INFO, TAG, "onFailure" + Thread.currentThread().toString());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Logger.log(Logger.INFO, TAG, Thread.currentThread().toString());
                if (response.isSuccessful()) {
                    Logger.log(Logger.INFO, TAG, "receivedContent start");
                    receivedContent(downloadRequest, startLen, response);
                    Logger.log(Logger.INFO, TAG, "receivedContent end");
                } else {
                    unreceivedContent(response.code());
                    Logger.log(Logger.INFO, TAG, "unreceivedContent");
                }
            }
        });

    }

    private void unreceivedContent(int responseCode) {
        switch (responseCode) {
            case GlobalConstants.HTTP_RESPONSE_CODE.NON_CONFORMANCE:
                Logger.log(Logger.INFO, TAG, "请求范围不符合要求，删除tmpFile");
                // NOTICE: 2016/12/8 删除临时文件
                break;

            case GlobalConstants.HTTP_RESPONSE_CODE.UNMODIFIED:
                Logger.log(Logger.INFO, TAG, "文件未改变，不下载");
                onFileUnmodified();
                break;
            default:
                break;
        }
    }

    /**
     * 下载文件,断点下载
     */
    public void downloadFile(FileDownloadRequest request) {
        if (!checkRequest(request)) {//检查下载请求
            return;
        }
        //检查保存文件的路径
        mDestFile = FileUtil.getFileByPath(request.dir + "/" + request.fileName);
        if (mDestFile == null) {
            Logger.log(Logger.INFO, TAG, "文件保存路径为null，不下载");
            return;
        }
        Logger.log(Logger.INFO, TAG, "文件保存路径：" + mDestFile.getAbsolutePath());

        //可能是老包,客户端无法确认是要下载的文件。
        // 请求服务器检查，如果是要下载的文件，不会重新下载；如果不是要下载的文件，重新下载。
        final boolean destFileExists = mDestFile.exists() && mDestFile.isFile();

        mTempFile = FileUtil.getFileByPath(mDestFile.getAbsolutePath() + ".tmp");
        long startLen = mTempFile != null ? mTempFile.length() : 0;
        String lastTag = getLastTag(mDestFile);
        Logger.log(Logger.INFO, TAG, "startLen: " + startLen + "; lastTag: " + lastTag);

        downloadFileTask(request, startLen, lastTag, destFileExists);
    }

    private void onFileUnmodified() {
        if (mDownloadCallback != null) {
            mDownloadCallback.complete();
        }
    }

    private void receivedContent(FileDownloadRequest downloadRequest, long startLen, Response response) {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        byte[] bytesContent = null;
        try {
            inputStream = response.body().byteStream();
            long cl;
            final String contentLength = response.header("Content-Length");
            if (TextUtils.isEmpty(contentLength)) {
                bytesContent = convertToByteArray(inputStream);
                cl = bytesContent == null ? 0 : bytesContent.length;
            } else {
                cl = Long.valueOf(contentLength);
            }
            boolean supportContinue = (response.code() == GlobalConstants.HTTP_RESPONSE_CODE.PARTIAL_CONTENT);
            final long totalLength = supportContinue ? startLen + cl : cl;
            saveLastTag(mDestFile, response.header("ETag"));

            Logger.log(Logger.INFO, TAG, String.format("下载文件的大小:%s(%s),是否支持断点续传: %b",
                    Formatter.formatFileSize(mContext, cl), Formatter.formatFileSize(mContext, totalLength), supportContinue));

            if (bytesContent != null) {
                inputStream = new ByteArrayInputStream(bytesContent);
            }
            outputStream = new FileOutputStream(mTempFile, supportContinue);

            if (downloadRequest.showNotification) {
                initDownloadProgress(downloadRequest, totalLength);
            }

            final int BUF_SIZE = 1024 * 4;
            byte[] buffer = new byte[BUF_SIZE];
            int len;
            long lastSendTime = System.currentTimeMillis();
            final long beginTime = lastSendTime;

            while ((len = inputStream.read(buffer, 0, BUF_SIZE)) != -1) {
                outputStream.write(buffer, 0, len);
                startLen += len;

                if (downloadRequest.pause) {
                    Logger.log(Logger.INFO, TAG, new DownloadStatus(3, startLen / totalLength).toString());
                    break;
                } else {
                    long nt = System.currentTimeMillis();
                    if (nt - lastSendTime >= DOWNLOAD_MSG_INTERVAL) {
                        lastSendTime = nt;
                        float progress = (float) startLen / totalLength;
                        updateDownloadProgress(downloadRequest.notificationId, progress, totalLength, len, nt - beginTime);
                        Logger.log(Logger.INFO, TAG, new DownloadStatus(0, (float) startLen / totalLength).toString());
                    }
                }
            }
            outputStream.flush();

            if (mTempFile.length() == totalLength) {
                // NOTICE: 2016/12/8 文件检查
                File completedFile = mTempFile.renameTo(mDestFile) ? mDestFile : mTempFile;
                Logger.log(Logger.INFO, TAG, new DownloadStatus(1, 1).toString());
                if (mDownloadCallback != null) {
                    mDownloadCallback.complete();
                }
            }
        } catch (IOException e) {
            //
        } finally {
            IOUtil.close(inputStream);
            IOUtil.close(outputStream);
        }
    }

    /** 获取保存的下载文件的ETag值 */
    private String getLastTag(File tmpFile) {
        FileReader fr = null;
        try {
            File tagFile = new File(tmpFile.getAbsolutePath() + "_");
            if (!tagFile.exists()) {
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
            File tagFile = new File(file.getAbsolutePath() + "_");
            if (TextUtils.isEmpty(tag)) {
                return tagFile.delete();
            }
            fw = new FileWriter(tagFile, false);
            BufferedWriter br = new BufferedWriter(fw);
            br.write(tag);
            br.flush();
            return true;
        } catch (Exception e) {
            Logger.log(Logger.INFO, TAG, "read tmp file tag failed!");
            return false;
        } finally {
            IOUtil.close(fw);
        }
    }

    private byte[] convertToByteArray(InputStream inputStream) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int len;
        byte[] data = new byte[16384];
        try {
            while ((len = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, len);
            }
            buffer.flush();
            return buffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
