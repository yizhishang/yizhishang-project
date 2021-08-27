package com.yizhishang.oauth.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.yizhishang.core.mybatis.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色信息实体类
 *
 * @author yizhishang
 * @date 2020-11-10 13:45
 */
@Data
@TableName("um_role")
@ApiModel(value = "UmRole对象", description = "角色信息")
public class UmRole extends BaseEntity implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "description")
    private String description;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableField(value = "created_time")
    private Long createdTime;

    @TableField(value = "name")
    private String name;

    @TableField(value = "role")
    private String role;


}

