package com.yizhishang.oauth.exception;

import com.yizhishang.common.enums.CommonEnum;

/**
 * @author yizhishang
 * @since: 2020/11/10 14:12
 */
public enum AuthExceptionEnum implements CommonEnum {

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "unauthorized"),
    /**
     * token无效
     */
    ACCESS_TOKEN_INVALID(401, "access_token_invalid"),
    /**
     * 权限不足
     */
    INSUFFICIENT_PERMISSIONS(401, "insufficient_permissions"),

    ;

    private Integer code;
    private String message;

    AuthExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
