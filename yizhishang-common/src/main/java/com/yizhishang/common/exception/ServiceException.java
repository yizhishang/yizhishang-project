package com.yizhishang.common.exception;

import com.yizhishang.common.enums.BizExceptionEnum;
import com.yizhishang.common.enums.CommonEnum;

/**
 * @author yizhishang
 */
public class ServiceException extends RuntimeException {
    private final Integer code;
    private final String errorMessage;

    public ServiceException(String errorMessage) {
        super(errorMessage);
        this.code = BizExceptionEnum.SERVER_ERROR.getCode();
        this.errorMessage = errorMessage;
    }

    public ServiceException(Integer code, String errorMessage) {
        super(errorMessage);
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public ServiceException(CommonEnum exception) {
        super(exception.getMessage());
        this.code = exception.getCode();
        this.errorMessage = exception.getMessage();
    }

    public Integer getCode() {
        return this.code;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

}
