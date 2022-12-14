package com.yizhishang.common.response;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yizhishang.common.enums.CommonEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author yizhishang
 */
@Slf4j
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

    private static ResourceBundle zh;

    private static ResourceBundle en;

    static {
        try {
            zh = ResourceBundle.getBundle("i18n.message", new Locale("zh", "CN"));
            en = ResourceBundle.getBundle("i18n.message", new Locale("en", "US"));
        } catch (MissingResourceException e) {
            log.error("ResponseData初始化失败", e);
        }
    }

    protected ResponseData() {
        this(true, DEFAULT_SUCCESS_CODE, "default.success.message");
    }

    public ResponseData(T data) {
        this(true, DEFAULT_SUCCESS_CODE, "default.success.message");
        this.data = data;
    }

    public ResponseData(Boolean success, Integer code, String messageKey, T data) {
        this(success, code, messageKey);
        this.data = data;
    }

    public ResponseData(Boolean success, Integer code, String messageKey) {
        this.success = success;
        this.code = code;
        this.messageKey = messageKey;
        if (messageKey != null) {
            try {
                this.zhMessage = zh.getString(messageKey);
                this.enMessage = en.getString(messageKey);
            } catch (Exception e) {
                log.error("ResponseData构造方法失败: {}", messageKey, e);
                this.zhMessage = messageKey;
                this.enMessage = messageKey;
            }
        }
    }

    public static ResponseData success() {
        return new ResponseData();
    }

    public static ResponseData success(Object object) {
        return new ResponseData(object);
    }

    public static ResponseData success(Integer code, String messageKey, Object object) {
        return new ResponseData(true, code, messageKey, object);
    }

    /**
     * 异常枚举返回
     *
     * @param commonEnum
     * @return
     */
    public static ResponseData error(CommonEnum commonEnum) {
        return new ResponseData(false, commonEnum.getCode(), commonEnum.getMessage());
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

    public static ResponseListData successList(List<?> data) {
        return new ResponseListData(data);
    }

    public static ResponsePageData successPage(IPage<?> data) {
        return new ResponsePageData((Page) data);
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

    @Override
    public String toString() {
        return "ResponseData(success=" + this.getSuccess() + ", code=" + this.getCode() + ", messageKey=" + this.getMessageKey() + ", zhMessage=" + this.getZhMessage() + ", enMessage=" + this.getEnMessage() + ", data=" + this.getData() + ")";
    }
}
