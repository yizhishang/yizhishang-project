package com.yizhishang.common.response;

/**
 * @author yizhishang
 */
public class SuccessResponseData extends ResponseData {
    public SuccessResponseData() {
        super();
    }

    public SuccessResponseData(Object object) {
        super(object);
    }

    public SuccessResponseData(Integer code, String messageKey, Object object) {
        super(true, code, messageKey, object);
    }
}
