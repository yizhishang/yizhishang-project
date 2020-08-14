package com.yizhishang.common.exception;

import com.yizhishang.common.enums.AbstractBaseExceptionEnum;

import java.io.Serializable;

public class BizException extends ServiceException implements Serializable {
    public BizException(AbstractBaseExceptionEnum exception) {
        super(exception);
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(Integer code, String errorMessage) {
        super(code, errorMessage);
    }
}
