package com.yizhishang.common.util;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @author yizhishang
 * @Description : 加密字符串 替换* 号
 * @since 2020-07-03 15:31
 */
public class DesensitizationUtil {


    private DesensitizationUtil() {
        super();
    }

    private static final String A = "@";

    private static final String D = ".";

    private static final char X = '*';

    private static final String XXX = "***";

    private static final String PROVINCE = "省";

    private static final String CITY = "市";

    /**
     * 邮箱，@前使用星号*隐藏后几位（大于3位保留3位，小于3位保留1位），
     *
     * @param email
     * @return
     * @后使用星号*隐藏后几位（保留1位），点号后直接显示， 如：Qio***@s***.com，637***@q***.com。
     */
    public static String hideMailbox(String email) {
        if (StringUtils.isBlank(email) || email.indexOf(A) == -1 || email.indexOf(D) == -1) {
            return email;
        }
        String[] emailArr = email.split(A);
        String before = "";
        int beforeLength = emailArr[0].length();
        if (beforeLength > 3) {
            before = emailArr[0].substring(0, 3) + XXX;
        } else if (beforeLength >= 1) {
            before = emailArr[0].substring(0, 1) + XXX;
        } else {
            before = emailArr[0];
        }

        String after = "";
        int afterLength = emailArr[1].length();
        int lastIndex = emailArr[1].lastIndexOf(D);
        if (afterLength < 1 || emailArr[1].lastIndexOf(D) == -1) {
            after = emailArr[1];
        } else {
            String substring = emailArr[1].substring(lastIndex, afterLength);
            after = emailArr[1].substring(0, 1) + XXX + substring;

        }
        return before + A + XXX;
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
        if (StringUtils.isBlank(number)) {
            return number;
        }
        int length = number.length();
        if (length > 10) {
            String before = number.substring(0, 3);
            String after = number.substring(length - 4);
            return before + StrUtil.fillBefore(after, X, length - 3);
        }
        if (length > 4) {
            String after = number.substring(length - 4);
            return StrUtil.fillBefore(after, X, length);
        }
        return number;
    }

    /**
     * 姓名脱敏
     *
     * @param username 姓名
     * @return
     */
    public static String hideUserName(String username) {
        if (StringUtils.isBlank(username)) {
            return username;
        }
        return username.substring(0, 1) + XXX;
    }

    /**
     * 姓名脱敏
     *
     * @param address 姓名
     * @return
     */
    public static String hideAddress(String address) {
        if (StringUtils.isBlank(address)) {
            return address;
        }
        boolean hasProvince = address.indexOf(PROVINCE) > -1;
        if (hasProvince) {
            String[] addressArray = address.split(PROVINCE);
            return addressArray[0] + PROVINCE + XXX;
        }

        boolean hasCity = address.indexOf(CITY) > -1;
        if (hasCity) {
            String[] addressArray = address.split(CITY);
            return addressArray[0] + CITY + XXX;
        }
        return "";
    }

}
