package com.yizhishang.redis.limit.ratelimit;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 获取令牌结果枚举
 *
 * @author 袁永君
 */
@AllArgsConstructor
public enum RateToken {

    /**
     * 成功
     */
    SUCCESS(1, "成功"),

    /**
     * 失败
     */
    FAILED(-1, "失败"),

    /**
     * 没有配置
     */
    NONE(0, "没有配置");

    @Getter
    private int code;

    @Getter
    private String content;

    public static RateToken getByCode(Long code) {
        if (code == null) {
            return RateToken.NONE;
        }
        for (RateToken rateToken : RateToken.values()) {
            if (rateToken.getCode() == code) {
                return rateToken;
            }
        }
        return RateToken.NONE;
    }

    public boolean isSuccess() {
        return this.code == 1;
    }

    public boolean isFailed() {
        return this.code == -1;
    }

    public boolean isNone() {
        return this.code == 0;
    }
}
