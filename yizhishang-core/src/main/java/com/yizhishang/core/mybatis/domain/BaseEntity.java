package com.yizhishang.core.mybatis.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Data
public class BaseEntity {

    @ApiModelProperty(value = "主键id")
    @TableId("ID")
    private Long id;

    @Length(max = 255, message = "创建人长度不能超过255个字符")
    @ApiModelProperty(value = "创建人")
    @TableField(value = "CREATE_BY", fill= FieldFill.INSERT)
    private String createBy;

    @Length(max = 255, message = "修改人长度不能超过255个字符")
    @ApiModelProperty(value = "修改人")
    @TableField(value = "UPDATE_BY")
    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    @TableField(value = "CREATE_TIME", fill= FieldFill.INSERT)
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "修改时间")
    @TableField(value = "UPDATE_TIME", fill= FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Version
    @TableField(value = "VERSION", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "乐观锁")
    private Integer version;

    @TableLogic
    @ApiModelProperty(value = "逻辑删除标识，0：未删除，1：已删除")
    private Integer deleteMark;

    @Length(max = 64, message = "租户code长度不能超过64个字符")
    @ApiModelProperty(value = "租户code")
    @TableField(value = "TENANT_CODE")
    private String tenantCode;
}
