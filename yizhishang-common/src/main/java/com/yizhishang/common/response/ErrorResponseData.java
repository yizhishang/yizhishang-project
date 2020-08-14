package com.yizhishang.common.response;

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
                Object this$exceptionClazz = this.getExceptionClazz();
                Object other$exceptionClazz = other.getExceptionClazz();
                if (this$exceptionClazz == null) {
                    if (other$exceptionClazz != null) {
                        return false;
                    }
                } else if (!this$exceptionClazz.equals(other$exceptionClazz)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof ErrorResponseData;
    }

    public int hashCode() {
        int result = 1;
        Object $exceptionClazz = this.getExceptionClazz();
        result = result * 59 + ($exceptionClazz == null ? 43 : $exceptionClazz.hashCode());
        return result;
    }

    public String toString() {
        return "ErrorResponseData(exceptionClazz=" + this.getExceptionClazz() + ")";
    }
}
