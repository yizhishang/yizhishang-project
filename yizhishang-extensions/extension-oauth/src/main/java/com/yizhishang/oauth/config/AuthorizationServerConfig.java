package com.yizhishang.oauth.config;

import com.yizhishang.oauth.ClientDetailServiceImpl;
import com.yizhishang.oauth.CustomUserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 配置授权服务器
 *
 * @author yizhishang
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 密码模式授权模式
     */
    private static final String GRANT_TYPE_PASSWORD = "password";
    /**
     * 授权码模式  授权码模式使用到了回调地址，是最为复杂的方式，通常网站中经常出现的微博，qq第三方登录，都会采用这个形式。
     */
    private static final String AUTHORIZATION_CODE = "authorization_code";
    private static final String REFRESH_TOKEN = "refresh_token";
    /**
     * 简化授权模式
     */
    private static final String IMPLICIT = "implicit";
    /**
     * 客户端模式
     */
    private static final String GRANT_TYPE = "client_credentials";
    private static final String SCOPE_READ = "read";
    private static final String SCOPE_WRITE = "write";

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * 该对象用来支持 password 模式
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 该对象将为刷新token提供支持
     */
    @Autowired
    private CustomUserDetailServiceImpl userDetailsService;

    @Autowired
    private ClientDetailServiceImpl clientDetailService;

    @Autowired
    private WebResponseExceptionTranslator webResponseExceptionTranslator;

    /**
     * 配置 password 授权模式
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //配置认证客户端
        clients.withClientDetails(clientDetailService);

    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        //配置令牌的存储（这里存放在内存中）: inMemoryTokenStore
        //配置令牌存放在Redis中
        //必须注入userDetailsService否则根据refresh_token无法加载用户信息
        endpoints.tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .exceptionTranslator(webResponseExceptionTranslator)
                //开启刷新token
                .reuseRefreshTokens(true)
                .tokenServices(tokenServices());
    }

    private RedisTokenStore getRedisTokenStore() {
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setPrefix("oauth:");
        return redisTokenStore;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        // 表示支持 client_id 和 client_secret 做登录认证
        security.allowFormAuthenticationForClients();
    }

    @Bean
    public TokenStore tokenStore() {
        //基于jwt实现令牌（Access Token）
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 重写默认的资源服务token
     *
     * @return
     */
    @Bean
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        // 针对jwt令牌的添加
        defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter());

        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        // 30天
        defaultTokenServices.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30));
        return defaultTokenServices;
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter() {
            /**
             * 自定义一些token返回的信息
             * @param accessToken
             * @param authentication
             * @return OAuth2AccessToken
             */
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                String grantType = authentication.getOAuth2Request().getGrantType();
                //只有如下两种模式才能获取到当前用户信息
                if (AUTHORIZATION_CODE.equals(grantType) || GRANT_TYPE_PASSWORD.equals(grantType)) {
                    String userName = authentication.getUserAuthentication().getName();
                    // 自定义一些token 信息 会在获取token返回结果中展示出来
                    final Map<String, Object> additionalInformation = new HashMap<>(16);
                    additionalInformation.put("username", userName);
                    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
                }
                return super.enhance(accessToken, authentication);
            }
        };
        converter.setSigningKey("test123");
        converter.setVerifier(new MacSigner("test123"));
        return converter;
    }
}