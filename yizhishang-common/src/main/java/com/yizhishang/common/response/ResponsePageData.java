package com.yizhishang.common.response;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("通用Response对象")
public class ResponsePageData<T> extends ResponseData {

    @ApiModelProperty("分页响应对象")
    private Page<T> data;

    public ResponsePageData() {
    }

    private ResponsePageData(Page<T> data) {
        this(true, DEFAULT_SUCCESS_CODE, "default.success.message", data);
        this.data = data;
    }

    public ResponsePageData(Boolean success, Integer code, String messageKey, Page<T> data) {
        super(success, code, messageKey);
        this.data = data;
    }

    @Override
    public Page<T> getData() {
        return this.data;
    }

    public void setData(Page<T> data) {
        this.data = data;
    }

    public static ResponsePageData successPage(Object object) {
        return new ResponsePageData(true, DEFAULT_SUCCESS_CODE, "default.success.message", (Page)object);
    }

}
