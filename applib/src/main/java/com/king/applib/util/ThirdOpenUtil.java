package com.king.applib.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * 打开第三方应用的工具类。
 *
 * @author huoguangxu
 * @since 2018/1/26.
 */

public class ThirdOpenUtil {
    /** 打开QQ或Tim客户端的协议 */
    public static final String QQ_TIM_PROTOCOL = "mqqwpa://im/chat";

    private ThirdOpenUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    /** 创建QQ或Tim的临时会话 */
    public static boolean openQQTim(Context context, String qq) {
        if (context == null || StringUtil.isNullOrEmpty(qq)) {
            return false;
        }
        String url = QQ_TIM_PROTOCOL.concat("?chat_type=wpa&uin=").concat(qq);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (canResolveIntent(context, intent)) {
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * 发送邮件到指定联系人。<br/>
     * https://developer.android.com/guide/components/intents-common.html?hl=zh-cn
     */
    public static boolean sendEmail(Context context, String emailAddress) {
        if (context == null || StringUtil.isNullOrEmpty(emailAddress)) {
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        //如果您想确保 Intent 只由电子邮件应用（而非其他短信或社交应用）进行处理，则需使用 ACTION_SENDTO 操作并加入 "mailto:" 数据架构。
        intent.setData(Uri.parse("mailto:".concat(emailAddress)));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (canResolveIntent(context, intent)) {
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    /** 能处理当前 Intent 返回 true，否则返回 false */
    public static boolean canResolveIntent(Context context, Intent intent) {
        return intent.resolveActivityInfo(context.getPackageManager(), PackageManager.MATCH_DEFAULT_ONLY) != null;
    }
}
