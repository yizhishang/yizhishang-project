package com.yizhishang.redis.cache.aspect;

import com.yizhishang.redis.cache.annotation.RedisEvict;
import com.yizhishang.redis.util.ExplainUtil;
import com.yizhishang.redis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 删除缓存
 *
 * @author yizhishang
 */
@Slf4j
@Aspect
@Service
public class RedisEvictAspect {

    private final RedisUtil redisUtil;

    @Autowired
    public RedisEvictAspect(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 清除缓存
     *
     * @param joinPoint  切入点
     * @param redisEvict 缓存注解
     * @return 返回对象
     * @throws Throwable 异常
     */
    @Around("@annotation(redisEvict)")
    public Object removeCache(ProceedingJoinPoint joinPoint, RedisEvict redisEvict) throws Throwable {

        if (redisEvict.keys().length > 0) {
            for (String key : redisEvict.keys()) {
                String redisKey = ExplainUtil.explainKey(key, joinPoint);
                redisUtil.remove(redisKey);
            }
        }
        return joinPoint.proceed();
    }

}