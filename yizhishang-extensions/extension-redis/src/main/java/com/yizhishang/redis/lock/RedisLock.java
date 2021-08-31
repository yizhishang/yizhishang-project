package com.yizhishang.redis.lock;

import com.yizhishang.redis.util.Consts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;

/**
 * redis分布式锁.<br>
 * 思路：
 * <pre>
 * 用SETNX命令，SETNX只有在key不存在时才返回成功。这意味着只有一个线程可以成功运行SETNX命令，而其他线程会失败，然后不断重试，直到它们能建立锁。
 * 然后使用脚本来创建锁，因为一个redis脚本同一时刻只能运行一次。
 *
 * 创建锁代码：
 * <code>
 *
 * -- KEYS[1] key,
 * -- ARGV[1] value,
 * -- ARGV[2] expireTimeMilliseconds
 *
 * if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then
 *      redis.call('expire', KEYS[1], ARGV[2])
 *      return 1
 * else
 *      return 0
 * end
 * </code>
 *
 * 最后使用脚本来解锁。
 * 解锁代码：
 * <code>
 *
 * -- KEYS[1] key,
 * -- ARGV[1] value
 * if redis.call("get", KEYS[1]) == ARGV[1]
 * then
 *      return redis.call("del", KEYS[1])
 * else
 *      return 0
 * end
 * </code>
 * </pre>
 *
 * @author 袁永君
 * @since 2019/11/13 13:59
 */
@Slf4j
@Component
public class RedisLock {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String LOCK_PREFIX = "lock.";
    private static final String LOCK_STRING = "if redis.call('setnx',KEYS[1],ARGV[1]) == 1 then redis.call('expire',KEYS[1],ARGV[2]) return 1 else return 0 end";
    private static final String RELEASE_LOCK_STRING = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
    private final RedisScript<Long> redisLockScript;
    private final RedisScript<Long> releaseScript;

    private static ThreadLocal<String> local = new ThreadLocal<>();

    @Autowired
    public RedisLock(RedisTemplate<String, Object> redisTemplate) {
        log.debug("RedisLock初始化");
        this.redisTemplate = redisTemplate;
        redisLockScript = new DefaultRedisScript<>(LOCK_STRING);
        releaseScript = new DefaultRedisScript<>(RELEASE_LOCK_STRING);
    }

    /**
     * 加锁
     *
     * @param key        锁
     * @param expireTime 超期时间，多少秒后这把锁自动释放：单位秒
     * @return 返回true表示拿到锁
     */
    public boolean tryLock(String key, int expireTime) {
        String uniqueId = UUID.randomUUID().toString();
        Long count = redisTemplate.execute(redisLockScript, Collections.singletonList(LOCK_PREFIX + key), uniqueId, expireTime);
        //判断是否成功
        if (Consts.SUCCESS.equals(count)) {
            local.set(uniqueId);
            return true;
        }
        return false;
    }

    /**
     * 释放锁
     *
     * @param key 锁
     * @return 返回true表示释放锁成功
     */
    public boolean releaseLock(String key) {
        Long count = redisTemplate.execute(releaseScript, Collections.singletonList(LOCK_PREFIX + key), local.get());
        //判断是否成功
        if (Consts.SUCCESS.equals(count)) {
            local.remove();
        }
        return false;
    }
}
