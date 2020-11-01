package com.yizhishang.common.enums;

/**
 * @author: yuanyj
 * @date: 2020/9/14 19:26
 */
public enum BrowserEnum {

    /**
     * qq浏览器
     */
    TENCENT_TRAVELER("TencentTraveler"),

    /**
     * 微信浏览器
     */
    MICRO_MESSENGER("MicroMessenger"),

    /**
     * 易信APP浏览器
     */
    YIXIN("YiXin")

        ;

    private String message;

    BrowserEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
