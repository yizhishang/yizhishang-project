package com.yizhishang.redis.util;

/**
 * 常量类
 *
 * @author yizhishang
 */
public class Consts {

    private Consts() {

    }

    public static final Long SUCCESS = 1L;

    public static final String UNKNOWN = "unknown";

    public static final String RATE_LIMIT_KEY = "ratelimit:";

    public static final String SYSTEM_BUSY_ERROR = "系统繁忙...";

    public static final String SYSTEM_ERROR = "系错误...";

    public static final String REDIS_OPERATE_ERROR = "redis操作异常";
}
