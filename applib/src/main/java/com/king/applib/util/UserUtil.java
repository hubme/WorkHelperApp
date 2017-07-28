package com.king.applib.util;

//CHECKSTYLE:OFF

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验身份证
 * @since 2013年11月14日 下午1:58:18
 */
public class UserUtil {

    private static final int[] weight = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};
    // 校验码
    private static final int[] checkDigit = new int[]{1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2};

    /**
     * 检查注册用户名是否符合要求
     */
    public static boolean checkUserStr(String s) {
        String str = "[A-Za-z0-9_|\u4e00-\u9fa5]*";
        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(s);
        if (!matcher.matches()) {
            return false;
        }

        str = "\\$|\\(|\\)|\\*|\\+|\\-|\\.|\\[|]|\\?|\\|\\^|\\{|\\||}|~|`|!|@|#|%|&|=|<|>|/|,|'| |　|\\:|林美眉|林MM|林美湄|大鳄浮头|500158|今夜很冷|余超群|小赌怡情|专家团|大赢家|盈彩畅联|辉煌|dyj|小鹰|无双|大师|投资团|法[ 　]*轮[ 　]*功";
        pattern = Pattern.compile(str);
        matcher = pattern.matcher(s);

        return !matcher.matches();
    }

    /**
     * 验证身份证是否符合格式
     */
    public static boolean verifyIDCard(String idcard) {
        if (StringUtil.isNullOrEmpty(idcard) || (idcard.length() != 15 && idcard.length() != 18)) {
            return false;
        }

        if (idcard.length() == 15) {
            //15 位的身份证不能带X等字母
            Pattern pattern = Pattern.compile("[0-9]{1,}");
            if (pattern.matcher(idcard).matches()) {
                idcard = update2eighteen(idcard);
            } else {
                return false;
            }
        }
        if (!NumberUtil.isInteger(idcard.substring(0, 17))) {
            return false;
        }
        // 获取输入身份证上的最后一位，它是校验码
        // 比较获取的校验码与本方法生成的校验码是否相等
        return idcard.substring(17, 18).equalsIgnoreCase(getCheckDigit(idcard));
    }

    /**
     * 计算18位身份证的校验码
     * @param eighteenCardID 18位身份证
     */
    private static String getCheckDigit(String eighteenCardID) {
        int remaining = 0;
        if (eighteenCardID.length() == 18) {
            eighteenCardID = eighteenCardID.substring(0, 17);
        }

        if (eighteenCardID.length() == 17) {
            int sum = 0;
            int[] a = new int[17];
            // 先对前17位数字的权求和
            for (int i = 0; i < 17; i++) {
                String k = eighteenCardID.substring(i, i + 1);

                a[i] = Integer.parseInt(k);
            }
            for (int i = 0; i < 17; i++) {
                sum = sum + weight[i] * a[i];
            }
            // 再与11取模
            remaining = sum % 11;
        }
        return remaining == 2 ? "X" : String.valueOf(checkDigit[remaining]);
    }

    /**
     * 将15位身份证升级成18位身份证号码
     */
    private static String update2eighteen(String fifteenCardID) {
        // 15位身份证上的生日中的年份没有19，要加上
        String eighteenCardID = fifteenCardID.substring(0, 6) + "19" + fifteenCardID.substring(6, 15);
        return eighteenCardID + getCheckDigit(eighteenCardID);
    }
}
//CHECKSTYLE:ON