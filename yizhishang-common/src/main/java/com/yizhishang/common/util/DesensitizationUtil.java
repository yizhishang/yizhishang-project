package com.yizhishang.common.util;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @author yizhishang
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
     * @param email 邮箱
     * @return 邮箱密文
     */
    public static String hideMailbox(String email) {
        if (StringUtils.isBlank(email) || !email.contains(A) || !email.contains(D)) {
            return email;
        }
        String[] emailArr = email.split(A);
        String before;
        int beforeLength = emailArr[0].length();
        if (beforeLength > 3) {
            before = emailArr[0].substring(0, 3) + XXX;
        } else if (beforeLength >= 1) {
            before = emailArr[0].charAt(0) + XXX;
        } else {
            before = emailArr[0];
        }
        return before + A + XXX;
    }

    /**
     * 电话、手机、身份证件号、银行卡号，支付账号，
     * 10位以上，保留前三后四,
     * 10位及10位以下，保留后四位
     * ，如：137****1234，441***********0021，******6666。
     *
     * @param number 数字
     * @return 数字密文
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
     * @return 姓名密文
     */
    public static String hideUserName(String username) {
        if (StringUtils.isBlank(username)) {
            return username;
        }
        return username.charAt(0) + XXX;
    }

    /**
     * 地址脱敏
     *
     * @param address 地址
     * @return 地址密文
     */
    public static String hideAddress(String address) {
        if (StringUtils.isBlank(address)) {
            return address;
        }
        boolean hasProvince = address.contains(PROVINCE);
        if (hasProvince) {
            String[] addressArray = address.split(PROVINCE);
            return addressArray[0] + PROVINCE + XXX;
        }

        boolean hasCity = address.contains(CITY);
        if (hasCity) {
            String[] addressArray = address.split(CITY);
            return addressArray[0] + CITY + XXX;
        }
        return "";
    }

    /**
     * 公司名称脱敏
     *
     * @param companyName 公司名称
     * @return 公司密文
     */
    public static String hideCompanyName(String companyName) {
        if (StringUtils.isBlank(companyName)) {
            return companyName;
        }
        boolean hasProvince = companyName.contains(PROVINCE);
        if (hasProvince) {
            String[] companyArray = companyName.split(PROVINCE);
            return XXX + companyArray[1].substring(0, 2) + XXX;
        }

        boolean hasCity = companyName.contains(CITY);
        if (hasCity) {
            String[] companyArray = companyName.split(CITY);
            return XXX + companyArray[1].substring(0, 2) + XXX;
        }
        return XXX + companyName.substring(0, 2) + XXX;
    }

}
