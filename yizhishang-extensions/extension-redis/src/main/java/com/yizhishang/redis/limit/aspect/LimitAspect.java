package com.yizhishang.redis.limit.aspect;

import com.google.common.collect.ImmutableList;
import com.yizhishang.redis.RedisException;
import com.yizhishang.redis.limit.LimitType;
import com.yizhishang.redis.limit.annotation.Limit;
import com.yizhishang.redis.util.Consts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 分布式接口限流
 *
 * @author 袁永君
 * @since 2019/11/11 18:44
 */
@Aspect
@Slf4j
@Configuration
public class LimitAspect {

    private final RedisTemplate<String, Object> redisTemplate;

    private final DefaultRedisScript<Long> redisScript;

    @Autowired
    public LimitAspect(RedisTemplate<String, Object> redisTemplate) {
        log.debug("分布式接口限流初始化");
        this.redisTemplate = redisTemplate;
        redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("rate-count-limit.lua")));
    }

    @Around("execution(public * *(..)) && @annotation(com.yizhishang.redis.limit.annotation.Limit)")
    public Object interceptor(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Limit limitAnnotation = method.getAnnotation(Limit.class);
        LimitType limitType = limitAnnotation.limitType();
        String name = limitAnnotation.name();
        String key;

        switch (limitType) {
            case IP:
                key = getIpAddress();
                break;
            case CUSTOMER:
                key = limitAnnotation.key();
                break;
            default:
                key = StringUtils.upperCase(method.getName());
        }
        ImmutableList<String> keys = ImmutableList.of(StringUtils.join(limitAnnotation.prefix(), key));

        int expire = limitAnnotation.expire();
        int limit = limitAnnotation.limit();
        Long count = redisTemplate.execute(redisScript, keys, limit, expire);

        if (Consts.SUCCESS.equals(count)) {
            try {
                return pjp.proceed();
            } catch (Throwable throwable) {
                throw new RedisException(Consts.SYSTEM_ERROR, throwable);
            }
        }
        log.debug("{}:由于超过单位时间={}, -允许的请求次数={} [触发限流]", name, expire, limit);
        return "[触发限流]";
    }

    public String getIpAddress() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || Consts.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || Consts.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || Consts.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}