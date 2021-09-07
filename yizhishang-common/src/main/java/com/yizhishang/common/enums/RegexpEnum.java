package com.yizhishang.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 正则式枚举
 *
 * @author yizhishang
 * @since 2020/9/15 11:28
 */
@Getter
@AllArgsConstructor
public enum RegexpEnum {

    /**
     * 手机号码
     */
    MOBILE_PHONE("^1[3|4|5|7|8|9]\\d{9}$");

    private final String regexp;
}
