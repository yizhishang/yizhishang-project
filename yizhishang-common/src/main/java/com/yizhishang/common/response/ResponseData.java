package com.yizhishang.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author yizhishang
 */
@ApiModel("通用Response对象")
public class ResponseData<T> {

    public static final String DEFAULT_SUCCESS_MESSAGE = "default.success.message";
    public static final String DEFAULT_ERROR_MESSAGE = "default.error.message";
    public static final Integer DEFAULT_SUCCESS_CODE = 200;
    public static final Integer DEFAULT_ERROR_CODE = 500;

    @ApiModelProperty("请求是否成功")
    private Boolean success;

    @ApiModelProperty("响应状态码")
    private Integer code;

    @ApiModelProperty("message在国际化配置文件中的key")
    private String messageKey;

    @ApiModelProperty("响应信息（zh）")
    private String zhMessage;

    @ApiModelProperty("响应信息（en）")
    private String enMessage;

    @ApiModelProperty("响应对象")
    private T data;

    public ResponseData() {
    }

    public ResponseData(Boolean success, Integer code, String messageKey) {
        this.success = success;
        this.code = code;
        this.messageKey = messageKey;
        if (messageKey != null) {
            ResourceBundle zh = ResourceBundle.getBundle("i18n.message", new Locale("zh", "CN"));
            ResourceBundle en = ResourceBundle.getBundle("i18n.message", new Locale("en", "US"));

            try {
                this.zhMessage = zh.getString(messageKey);
                this.enMessage = en.getString(messageKey);
            } catch (MissingResourceException var8) {
                this.zhMessage = messageKey;
                this.enMessage = messageKey;
            }
        }
    }

    public ResponseData(Boolean success, Integer code, String messageKey, T data) {
        this(success, code, messageKey);
        this.data = data;
    }

    public static SuccessResponseData success() {
        return new SuccessResponseData();
    }

    public static SuccessResponseData success(Object object) {
        return new SuccessResponseData(object);
    }

    public static SuccessResponseData success(Integer code, String messageKey, Object object) {
        return new SuccessResponseData(code, messageKey, object);
    }

    public static ErrorResponseData error(String messageKey) {
        return new ErrorResponseData(messageKey);
    }

    public static ErrorResponseData error(Integer code, String messageKey) {
        return new ErrorResponseData(code, messageKey);
    }

    public static ErrorResponseData error(Integer code, String messageKey, Object object) {
        return new ErrorResponseData(code, messageKey, object);
    }

    public static ErrorResponseData error(Integer code, String messageKey, String exceptionClass) {
        return new ErrorResponseData(code, messageKey, exceptionClass);
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessageKey() {
        return this.messageKey;
    }

    public String getZhMessage() {
        return this.zhMessage;
    }

    public String getEnMessage() {
        return this.enMessage;
    }

    public T getData() {
        return this.data;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public void setZhMessage(String zhMessage) {
        this.zhMessage = zhMessage;
    }

    public void setEnMessage(String enMessage) {
        this.enMessage = enMessage;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ResponseData)) {
            return false;
        }
        ResponseData<?> other = (ResponseData) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object otherSuccess = other.getSuccess();
        if (success == null && otherSuccess != null) {
            return false;
        } else if (!success.equals(otherSuccess)) {
            return false;
        }

        Object otherCode = other.getCode();
        if (code == null && otherCode != null) {
            return false;
        } else if (!code.equals(otherCode)) {
            return false;
        }

        Object otherMessageKey = other.getMessageKey();
        if (messageKey == null && otherMessageKey != null) {
            return false;
        } else if (!messageKey.equals(otherMessageKey)) {
            return false;
        }

        label62:
        {
            Object otherZhMessage = other.getZhMessage();
            if (zhMessage == null && otherZhMessage == null) {
                break label62;
            } else if (zhMessage.equals(otherZhMessage)) {
                break label62;
            }

            return false;
        }

        label55:
        {
            Object otherEnMessage = other.getEnMessage();
            if (enMessage == null && otherEnMessage == null) {
                break label55;
            } else if (enMessage.equals(otherEnMessage)) {
                break label55;
            }

            return false;
        }

        Object otherData = other.getData();
        if (data == null && otherData != null) {
            return false;
        } else if (!data.equals(otherData)) {
            return false;
        }

        return true;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ResponseData;
    }

    @Override
    public int hashCode() {
        int result = 1;
        Object success = this.getSuccess();
        result = result * 59 + (success == null ? 43 : success.hashCode());
        Object code = this.getCode();
        result = result * 59 + (code == null ? 43 : code.hashCode());
        Object messageKey = this.getMessageKey();
        result = result * 59 + (messageKey == null ? 43 : messageKey.hashCode());
        Object zhMessage = this.getZhMessage();
        result = result * 59 + (zhMessage == null ? 43 : zhMessage.hashCode());
        Object enMessage = this.getEnMessage();
        result = result * 59 + (enMessage == null ? 43 : enMessage.hashCode());
        Object data = this.getData();
        result = result * 59 + (data == null ? 43 : data.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "ResponseData(success=" + this.getSuccess() + ", code=" + this.getCode() + ", messageKey=" + this.getMessageKey() + ", zhMessage=" + this.getZhMessage() + ", enMessage=" + this.getEnMessage() + ", data=" + this.getData() + ")";
    }
}
