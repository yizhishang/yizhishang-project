package com.yizhishang.oauth.exception;

import com.alibaba.fastjson.JSON;
import com.yizhishang.common.response.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yizhishang
 * @description 认证失败的业务处理无效token response重写
 * @since 2019/3/4 15:49
 */
@Component
public class CustomAuthExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthExceptionHandler.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        logger.error("====================================================== ");
        logger.error("当前访问的URL地址： {}", request.getRequestURI());

        Throwable cause = authException.getCause();
        generateResponse(request, response);
        if (cause instanceof InvalidTokenException) {
            logger.error("InvalidTokenException : {}", cause.getMessage());
            //Token无效
            response.getWriter().write(JSON.toJSONString(ResponseData.error(AuthExceptionEnum.ACCESS_TOKEN_INVALID)));
        } else {
            logger.error("AuthenticationException : {}", authException.getMessage());
            //资源未授权
            response.getWriter().write(JSON.toJSONString(ResponseData.error(AuthExceptionEnum.UNAUTHORIZED)));
        }
        logger.error("============ 身份认证失败..................... ");
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        generateResponse(request, response);
        //访问资源的用户权限不足
        logger.error("AccessDeniedException : {}", accessDeniedException.getMessage());
        response.getWriter().write(JSON.toJSONString(ResponseData.error(AuthExceptionEnum.INSUFFICIENT_PERMISSIONS)));
    }

    private void generateResponse(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.addHeader("Cache-Control", "no-cache");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        response.addHeader("Access-Control-Max-Age", "1800");
    }
}