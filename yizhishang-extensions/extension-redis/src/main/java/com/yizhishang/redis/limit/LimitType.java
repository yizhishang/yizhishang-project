package com.yizhishang.redis.limit;

/**
 * @author yizhishang
 */

public enum LimitType {
    /**
     * 自定义key
     */
    CUSTOMER,
    /**
     * 根据请求者IP
     */
    IP;
}