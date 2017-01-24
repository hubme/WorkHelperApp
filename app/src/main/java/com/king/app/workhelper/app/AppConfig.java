package com.king.app.workhelper.app;

/**
 * 配置信息类
 * created by VanceKing at 2016/9/29.
 */

public class AppConfig {
    /** 日志tag */
    public static final String LOG_TAG = "aaa";
    /** http连接超时时间 */
    public static final long HTTP_CONNECT_TIME_OUT = 15 * 1000L;
    /** http读数据超时时间 */
    public static final long HTTP_READ_TIME_OUT = 20 * 1000L;
    /** http写数据超时时间 */
    public static final long HTTP_WRITE_TIME_OUT = 20 * 1000L;
    /** http最大响应缓存大小 */
    public static final long HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1004 * 1024L;
}
