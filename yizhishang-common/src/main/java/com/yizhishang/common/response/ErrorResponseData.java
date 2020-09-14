package com.yizhishang.common.response;

/**
 * @author yizhishang
 */
public class ErrorResponseData extends ResponseData {
    private String exceptionClazz;

    public ErrorResponseData(String messageKey) {
        super(false, ResponseData.DEFAULT_ERROR_CODE, messageKey, (Object) null);
    }

    public ErrorResponseData(Integer code, String messageKey) {
        super(false, code, messageKey, (Object) null);
    }

    public ErrorResponseData(Integer code, String messageKey, Object object) {
        super(false, code, messageKey, object);
    }

    public ErrorResponseData(Integer code, String messageKey, String exceptionClazz) {
        super(false, code, messageKey, exceptionClazz);
    }

    public String getExceptionClazz() {
        return this.exceptionClazz;
    }

    public void setExceptionClazz(String exceptionClazz) {
        this.exceptionClazz = exceptionClazz;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ErrorResponseData)) {
            return false;
        } else {
            ErrorResponseData other = (ErrorResponseData) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object exceptionClazz = this.getExceptionClazz();
                Object otherExceptionClazz = other.getExceptionClazz();
                if (exceptionClazz == null) {
                    if (otherExceptionClazz != null) {
                        return false;
                    }
                } else if (!exceptionClazz.equals(otherExceptionClazz)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof ErrorResponseData;
    }

    @Override
    public int hashCode() {
        int result = 1;
        Object exceptionClazz = this.getExceptionClazz();
        result = result * 59 + (exceptionClazz == null ? 43 : exceptionClazz.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "ErrorResponseData(exceptionClazz=" + this.getExceptionClazz() + ")";
    }
}
