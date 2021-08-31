package com.yizhishang.oauth.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * 自定义的权限验证过滤器
 *
 * @author yizhishang
 * @since 2020/1/3 15:58
 */
@Component
public class CustomFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

    private static final Logger log = LoggerFactory.getLogger(CustomFilterSecurityInterceptor.class);

    /**
     * 注入自定义的资源（url）权限（角色）获取类
     */
    @Autowired
    private FilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;

    /**
     * 注入自定义的权限验证管理器
     */
    @Autowired
    public void setAccessDecisionManager(CustomAccessDecisionManager customAccessDecisionManager) {
        super.setAccessDecisionManager(customAccessDecisionManager);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        FilterInvocation invocation = new FilterInvocation(request, response, chain);
        InterceptorStatusToken token = super.beforeInvocation(invocation);
        try {
            // 执行下一个拦截器
            invocation.getChain().doFilter(invocation.getRequest(), invocation.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }

    @Override
    public void destroy() {
        log.debug("destroy method");
    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.customFilterInvocationSecurityMetadataSource;
    }

}