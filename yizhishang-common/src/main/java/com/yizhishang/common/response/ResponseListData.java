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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResponseListData)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        ResponseListData<?> that = (ResponseListData<?>) o;

        return getData() != null ? getData().equals(that.getData()) : that.getData() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getData() != null ? getData().hashCode() : 0);
        return result;
    }
}
