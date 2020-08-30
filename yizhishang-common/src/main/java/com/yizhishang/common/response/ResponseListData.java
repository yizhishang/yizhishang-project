package com.yizhishang.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("通用Response对象")
public class ResponseListData<T> extends ResponseData {

    @ApiModelProperty("list响应对象")
    private List<T> data;

    public ResponseListData() {
    }

    private ResponseListData(List<T> data) {
        this(true, DEFAULT_SUCCESS_CODE, "default.success.message", data);
    }

    public ResponseListData(Boolean success, Integer code, String messageKey, List<T> data) {
        super(success, code, messageKey, data);
        this.data = data;
    }

    @Override
    public List<T> getData() {
        return this.data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public static ResponseListData successList(Object object) {
        return new ResponseListData((List)object);
    }

}
