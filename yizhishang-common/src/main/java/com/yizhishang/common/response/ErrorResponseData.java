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
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ErrorResponseData)) {
            return false;
        }
        ErrorResponseData other = (ErrorResponseData) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object otherExceptionClazz = other.getExceptionClass();
        if (exceptionClass == null) {
            if (otherExceptionClazz != null) {
                return false;
            }
        } else if (!exceptionClass.equals(otherExceptionClazz)) {
            return false;
        }

        return true;
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof ErrorResponseData;
    }

    @Override
    public int hashCode() {
        return 59 + (exceptionClass == null ? 43 : exceptionClass.hashCode());
    }

    @Override
    public String toString() {
        return "ErrorResponseData(exceptionClazz=" + this.getExceptionClass() + ")";
    }
}
