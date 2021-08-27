package com.yizhishang.redis.cache.aspect;

import com.yizhishang.redis.cache.annotation.RedisCache;
import com.yizhishang.redis.lock.RedisLock;
import com.yizhishang.redis.util.ExplainUtil;
import com.yizhishang.redis.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 分布式缓存
 * 防雪崩，缓存时间随机
 *
 * @author yizhishang
 */
@Aspect
@Service
public class RedisCacheAspect {

    private static final Logger log = LoggerFactory.getLogger(RedisCacheAspect.class);

    private final RedisUtil redisUtil;

    private final RedisLock redisLock;

    @Autowired
    public RedisCacheAspect(RedisUtil redisUtil, RedisLock redisLock) {
        this.redisUtil = redisUtil;
        this.redisLock = redisLock;
    }

    /**
     * 设置缓存
     *
     * @param joinPoint  切入点
     * @param redisCache 缓存注解
     * @return 返回对象
     * @throws Throwable 异常
     */
    @Around("@annotation(redisCache)")
    public Object addCache(ProceedingJoinPoint joinPoint, RedisCache redisCache) throws Throwable {
        String key = redisCache.key();
        if (StringUtils.isBlank(key)) {
            key = getCacheKey(joinPoint);
        } else {
            key = ExplainUtil.explainKey(key, joinPoint);
        }
        // 查询缓存
        log.debug("取值key, {}", key);
        Object result = null;
        if (redisUtil.hasKey(key)) {
            result = redisUtil.get(key);
            log.debug("从redis中取出缓存, value: {}", result);
            return result;
        }

        // 加自旋锁锁, 防止缓存击穿
        boolean lock;
        do {
            lock = redisLock.tryLock(key, 1);
            if (lock) {
                if (redisUtil.hasKey(key)) {
                    result = redisUtil.get(key);
                    log.debug("从redis中取出缓存, value: {}", result);
                    return result;
                }

                result = joinPoint.proceed();
                // 设置key-null, 防止缓存穿透
                long expireTime = redisUtil.getRandomExpire(redisCache.expire());
                redisUtil.set(key, result, expireTime);

                redisLock.releaseLock(key);
            }
        } while (!lock);

        return result;
    }

    /**
     * 包名+ 类名 + 方法名 + 参数(多个) 生成Key
     */
    public String getCacheKey(ProceedingJoinPoint pjp) {
        StringBuilder key = new StringBuilder();

        String methodName = pjp.getSignature().getName();
        key.append(methodName);

        // 参数(多个)
        Object[] args = pjp.getArgs();
        if (args == null) {
            return key.toString();
        }
        for (Object arg : args) {
            // 参数
            if (arg == null) {
                key.append(".#");
                continue;
            }
            key.append(".").append(arg);
        }

        return key.toString();
    }

}