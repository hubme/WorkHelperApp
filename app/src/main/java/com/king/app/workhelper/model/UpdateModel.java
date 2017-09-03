package com.king.app.workhelper.model;

import java.io.Serializable;

/**
 * 应用升级数据结构
 *
 * @author huoguangxu
 * @since 2017/5/23.
 */

public class UpdateModel implements Serializable {
    /** 强制升级类型.0 否 1 是 */
    public int type;
    /** 升级内容 */
    public String content;
    /** 升级的地址 */
    public String url;
    /** 升级到的版本 */
    public String appversion;

    public UpdateModel() {
    }

    public UpdateModel(int type, String content, String url, String appversion) {
        this.type = type;
        this.content = content;
        this.url = url;
        this.appversion = appversion;
    }
}
