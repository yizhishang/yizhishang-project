package com.yizhishang.redis.limit.ratelimit;

import com.yizhishang.redis.util.Consts;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author yizhishang
 */
@Service
public class RateLimitService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 令牌桶初始化脚本
     */
    private final DefaultRedisScript<Long> rateLimitInitScript;

    /**
     * 获取令牌脚本
     * <pre>
     * <code>
     *
     * -- 获取令牌
     * -- 返回码
     * -- 0 没有令牌桶配置
     * -- -1 表示取令牌失败，也就是桶里没有令牌
     * -- 1 表示取令牌成功
     * -- permits ARGV[1]  请求令牌数量
     * -- currMillSecond ARGV[2]   当前毫秒数
     * </code>
     * </pre>
     */
    private final DefaultRedisScript<Long> rateLimitScript;

    public RateLimitService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;

        rateLimitInitScript = new DefaultRedisScript<>();
        rateLimitInitScript.setResultType(Long.class);
        rateLimitInitScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("rate-bucket-limit-init.lua")));

        rateLimitScript = new DefaultRedisScript<>();
        rateLimitScript.setResultType(Long.class);
        rateLimitScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("rate-bucket-limit.lua")));
    }

    public RateToken initToken(String key) {
        return initToken(key, 1L, 10L, 10L, "yizhishang");
    }

    public RateToken initToken(String key, String tag) {
        return initToken(key, 1L, 10L, 10L, tag);
    }

    /**
     * 令牌桶初始化
     *
     * @param key           key值
     * @param expectPermits 请求令牌数量
     * @param maxPermits    bucket储存令牌的最大数量
     * @param rate          单位时间内放入的令牌数量
     * @param tag           应用标识
     * @return 1-设置成功
     */
    public RateToken initToken(String key, Long expectPermits, Long maxPermits, Long rate, String tag) {
        Long currMillSecond = getCurrentMillSeconds();
        Long acquire = redisTemplate.execute(rateLimitInitScript, Collections.singletonList(getKey(key)), currMillSecond, expectPermits, maxPermits, rate, tag);
        return RateToken.getByCode(acquire);
    }

    /**
     * 取令牌
     *
     * @param key key值
     * @return true-获取令牌成功; false-获取令牌失败
     */
    public RateToken acquireToken(String key) {
        return acquireToken(key, 1);
    }

    public RateToken acquireToken(String key, Integer permits) {
        Long currMillSecond = getCurrentMillSeconds();

        Long acquire = redisTemplate.execute(rateLimitScript, Collections.singletonList(getKey(key)), permits, currMillSecond);
        return RateToken.getByCode(acquire);
    }

    /**
     * 从redis获取当前时间：毫秒数
     *
     * @return
     */
    private Long getCurrentMillSeconds() {
        return redisTemplate.execute(
                RedisServerCommands::time
        );
    }

    private static String getKey(String key) {
        return Consts.RATE_LIMIT_KEY + key;
    }

}
