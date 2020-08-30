package com.yizhishang.common.response;

/**
 * @author yizhishang
 */
public class SuccessResponseData extends ResponseData {
    public SuccessResponseData() {
        super(true, DEFAULT_SUCCESS_CODE, "default.success.message", (Object) null);
    }

    public SuccessResponseData(Object object) {
        super(true, DEFAULT_SUCCESS_CODE, "default.success.message", object);
    }

    public SuccessResponseData(Integer code, String messageKey, Object object) {
        super(true, code, messageKey, object);
    }
}
