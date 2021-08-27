package com.yizhishang.oauth.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yizhishang.oauth.client.entity.OauthClientDetails;

/**
 *  服务类
 *
 * @author yizhishang
 * @date 2020-11-05 15:33
 */
public interface OauthClientDetailsService extends IService<OauthClientDetails> {

   OauthClientDetails queryByClientId(String clientId);

   OauthClientDetails insert(OauthClientDetails oauthClientDetails);

   OauthClientDetails modify(OauthClientDetails oauthClientDetails);

   void delete(Long Id);

}
