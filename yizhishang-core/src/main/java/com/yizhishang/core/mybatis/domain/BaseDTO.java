package com.yizhishang.core.mybatis.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseDTO implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "主键id")
    private Long id;

    @Length(max = 255, message = "创建人长度不能超过255个字符")
    @ApiModelProperty(value = "创建人")
    private String createBy;

    @Length(max = 255, message = "修改人长度不能超过255个字符")
    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "乐观锁")
    private Integer version;

    @ApiModelProperty(value = "逻辑删除标识，0：未删除，1：已删除")
    private Integer deleteMark;

    @Length(max = 64, message = "租户code长度不能超过64个字符")
    @ApiModelProperty(value = "租户code")
    private String tenantCode;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer pageNum;

    @ApiModelProperty(value = "页大小", example = "10")
    private Integer pageSize;

}
