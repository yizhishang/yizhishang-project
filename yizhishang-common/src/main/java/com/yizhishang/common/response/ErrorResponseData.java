package com.yizhishang.common.response;

/**
 * @author yizhishang
 */
public class ErrorResponseData extends ResponseData {
    private String exceptionClass;

    public ErrorResponseData(String messageKey) {
        super(false, ResponseData.DEFAULT_ERROR_CODE, messageKey, null);
    }

    public ErrorResponseData(Integer code, String messageKey) {
        super(false, code, messageKey, null);
    }

    public ErrorResponseData(Integer code, String messageKey, Object object) {
        super(false, code, messageKey, object);
    }

    public ErrorResponseData(Integer code, String messageKey, String exceptionClass) {
        super(false, code, messageKey, exceptionClass);
    }

    public String getExceptionClass() {
        return this.exceptionClass;
    }

    @Override
    public String toString() {
        return "ErrorResponseData(exceptionClazz=" + getExceptionClass() + ")";
    }
}
