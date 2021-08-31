package com.yizhishang.common.enums;

/**
 * 正则式枚举
 *
 * @author yizhishang
 * @since 2020/9/15 11:28
 */
public enum RegexpEnum {

    /**
     * 手机号码
     */
    MOBILE_PHONE("^1[3|4|5|7|8|9]\\d{9}$");

    private String regexp;

    RegexpEnum(String regexp) {
        this.regexp = regexp;
    }

    public String getRegexp() {
        return regexp;
    }
}
