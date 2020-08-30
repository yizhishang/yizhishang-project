package com.yizhishang.common.response;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yizhishang
 */
@Data
@ApiModel("通用Response对象")
public class ResponsePageData<T> extends ResponseData {

    @ApiModelProperty("分页响应对象")
    private Page<T> data;

    private ResponsePageData() {
    }

    private ResponsePageData(Page<T> data) {
        super(true, DEFAULT_SUCCESS_CODE, "default.success.message");
        this.data = data;
    }

    @Override
    public Page<T> getData() {
        return this.data;
    }

    public static ResponsePageData successPage(IPage<?> data) {
        return new ResponsePageData((Page)data);
    }

}
