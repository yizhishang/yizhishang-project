package com.yizhishang.common.response;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author yizhishang
 */
@ApiModel("通用ResponsePageData对象")
public class ResponsePageData<T> extends ResponseData<Page<T>> {

    @ApiModelProperty("分页响应对象")
    private Page<T> data;

    @Override
    public Page<T> getData() {
        return data;
    }

    public ResponsePageData(Page<T> data) {
        super();
        this.data = data;
    }

}
