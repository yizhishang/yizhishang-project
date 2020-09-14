package com.yizhishang.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yizhishang
 */
@Data
@ApiModel("通用ResponseListData对象")
public class ResponseListData<T> extends ResponseData<List<T>> {

    @ApiModelProperty("list响应对象")
    private List<T> data;

    private ResponseListData() {
    }

    @Override
    public List<T> getData() {
        return data;
    }

    private ResponseListData(List<T> data) {
        super(true, DEFAULT_SUCCESS_CODE, "default.success.message");
        this.data = data;
    }

    public static ResponseListData successList(List<?> data) {
        return new ResponseListData(data);
    }

}
