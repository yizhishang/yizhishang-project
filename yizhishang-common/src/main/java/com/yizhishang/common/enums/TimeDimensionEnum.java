package com.yizhishang.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 时间维度
 *
 * @author yizhishang
 * @since 2021/6/30 15:00
 */
@Getter
@AllArgsConstructor
public enum TimeDimensionEnum {

    /**
     * week-周
     */
    WEEK(1, "周"),

    /**
     * month-月
     */
    MONTH(2, "月"),

    /**
     * quarter-季度
     */
    QUARTER(3, "季度"),

    /**
     * half_year-半年
     */
    HALF_YEAR(4, "半年"),

    /**
     * year-年
     */
    YEAR(5, "年");

    private final Integer code;
    private final String desc;

    public static TimeDimensionEnum getByCode(Integer code) {
        for (TimeDimensionEnum item : TimeDimensionEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
