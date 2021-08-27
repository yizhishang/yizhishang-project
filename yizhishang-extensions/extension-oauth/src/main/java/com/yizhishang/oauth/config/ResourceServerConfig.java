package com.yizhishang.oauth.config;

import com.yizhishang.oauth.exception.CustomAuthExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

/**
 * 配置资源服务器
 *
 * @author yizhishang
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private ResourceIgnoreConfig resourceIgnoreConfig;

    @Autowired
    private PermitAllSecurityConfig permitAllSecurityConfig;

    @Autowired
    private DefaultTokenServices tokenServices;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        //加入自定义异常
        resources.resourceId("authorize-server")
                .authenticationEntryPoint(new CustomAuthExceptionHandler())
                .accessDeniedHandler(new CustomAuthExceptionHandler())
                .stateless(true).tokenServices(tokenServices);
    }

    /**
     * 配置 URL 访问权限
     *
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        String[] urls = resourceIgnoreConfig.getIgnore().stream().distinct().toArray(String[]::new);
        http
                .authorizeRequests()
                .and()
                .apply(permitAllSecurityConfig)
                .and().authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(urls).permitAll()
                .anyRequest().authenticated();
    }
}