package com.yizhishang.common.util;

import cn.hutool.core.util.StrUtil;

/**
 * @Description : 加密字符串 替换* 号
 * @Author : hou zhao
 * @Date: 2020-07-03 15:31
 */
public class EncryptorUtil {


    private EncryptorUtil() {
        super();
    }

    private static final String A = "@";

    private static final String D = ".";

    private static final char X = '*';

    private static final String XXX = "***";

    /**
     * 邮箱，@前使用星号*隐藏后几位（大于3位保留3位，小于3位保留1位），
     *
     * @param email
     * @return
     * @后使用星号*隐藏后几位（保留1位），点号后直接显示， 如：Qio***@s***.com，637***@q***.com。
     */
    public static String hideMailbox(String email) {
        if (StringUtil.isBlank(email) || email.indexOf(A) == -1 || email.indexOf(D) == -1) {
            return email;
        }
        String[] emailArr = email.split(A);
        String befor = "";
        int beforLen = emailArr[0].length();
        if (beforLen > 3) {
            befor = emailArr[0].substring(0, 3) + XXX;
        } else if (beforLen >= 1) {
            String sub1 = emailArr[0].substring(0, 1);
            befor = sub1 + XXX;
        } else {
            befor = emailArr[0];
        }

        String after = "";
        int afterLen = emailArr[1].length();
        int lastI = emailArr[1].lastIndexOf(D);
        if (afterLen < 1 || emailArr[1].lastIndexOf(D) == -1) {
            after = emailArr[1];
        } else {
            String substring = emailArr[1].substring(lastI, afterLen);
            after = emailArr[1].substring(0, 1) + XXX + substring;

        }
        return befor + A + after;
    }

    /**
     * 电话、手机、身份证件号、银行卡号，支付账号，
     * 10位以上，保留前三后四,
     * 10位及10位以下，保留后四位
     * ，如：137****1234，441***********0021，******6666。
     *
     * @param number
     * @return
     */
    public static String hideNumber(String number) {
        if (StringUtil.isBlank(number)) {
            return number;
        }
        int length = number.length();
        if (length > 10) {
            String befor = number.substring(0, 3);
            String after = number.substring(length - 4);
            return befor + StrUtil.fillBefore(after, X, length - 3);
        } else if (number.length() > 4) {
            String after = number.substring(length - 4);
            return StrUtil.fillBefore(after, X, length);
        } else {
            return number;
        }

    }


}
