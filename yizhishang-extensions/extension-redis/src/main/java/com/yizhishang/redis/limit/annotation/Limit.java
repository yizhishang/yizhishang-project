package com.yizhishang.redis.limit.annotation;

import com.yizhishang.redis.limit.LimitType;

import java.lang.annotation.*;

/**
 * 限流: 120秒内最多访问3次
 *
 * @author Levin
 * @since 2018-02-05
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Limit {

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
    String key() default "";

    /**
     * Key的prefix
     *
     * @return String
     */
    String prefix() default "rate-limit:";

    /**
     * 过期时间，单位秒
     *
     * @return int
     */
    int expire() default 120;

    /**
     * 单位时间限制通过请求数
     *
     * @return int
     */
    int limit() default 3;

    /**
     * 类型
     *
     * @return LimitType
     */
    LimitType limitType() default LimitType.CUSTOMER;
}

