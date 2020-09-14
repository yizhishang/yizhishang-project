package com.yizhishang.common.exception;

import com.yizhishang.common.enums.CommonEnum;

import java.io.Serializable;

/**
 * @author yizhishang
 */
public class BizException extends ServiceException implements Serializable {

    public BizException(CommonEnum exception) {
        super(exception);
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(Integer code, String errorMessage) {
        super(code, errorMessage);
    }
}
