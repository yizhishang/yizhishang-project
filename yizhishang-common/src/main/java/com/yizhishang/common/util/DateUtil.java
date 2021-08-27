package com.yizhishang.common.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;

/**
 * @author :  yizhishang
 * @description : class描述
 * @date :  2021-06-14 18:03
 */
public class DateUtil {

    /**
     * Date 转 LocalDate
     *
     * @param date
     * @return
     */
    public static LocalDate dateToLocalDate(Date date) {
        return LocalDate.of(date.getYear(), date.getMonth(), date.getDate());
    }

    /**
     * 获取当前日期处于该年的第几周
     * <p>默认周日为每周第一天</p>
     *
     * @param localDate 日期
     * @return
     */
    public static int getWeekInYear(LocalDate localDate) {
        return getWeekInYear(localDate, DayOfWeek.SUNDAY);
    }

    /**
     * 获取当前日期处于该年的第几周
     *
     * @param localDate      日期
     * @param firstDayOfWeek 星期第一天
     * @return
     */
    public static int getWeekInYear(LocalDate localDate, DayOfWeek firstDayOfWeek) {
        WeekFields weekFields = WeekFields.of(firstDayOfWeek, 1);
        int month = localDate.getMonthValue();
        int weekTh = localDate.get(weekFields.weekOfWeekBasedYear());
        if (month == 1 && weekTh > 20) {
            weekTh = 1;
        }

        if (month == Month.DECEMBER.getValue() && weekTh == 1) {
            weekTh = 53;
            // 闰年：yyyy-12-31日期，若yyyy-01-02处于第二个周，则12-31处于54个星期
            int dayOfYear = localDate.getDayOfYear();
            localDate = LocalDate.of(localDate.getYear(), 1, 2);
            final int secondWeek = 2;
            final int lastDay = 366;
            if (localDate.get(weekFields.weekOfWeekBasedYear()) == secondWeek && dayOfYear == lastDay) {
                weekTh = 54;
            }
        }
        return weekTh;
    }

    /**
     * 获取当前日期所在周的开始日期和结束日期
     *
     * @param now
     * @param weekStartEnd 一周的开始和结束
     * @return Date[2]
     */
    public static Date[] getWeekStartEnd(Date now, WeekStartEnd weekStartEnd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        //获得当前日期是一个星期的第几天
        calendar.setFirstDayOfWeek(weekStartEnd.getStart());

        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        //获得当前日期是一个星期的第几天
        int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);

        //根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        calendar.add(Calendar.DATE, weekStartEnd.getStart() - dayWeek);
        Date start = calendar.getTime();
        if (start.after(now)) {
            calendar.add(Calendar.DATE, -7);
        }

        Date[] result = new Date[2];
        result[0] = calendar.getTime();

        calendar.add(Calendar.DATE, 6);
        result[1] = calendar.getTime();

        return result;
    }

    @Getter
    @AllArgsConstructor
    public enum WeekStartEnd {
        /**
         * 周日至周一
         */
        SUNDAY(Calendar.SUNDAY, Calendar.SATURDAY),
        /**
         * 周一至周周日
         */
        MONDAY(Calendar.MONDAY, Calendar.SUNDAY),
        /**
         * 周二至周一
         */
        TUESDAY(Calendar.TUESDAY, Calendar.MONDAY),
        /**
         * 周三至周二
         */
        WEDNESDAY(Calendar.WEDNESDAY, Calendar.TUESDAY),
        /**
         * 周四至周三
         */
        THURSDAY(Calendar.THURSDAY, Calendar.WEDNESDAY),
        /**
         * 周五至周四
         */
        FRIDAY(Calendar.FRIDAY, Calendar.THURSDAY),
        /**
         * 周六至周五
         */
        SATURDAY(Calendar.SATURDAY, Calendar.FRIDAY);

        /**
         * 每周开始
         */
        private Integer start;

        /**
         * 每周结束
         */
        private Integer end;

    }

}
