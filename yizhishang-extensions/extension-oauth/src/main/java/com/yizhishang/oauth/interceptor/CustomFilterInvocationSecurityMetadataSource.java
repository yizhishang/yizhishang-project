package com.yizhishang.oauth.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.yizhishang.oauth.config.ResourceIgnoreConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author yizhishang
 * @since 2020/1/3 15:55
 */
@Component
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private static final Logger log = LoggerFactory.getLogger(CustomFilterInvocationSecurityMetadataSource.class);

    private static final String UNKNOWN_ROLE = "unknownRole";

    @Autowired
    private ResourceIgnoreConfig resourceIgnoreConfig;

    /**
     * 获取用户请求的某个具体的资源（url）所需要的权限（角色）集合
     *
     * @param object 包含了用户请求的request信息
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) {
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        if (request.getMethod().equals(HttpMethod.OPTIONS.toString())) {
            return Collections.emptyList();
        }

        // 忽略的资源不走权限控制
        List<String> ignoreUrls = new ArrayList<>();
        ignoreUrls.addAll(resourceIgnoreConfig.getIgnore());
        for (String url : ignoreUrls) {
            if (StrUtil.isNotBlank(url) && new AntPathRequestMatcher(url).matches(request)) {
                return Collections.emptyList();
            }
        }

        // 遍历每个资源（url），如果与用户请求的资源（url）匹配，则返回该资源（url）所需要的权限（角色）集合
        List<ConfigAttribute> roles = new ArrayList<>();
        HashMap<String, Collection<ConfigAttribute>> map = getResourcePermission();
        Iterator<String> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String url = iter.next();
            if (StrUtil.isNotBlank(url) && new AntPathRequestMatcher(url).matches(request)) {
                roles.addAll(map.get(url));
            }
        }
        if (CollUtil.isNotEmpty(roles)) {
            return roles;
        }
        // 没有匹配到资源对应角色,给定一个unknownRole
        List<ConfigAttribute> unknownRole = new ArrayList<>();
        unknownRole.add(new SecurityConfig(UNKNOWN_ROLE));
        return unknownRole;
    }

    /**
     * 加载角色资源, 配置成这种样子
     * map.put("/user/*", adminUrlRoles);
     * map.put("/role/*", userUrlRoles);
     */
    private HashMap<String, Collection<ConfigAttribute>> getResourcePermission() {
        HashMap<String, Collection<ConfigAttribute>> map = Maps.newHashMap();

        // 设置资源（url）所需要的权限（角色）集合
        List<ConfigAttribute> urlRoles = new ArrayList<>();
        urlRoles.add(new SecurityConfig("admin"));

        map.put("/admin/*", urlRoles);
        return map;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return Collections.emptyList();
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
