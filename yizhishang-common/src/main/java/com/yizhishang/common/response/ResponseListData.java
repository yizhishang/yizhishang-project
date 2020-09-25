package com.yizhishang.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author yizhishang
 */
@ApiModel("通用ResponseListData对象")
public class ResponseListData<T> extends ResponseData<List<T>> {

    @ApiModelProperty("list响应对象")
    private List<T> data;

    @Override
    public List<T> getData() {
        return data;
    }

    public ResponseListData(List<T> data) {
        super();
        this.data = data;
    }

}
