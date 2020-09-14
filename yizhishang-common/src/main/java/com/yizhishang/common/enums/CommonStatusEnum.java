package com.yizhishang.common.enums;

import lombok.Getter;

/**
 * 公共状态
 *
 * @author yizhishang
 * @date 2020-01-17 14:00
 */
@Getter
public enum CommonStatusEnum implements CommonEnum {

    /**
     * 启用
     */
    ENABLE(0, "启用"),
    /**
     * 禁用
     */
    DISABLE(1, "禁用");

    Integer code;
    String message;

    CommonStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getDescription(Integer status) {
        if (status != null) {
            for (CommonStatusEnum s : CommonStatusEnum.values()) {
                if (s.getCode().equals(status)) {
                    return s.getMessage();
                }
            }
        }
        return "";
    }
}
