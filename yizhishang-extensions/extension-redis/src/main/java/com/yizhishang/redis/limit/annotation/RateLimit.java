package com.yizhishang.redis.limit.annotation;

import com.yizhishang.redis.limit.LimitType;

import java.lang.annotation.*;

/**
 * 令牌桶限流
 *
 * @author Levin
 * @since 2018-02-05
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RateLimit {

    /**
     * 资源的名字
     *
     * @return String
     */
    String name() default "";

    /**
     * 资源的key
     *
     * @return String
     */
    String key() default "rate-limit:";

    /**
     * 单位时间内放入的令牌数量
     *
     * @return
     */
    int rate() default 1;

    /**
     * 请求令牌数量
     *
     * @return int
     */
    int expectPermits() default 1;

    /**
     * bucket储存令牌的最大数量
     *
     * @return int
     */
    int maxPermits() default 3;

    /**
     * 应用标识
     *
     * @return
     */
    String tag() default "yizhishang";

    /**
     * 类型
     *
     * @return LimitType
     */
    LimitType limitType() default LimitType.CUSTOMER;
}

