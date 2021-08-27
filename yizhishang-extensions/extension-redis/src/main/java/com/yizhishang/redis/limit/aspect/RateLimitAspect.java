package com.yizhishang.redis.limit.aspect;

import com.yizhishang.redis.RedisException;
import com.yizhishang.redis.limit.annotation.RateLimit;
import com.yizhishang.redis.limit.ratelimit.RateLimitService;
import com.yizhishang.redis.limit.ratelimit.RateToken;
import com.yizhishang.redis.util.Consts;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Description :
 *
 * @author yizhishang
 * CreateTime    2018/09/05
 * Description   MethodRateLimit注解切面类
 */
@Slf4j
@Aspect
@Component
public class RateLimitAspect {

    private final RateLimitService rateLimitService;

    @Autowired
    public RateLimitAspect(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    /**
     * @param joinPoint 切点
     */
    @Around("execution(public * *(..)) && @annotation(com.yizhishang.redis.limit.annotation.RateLimit)")
    public Object interceptor(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);
        RateToken acquire = rateLimitService.acquireToken(rateLimit.key(), rateLimit.expectPermits());

        if (acquire.isSuccess()) {
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                throw new RedisException(Consts.SYSTEM_ERROR, throwable);
            }
        }
        if (acquire.isFailed()) {
            log.info("获取令牌失败");
        }
        if (acquire.isNone()) {
            log.error("令牌桶未设置，请联系管理员");
        }
        log.debug("{}: 允许的请求次数={} [触发限流]", rateLimit.name(), rateLimit.expectPermits());
        return "[触发限流]";
    }

}
