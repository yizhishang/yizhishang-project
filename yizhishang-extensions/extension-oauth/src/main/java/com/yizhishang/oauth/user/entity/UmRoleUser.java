package com.yizhishang.oauth.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yizhishang.core.mybatis.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 实体类
 *
 * @author yizhishang
 * @since 2020-11-10 13:48
 */
@Data
@TableName("um_role_user")
@ApiModel(value = "UmRoleUser对象", description = "")
public class UmRoleUser extends BaseEntity implements Serializable {

    @TableField(value = "role_id")
    private Long roleId;

    @TableField(value = "user_id")
    private Integer userId;


}

