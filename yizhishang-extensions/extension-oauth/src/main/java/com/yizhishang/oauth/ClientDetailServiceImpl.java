package com.yizhishang.oauth;

import com.yizhishang.oauth.client.entity.CustomClientDetails;
import com.yizhishang.oauth.client.entity.OauthClientDetails;
import com.yizhishang.oauth.client.service.OauthClientDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

/**
 * 自定义客户端
 *
 * @author yizhishang
 */
@Service
public class ClientDetailServiceImpl implements ClientDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(ClientDetailServiceImpl.class);

    @Autowired
    private OauthClientDetailsService clientDetailsService;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        OauthClientDetails clientDetails = clientDetailsService.queryByClientId(clientId);
        if (clientDetails == null) {
            throw new ClientRegistrationException("client认证失败");
        }
        return new CustomClientDetails(clientDetails);
    }
}

