package com.yizhishang.redis.cache.annotation;

import java.lang.annotation.*;

/**
 * 设置缓存
 *
 * @author yizhishang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RedisCache {

    /**
     * 自定义key（注意唯一性）
     *
     * @return key值
     */
    String key() default "";

    /**
     * 缓存多少秒,默认无限期
     */
    int expire() default 0;

}