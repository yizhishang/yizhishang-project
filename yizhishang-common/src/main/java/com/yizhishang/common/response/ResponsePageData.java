package com.yizhishang.common.response;

import com.baomidou.mybatisplus.core.metadata.IPage;
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

    private ResponsePageData() {
    }

    @Override
    public Page<T> getData() {
        return data;
    }

    private ResponsePageData(Page<T> data) {
        super(true, DEFAULT_SUCCESS_CODE, "default.success.message");
        this.data = data;
    }

    public static ResponsePageData successPage(IPage<?> data) {
        return new ResponsePageData((Page) data);
    }

}
