package com.yizhishang.oauth.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yizhishang.core.mybatis.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 实体类
 *
 * @author yizhishang
 * @date 2020-11-10 13:46
 */
@Data
@TableName("um_user")
@ApiModel(value = "UmUser对象", description = "")
public class UmUser extends BaseEntity implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "account")
    private String account;

    @TableField(value = "description")
    private String description;

    @TableField(value = "password")
    private String password;

    @TableField(value = "name")
    private String name;


}

