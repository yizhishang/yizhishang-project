package com.yizhishang.redis.lock;

import com.google.common.collect.Lists;
import com.yizhishang.common.exception.BizException;
import com.yizhishang.redis.util.Consts;
import com.yizhishang.redis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
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
    private final RedisUtil redisUtil;

    private static final String LOCK_PREFIX = "lock.";
    private static final String LOCK_STRING = "if redis.call('setnx',KEYS[1],ARGV[1]) == 1 then redis.call('expire',KEYS[1],ARGV[2]) return 1 else return 0 end";
    private static final String RELEASE_LOCK_STRING = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
    private final RedisScript<Long> redisLockScript;
    private final RedisScript<Long> releaseScript;

    private static final ThreadLocal<String> UUID_LOCAL = new ThreadLocal<>();

    @Autowired
    public RedisLock(RedisTemplate<String, Object> redisTemplate, RedisUtil redisUtil) {
        log.debug("RedisLock初始化");
        this.redisTemplate = redisTemplate;
        this.redisUtil = redisUtil;
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
        // 判断是否成功
        if (Consts.SUCCESS.equals(count)) {
            UUID_LOCAL.set(uniqueId);
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
        Long count = redisTemplate.execute(releaseScript, Collections.singletonList(LOCK_PREFIX + key), UUID_LOCAL.get());
        // 判断是否成功
        if (Consts.SUCCESS.equals(count)) {
            UUID_LOCAL.remove();
        }
        return false;
    }

    /**
     * 对批量数据实现分布式锁
     *
     * @param keys       批量数据的键值
     * @param expireTime 加锁时长
     * @param bizPrefix  key前缀
     * @param lockExBody 执行方法
     * @return 加锁执行结果
     */
    public LockResult apply(List<?> keys, Long expireTime, String bizPrefix, LockExBody lockExBody) {
        if (CollectionUtils.isEmpty(keys)) {
            throw new BizException("加锁键值不可为空");
        }
        if (lockExBody == null) {
            throw new BizException("执行方法体不可为空");
        }
        LockResult lockResult = new LockResult();
        lockResult.setLock(false);
        lockResult.setResult(false);
        // 获得锁
        List<String> lockKeys = getLockByKeys(keys, expireTime, bizPrefix);
        try {
            lockResult.setLockKeys(lockKeys);
            if (keys.size() > lockKeys.size()) {
                lockResult.setErrorExMsg("加锁数量不一致");
                return lockResult;
            }
            lockResult.setLock(true);
            lockExBody.execute();
            lockResult.setResult(true);
        } catch (BizException e) {
            lockResult.setErrorExMsg(e.getMessage());
        } catch (Exception e) {
            log.error("加锁执行失败: {}", e.getMessage());
            lockResult.setErrorExMsg("加锁执行失败");
            throw new BizException("加锁执行失败");
        } finally {
            // 应用代码执行报错，解锁
            unLockByKeys(lockKeys);
            UUID_LOCAL.remove();
        }
        return lockResult;
    }

    /**
     * 对批量数据实现分布式锁
     *
     * @param keys       keys列表
     * @param expireTime 过期时间
     * @param bizPrefix  前缀
     * @return 加锁结果
     */
    private List<String> getLockByKeys(List<?> keys, Long expireTime, String bizPrefix) {
        if (CollectionUtils.isEmpty(keys)) {
            return Lists.newArrayList();
        }
        List<String> lockKeys = Lists.newArrayList();
        try {
            String uuid = UUID.randomUUID().toString();
            UUID_LOCAL.set(uuid);
            for (Object key : keys) {
                String redisKey = String.format("%s:%s", bizPrefix, key);
                if (redisUtil.lock(redisKey, uuid, expireTime)) {
                    // 保存设置的锁信息
                    lockKeys.add(key.toString());
                    continue;
                }
                // 加锁失败
                unLockByKeys(lockKeys);
                return Lists.newArrayList();
            }
        } catch (Exception e) {
            log.error("redis lock error!lockKeys={}", lockKeys);
        }
        return lockKeys;
    }

    /**
     * 解锁
     *
     * @param lockKeys 加锁对象
     */
    private void unLockByKeys(List<String> lockKeys) {
        try {
            if (CollectionUtils.isEmpty(lockKeys)) {
                return;
            }
            List<String> deleteKeys = Lists.newArrayList();
            String uuid = UUID_LOCAL.get();
            for (String redisKey : lockKeys) {
                // 判断是否是自己的锁
                if (StringUtils.equals(uuid, redisUtil.getString(redisKey))) {
                    deleteKeys.add(redisKey);
                }
            }
            redisUtil.removeBatch(deleteKeys);
        } catch (Exception e) {
            log.error("redis unLock error!lockKeys={}", lockKeys);
        }
    }

    @FunctionalInterface
    public interface LockExBody {

        /**
         * 方法体
         */
        void execute();
    }
}
