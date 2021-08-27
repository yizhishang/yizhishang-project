package com.yizhishang.oauth.interceptor;

import com.alibaba.fastjson.JSON;
import com.yizhishang.common.enums.CommonEnum;
import com.yizhishang.common.response.ResponseData;
import com.yizhishang.oauth.config.ResourceIgnoreConfig;
import com.yizhishang.oauth.exception.AuthExceptionEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yizhishang
 * @Description 解决(忽略认证接口header带invalid token报401)问题
 * @since 2020/4/27 11:18
 */
@Component
public class PermitAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(PermitAuthenticationFilter.class);

    private TokenExtractor tokenExtractor = new BearerTokenExtractor();

    private static final String PARAM_ACCESS_TOKEN = "access_token ";
    private static final String HEADER_AUTHORIZATION = "Authorization";

    private boolean stateless = true;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ResourceIgnoreConfig resourceIgnoreConfig;

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        for (String url : resourceIgnoreConfig.getIgnore()) {
            if (new AntPathRequestMatcher(url).matches(request)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        Authentication authentication = this.tokenExtractor.extract(request);
        if (authentication == null) {
            if (this.stateless && this.isAuthenticated()) {
                // SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = request.getParameter(PARAM_ACCESS_TOKEN);
        String headerToken = request.getHeader(HEADER_AUTHORIZATION);
        CommonEnum commonEnum = null;
        if (StringUtils.isNotBlank(accessToken)) {
            try {
                OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(accessToken);
                logger.debug("token = {}", oAuth2AccessToken.getValue());
            } catch (InvalidTokenException e) {
                logger.error("************ token校验失败: {}", accessToken);
            }
        } else if (StringUtils.isNotBlank(headerToken) && headerToken.startsWith(OAuth2AccessToken.BEARER_TYPE)) {
            try {
                String token = headerToken.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
                OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
                logger.debug("token = {}", oAuth2AccessToken.getValue());
            } catch (InvalidTokenException e) {
                logger.error("************ token校验失败: {}", headerToken);
            }
        } else {
            logger.error("** 参数无token. ** ");
            commonEnum = AuthExceptionEnum.INSUFFICIENT_PERMISSIONS;
        }

        if (commonEnum != null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(JSON.toJSONString(ResponseData.error(commonEnum)));
        }
        filterChain.doFilter(request, response);
    }

}
