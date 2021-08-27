package com.yizhishang.oauth.interceptor;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

/**
 * 自定义资源权限访问管理器
 *
 * @author yizhishang
 * @since 2020/1/3 15:50
 */
@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {

    /**
     * 判断是否有权限
     *
     * @param auth
     * @param object
     * @param configAttributes 由CustomFilterInvocationSecurityMetadataSource.getAttributes(object)返回的请求的资源（url）所需要的权限（角色）集合
     */
    @Override
    public void decide(Authentication auth, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException {
        // 如果请求的资源不需要权限，则直接放行
        if (configAttributes == null || configAttributes.isEmpty()) {
            return;
        }

        // 判断用户所拥有的权限是否是资源所需要的权限之一，如果是则放行，否则拦截
        Iterator<ConfigAttribute> iter = configAttributes.iterator();
        while (iter.hasNext()) {
            String needRole = iter.next().getAttribute();
            for (GrantedAuthority grantRole : auth.getAuthorities()) {
                if (needRole.trim().equals(grantRole.getAuthority().trim())) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("access denied");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}