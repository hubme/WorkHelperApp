package com.king.app.workhelper.common;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.king.applib.log.Logger;
import com.king.applib.util.IOUtil;
import com.king.applib.util.StringUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.text.DecimalFormat;

import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * 文件下载管理器
 * Created by HuoGuangxu on 2016/10/25.
 */

public class DownloadManager {
    public static final String ACTION_DOWNLOAD_FILE = "fund.intent.action.DOWNLOAD_FILE";
    private Context mContext;
    private String mAppFileName = "update.apk";
    private final OkHttpClient mOkHttpClient;
    // 下载进度消息发送间隔时间(ms)
    private static final int DOWNLOAD_MSG_INTERVAL = 400;
    private static final String APK_DOWNLOAD_URL = "http://192.168.38.2:8080/appupdate/app_update.apk";

    public DownloadManager(Context context) {
        mContext = context;
        // FIXME: 2016/10/22 使用OkHttpClient单例
        mOkHttpClient = new OkHttpClient();
    }

    /**
     * 下载Request
     */
    public static class Request implements Parcelable {
        //下载地址
        private String url;
        //文件保存目录,不包含文件名
        private String dir;

        public Request(String url) {
            this.url = url;
        }

        public String getDir() {
            return dir;
        }

        public void setDir(String parentDir) {
            this.dir = parentDir;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Request && url.equals(((Request) o).url);
        }

        protected Request(Parcel in) {
            url = in.readString();
            pause = in.readByte() != 0;
        }

        public static final Creator<Request> CREATOR = new Creator<Request>() {
            @Override
            public Request createFromParcel(Parcel in) {
                return new Request(in);
            }

            @Override
            public Request[] newArray(int size) {
                return new Request[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(url);
            dest.writeByte((byte) (pause ? 1 : 0));
        }

        volatile boolean pause;
    }

    /**
     * 当前下载进度信息
     */
    public static class DownloadStatus {
        public final Request request;
        public final int status; // 0下载中，1下载完成，2下载失败，3下载暂停
        public final float percent; // 0-1

        public DownloadStatus(Request request, int status, float percent) {
            this.request = request;
            this.status = status;
            this.percent = percent;
        }

        @Override public String toString() {
            return "DownloadStatus{" +
                    "request=" + request +
                    ", status=" + status +
                    ", percent=" + percent +
                    '}';
        }
    }

    /**
     * 下载文件
     * <p>
     * 自动断点续传
     * @return 下载好的文件，失败则返回null
     */
    public File downloadFile(Request req) {
        File destFile;
        if (!StringUtil.isNullOrEmpty(req.getDir())) {
            destFile = new File(req.getDir(), mAppFileName);
        } else {
            destFile = new File(mContext.getExternalCacheDir(), mAppFileName);
        }
        Logger.i("文件下载路径：" + destFile.getAbsolutePath());

        final boolean destFileExists = destFile.exists() && destFile.isFile();
        File tmpFile = new File(destFile.getAbsolutePath() + ".tmp");
        long startLen = tmpFile.length();
        String lastTag = getLastTag(destFile);

        okhttp3.Request.Builder reqBuilder = new okhttp3.Request.Builder();

        if (!TextUtils.isEmpty(lastTag)) {
            if (destFileExists) { // 如果原文件存在，则比较ETag值，不同则重新下载
                reqBuilder.header("If-None-Match", lastTag);
            } else {
                reqBuilder.header("If-Range", lastTag);
            }
        }
        reqBuilder.header("Range", "bytes=" + startLen + "-");

        InputStream inputStream = null;
        FileOutputStream fos = null;
        try {
            okhttp3.Request request = reqBuilder.get().url(APK_DOWNLOAD_URL).build();
            Response response = mOkHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                boolean supportContinue = (response.code() == 206);
                long cl = Long.valueOf(response.header("Content-Length"));
                long totalLen = supportContinue ? startLen + cl : cl;
                saveLastTag(destFile, response.header("ETag"));

                Logger.i(String.format("下载文件->%s,大小:%s(%s),支持断点续传%b", APK_DOWNLOAD_URL, formatFileSize(cl, "0.00"),
                        formatFileSize(totalLen, "0.00"), supportContinue));

                inputStream = response.body().byteStream();
                fos = new FileOutputStream(tmpFile, supportContinue);

                final int BUF_SIZE = 1024 * 4;
                byte[] buf = new byte[BUF_SIZE];
                int len;
                long lastSendTime = System.currentTimeMillis();
                while ((len = inputStream.read(buf, 0, BUF_SIZE)) != -1) {
                    fos.write(buf, 0, len);
                    startLen += len;

                    if (req.pause) {
                        Logger.i(new DownloadStatus(req, 3, startLen / totalLen).toString());
                        break;
                    } else {
                        long nt = System.currentTimeMillis();
                        if (nt - lastSendTime >= DOWNLOAD_MSG_INTERVAL) {
                            lastSendTime = nt;
                            Logger.i(new DownloadStatus(req, 0, (float) startLen / totalLen).toString());
                        }
                    }
                }
                fos.flush();

                if (tmpFile.exists() && tmpFile.length() == totalLen) {
                    if (destFileExists) {
                        destFile.delete();
                    }
                    File f = tmpFile.renameTo(destFile) ? destFile : tmpFile;
                    Logger.i(new DownloadStatus(req, 1, 1).toString());
                    return f;
                } else {
                    return null;
                }
            } else if (response.code() == 416) {
                if (tmpFile.delete()) {
                    downloadFile(req);
                }
            } else if (response.code() == 304 && destFileExists) {
                Logger.i(String.format("文件未改变，不下载->(%s)", APK_DOWNLOAD_URL));
                Logger.i(new DownloadStatus(req, 1, 1).toString());
                return destFile;
            } else {
                throw new RuntimeException("unknown response code->" + response.code());
            }
        } catch (Exception e) {
            Logger.i(new DownloadStatus(req, 2, 1).toString());
            Logger.i(String.format("download file failed, url->" + APK_DOWNLOAD_URL, e));
        } finally {
            IOUtil.close(inputStream);
            IOUtil.close(fos);
        }
        return null;
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
            Logger.i("read tmp file tag failed!");
            return false;
        } finally {
            IOUtil.close(fw);
        }
    }

    /**
     * 返回byte的数据大小对应的格式化大小文字
     * @param size 大小
     * @param format 格式化格式（DecimalFormat参数）
     * @return 格式化后的文字
     */
    public static String formatFileSize(long size, String format) {
        DecimalFormat formater = new DecimalFormat(format);
        if (size < 1024) {
            return size + "bytes";
        } else if (size < 1024 * 1024) {
            return formater.format(size / 1024f) + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            return formater.format(size / 1024f / 1024f) + "MB";
        } else {
            return formater.format(size / 1024f / 1024f / 1024f) + "GB";
        }
    }
}
