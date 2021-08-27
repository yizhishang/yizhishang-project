package com.yizhishang.oauth.client.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 实体类
 *
 * @author yizhishang
 * @date 2020-11-05 15:33
 */
@Data
@TableName("oauth_client_details")
@ApiModel(value = "OauthClientDetails对象", description = "")
public class OauthClientDetails {

    @TableId("client_id")
    private String clientId;

    @TableField(value = "resource_ids")
    private String resourceIds;

    @TableField(value = "client_secret")
    private String clientSecret;

    @TableField(value = "scope")
    private String scope;

    @TableField(value = "authorized_grant_types")
    private String authorizedGrantTypes;

    @TableField(value = "web_server_redirect_uri")
    private String webServerRedirectUri;

    @TableField(value = "authorities")
    private String authorities;

    @TableField(value = "access_token_validity")
    private Integer accessTokenValidity;

    @TableField(value = "refresh_token_validity")
    private Integer refreshTokenValidity;

    @TableField(value = "additional_information")
    private String additionalInformation;

    @TableField(value = "autoapprove")
    private String autoapprove;
}

