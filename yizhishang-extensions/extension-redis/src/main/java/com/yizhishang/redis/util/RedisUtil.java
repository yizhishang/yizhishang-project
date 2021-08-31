package com.yizhishang.redis.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.yizhishang.redis.RedisException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 *
 * @author 袁永君
 * @since 2019/11/19 13:46
 */
@Slf4j
@Component
public class RedisUtil {

    /**
     * 最小过期时间
     */
    @Value("${spring.redis.cache.expireTime:1800}")
    private long minExpireTime;

    /**
     * 随机过期时间[0,1800)
     */
    @Value("${spring.redis.cache.randomTime:1800}")
    private int randomTime;

    private final RedisTemplate<String, Object> redisTemplate;

    private final ValueOperations<String, Object> valueOperations;

    private final HashOperations<String, String, Object> hashOperations;

    private final ListOperations<String, Object> listOperations;

    private final SetOperations<String, Object> setOperations;

    private final ZSetOperations<String, Object> zSetOperations;

    private final Random random;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate, ValueOperations<String, Object> valueOperations, HashOperations<String, String, Object> hashOperations, ListOperations<String, Object> listOperations, SetOperations<String, Object> setOperations, ZSetOperations<String, Object> zSetOperations) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = valueOperations;
        this.hashOperations = hashOperations;
        this.listOperations = listOperations;
        this.setOperations = setOperations;
        this.zSetOperations = zSetOperations;
        this.random = new Random();
    }

    /**=============================共同操作============================*/
    /**
     * 随机缓存失效时间(秒)
     *
     * @param key 键
     */
    public boolean expireRandom(@NonNull String key) {
        long expireTime = getRandomExpire(0);
        log.info("随机缓存：{}秒", expireTime);
        return redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key        键
     * @param expireTime 时间(秒)
     */
    public Boolean expire(@NonNull String key, long expireTime) {
        return redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key        键
     * @param expireTime 时间(秒)
     */
    public boolean expire(@NonNull String key, long expireTime, TimeUnit timeUnit) {
        try {
            if (expireTime > 0) {
                redisTemplate.expire(key, expireTime, timeUnit);
            }
            return true;
        } catch (Exception e) {
            log.error(Consts.REDIS_OPERATE_ERROR, e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public Long getExpire(@NonNull String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 获取随机时间：默认范围 [1800,3600)
     *
     * @param expireTime
     * @return
     */
    public Long getRandomExpire(long expireTime) {
        if (expireTime == 0) {
            expireTime = random.nextInt(randomTime) + minExpireTime;
        }
        return expireTime;
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true-存在、false-不存在
     */
    public Boolean hasKey(@NonNull String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error(Consts.REDIS_OPERATE_ERROR, e);
            return Boolean.FALSE;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值或多个
     */
    public void remove(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }
    /**============================ValueOperations操作=============================*/
    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true-成功、 false-失败
     */
    public boolean set(@NonNull String key, Object value) {
        try {
            valueOperations.set(key, value);
            return true;
        } catch (Exception e) {
            log.error(Consts.REDIS_OPERATE_ERROR, e);
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key        键
     * @param value      值
     * @param expireTime 时间(秒) expireTime要大于0 如果expireTime小于等于0 将设置无限期
     * @return true成功 false失败
     */
    public boolean set(@NonNull String key, Object value, long expireTime) {
        try {
            if (expireTime > 0) {
                valueOperations.set(key, value, expireTime, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error(Consts.REDIS_OPERATE_ERROR, e);
            return false;
        }
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(@NonNull String key) {
        return valueOperations.get(key);
    }

    /**
     * 字符串缓存获取
     *
     * @param key 键
     * @return 值
     */
    public String getString(@NonNull String key) {
        Object value = get(key);
        return value == null ? null : (String) value;
    }

    /**
     * 递增 1
     *
     * @param key 键
     */
    public Long incr(@NonNull String key) {
        return incr(key, 1);
    }

    /**
     * 递增
     *
     * @param key 键
     */
    public Long incr(@NonNull String key, long delta) {
        if (delta < 0) {
            throw new RedisException("递增因子必须大于0");
        }
        return valueOperations.increment(key, delta);
    }

    /**
     * 递减
     *
     * @param delta 要减少几(小于0)
     */
    public Long decr(@NonNull String key, long delta) {
        if (delta < 0) {
            throw new RedisException("递减因子必须大于0");
        }
        return valueOperations.increment(key, -delta);
    }

    /**================================HashOperations操作=================================*/

    /**
     * HashKeys
     *
     * @param key 键 不能为null
     * @return 值
     */
    public Set<String> hkeys(@NonNull String key) {
        return hashOperations.keys(key);
    }

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(@NonNull String key, String item) {
        return hashOperations.get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值对
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map hmget(@NonNull String key) {
        try {
            return hashOperations.entries(key);
        } catch (Exception e) {
            log.error(Consts.REDIS_OPERATE_ERROR, e);
            return null;
        }
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hashPutAll(@NonNull String key, Map<String, Object> map) {
        try {
            hashOperations.putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error(Consts.REDIS_OPERATE_ERROR, e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hashPutAll(@NonNull String key, Map<String, Object> map, long time) {
        try {
            hashOperations.putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(Consts.REDIS_OPERATE_ERROR, e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hashPut(@NonNull String key, String item, Object value) {
        try {
            hashOperations.put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error(Consts.REDIS_OPERATE_ERROR, e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hashPut(@NonNull String key, String item, Object value, long time) {
        try {
            hashOperations.put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(Consts.REDIS_OPERATE_ERROR, e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hashDelete(@NonNull String key, Object... item) {
        hashOperations.delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(@NonNull String key, String item) {
        return hashOperations.hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     */
    public double hincr(@NonNull String key, String item, double by) {
        return hashOperations.increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     */
    public double hdecr(@NonNull String key, String item, double by) {
        return hashOperations.increment(key, item, -by);
    }

    /**
     * 根据key-list获取value-list
     *
     * @param key  键
     * @param list 键值list
     * @return
     */
    public List<Object> multiGet(@NonNull String key, List<String> list) {
        return hashOperations.multiGet(key, list);
    }

    /**
     * ============================set============================= /** 根据key获取Set中的所有值
     *
     * @param key 键
     */
    public Set<Object> sGet(@NonNull String key) {
        try {
            return setOperations.members(key);
        } catch (Exception e) {
            log.error(Consts.REDIS_OPERATE_ERROR, e);
            return Sets.newHashSet();
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public Boolean setHasKey(@NonNull String key, Object value) {
        try {
            return setOperations.isMember(key, value);
        } catch (Exception e) {
            log.error(Consts.REDIS_OPERATE_ERROR, e);
            return Boolean.FALSE;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long setAdd(@NonNull String key, Object... values) {
        try {
            return setOperations.add(key, values);
        } catch (Exception e) {
            log.error(Consts.REDIS_OPERATE_ERROR, e);
            return 0L;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long setAdd(@NonNull String key, long time, Object... values) {
        try {
            Long count = setOperations.add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            log.error(Consts.REDIS_OPERATE_ERROR, e);
            return 0L;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     */
    public Long setGetSize(@NonNull String key) {
        try {
            return setOperations.size(key);
        } catch (Exception e) {
            log.error(Consts.REDIS_OPERATE_ERROR, e);
            return 0L;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public Long setRemove(@NonNull String key, Object... values) {
        try {
            return setOperations.remove(key, values);
        } catch (Exception e) {
            log.error(Consts.REDIS_OPERATE_ERROR, e);
            return 0L;
        }
    }

    /**
     * ===============================ListOperations================================= /** 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     */
    public List<Object> listRange(@NonNull String key, long start, long end) {
        try {
            return listOperations.range(key, start, end);
        } catch (Exception e) {
            log.error(Consts.REDIS_OPERATE_ERROR, e);
            return Lists.newArrayList();
        }
    }

    /**
     * 获取list缓存的所有内容
     */
    public List<Object> listRange(@NonNull String key) {
        return listRange(key, 0, -1);
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     */
    public Long listGetSize(@NonNull String key) {
        try {
            return listOperations.size(key);
        } catch (Exception e) {
            log.error(Consts.REDIS_OPERATE_ERROR, e);
            return 0L;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     */
    public Object lGetIndex(@NonNull String key, long index) {
        try {
            return listOperations.index(key, index);
        } catch (Exception e) {
            log.error(Consts.REDIS_OPERATE_ERROR, e);
            return null;
        }
    }

    /**
     * 将list放入缓存：尾部添加
     *
     * @param key   键
     * @param value 值
     */
    public Long rightPush(@NonNull String key, Object value) {
        try {
            return listOperations.rightPush(key, value);
        } catch (Exception e) {
            log.error(Consts.REDIS_OPERATE_ERROR, e);
            return -1L;
        }
    }

    /**
     * 将list放入缓存：尾部添加
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     */
    public boolean rightPush(@NonNull String key, Object value, long time) {
        try {
            listOperations.rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(Consts.REDIS_OPERATE_ERROR, e);
            return false;
        }
    }

    /**
     * 将list放入缓存：尾部添加
     *
     * @param key   键
     * @param value 值
     */
    public Long rightPushAll(@NonNull String key, List<Object> value) {
        try {
            return listOperations.rightPushAll(key, value);
        } catch (Exception e) {
            log.error(Consts.REDIS_OPERATE_ERROR, e);
            return -1L;
        }
    }

    /**
     * 将list放入缓存：尾部添加
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     */
    public boolean rightPushAll(@NonNull String key, List<Object> value, long time) {
        try {
            listOperations.rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(Consts.REDIS_OPERATE_ERROR, e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     */
    public boolean lUpdateIndex(@NonNull String key, long index, Object value) {
        try {
            listOperations.set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error(Consts.REDIS_OPERATE_ERROR, e);
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public Long lRemove(@NonNull String key, long count, Object value) {
        try {
            return listOperations.remove(key, count, value);
        } catch (Exception e) {
            log.error(Consts.REDIS_OPERATE_ERROR, e);
            return -1L;
        }
    }
    /**============================ValueOperations操作=============================*/
    /**
     * zSet 添加数据
     *
     * @param key   set-key
     * @param value 数据
     * @param score 分值
     * @return
     */
    public Boolean zAdd(@NonNull String key, Object value, double score) {
        try {
            return zSetOperations.add(key, value, score);
        } catch (Exception e) {
            log.error(Consts.REDIS_OPERATE_ERROR, e);
            return Boolean.FALSE;
        }
    }

    /**
     * zSet 计数
     *
     * @param key   set-key
     * @param value 数据
     * @param score 分值
     * @return
     */
    public Double zIncrementScore(@NotNull String key, Object value, double score) {
        return zSetOperations.incrementScore(key, value, score);
    }

    /**
     * zSet 从高到低获取[start, end]个元素<p>不包含score</p>
     *
     * @param key   set-key
     * @param start 开始下标
     * @param end   截止下标
     * @return
     */
    public Set<Object> zReverseRange(@NotNull String key, long start, long end) {
        return zSetOperations.reverseRange(key, start, end);
    }

    /**
     * zSet 从高到低获取<font size="6">count</font>个元素
     *
     * @param key   set-key
     * @param count 数量
     * @return
     */
    public Set<Object> zTop(@NotNull String key, long count) {
        return zSetOperations.reverseRange(key, 0, count - 1);
    }

    /**
     * zSet 从高到低获取[start, end]个元素<p>包含score</p>
     *
     * @param key   set-key
     * @param start 开始下标
     * @param end   截止下标
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> zReverseRangeWithScores(@NotNull String key, long start, long end) {
        return zSetOperations.reverseRangeWithScores(key, start, end);
    }
}