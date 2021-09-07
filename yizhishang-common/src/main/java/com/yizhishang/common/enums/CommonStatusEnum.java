package com.yizhishang.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 公共状态
 *
 * @author yizhishang
 * @since 2020-01-17 14:00
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum implements CommonEnum {

    /**
     * 启用
     */
    ENABLE(0, "启用"),
    /**
     * 禁用
     */
    DISABLE(1, "禁用");

    private final Integer code;
    private final String message;

    public static CommonStatusEnum getCommonStatus(Integer status) {
        if (status != null) {
            for (CommonStatusEnum s : CommonStatusEnum.values()) {
                if (s.getCode().equals(status)) {
                    return s;
                }
            }
        }
        return null;
    }
}
