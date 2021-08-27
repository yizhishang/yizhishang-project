package com.yizhishang.common.util;

import cn.hutool.core.date.DateUtil;
import com.yizhishang.common.enums.TimeDimensionEnum;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;

/**
 * 业务日期工具类
 *
 * @author yizhishang
 * @date 2021/7/26 18:10
 */
public class BusinessDateUtil {

    /**
     * 获取当前时间所在周的开始日期和结束日期
     *
     * @return LocalDate数组: [开始日期, 截止日期]
     */
    public static LocalDate[] getWeekStartEnd(LocalDate localDate) {
        LocalDate[] result = new LocalDate[2];
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, localDate.getYear());
        calendar.set(Calendar.MONTH, localDate.getMonthValue() - 1);
        calendar.set(Calendar.DATE, localDate.getDayOfMonth());
        Date now = calendar.getTime();
        // 设置一周的开始日期
        calendar.setFirstDayOfWeek(Calendar.FRIDAY);

        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        //获得当前日期是一个星期的第几天
        int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);

        //根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        calendar.add(Calendar.DATE, Calendar.FRIDAY - dayWeek);
        Date start = calendar.getTime();
        if (start.after(now)) {
            calendar.add(Calendar.DATE, -7);
        }
        result[0] = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));

        calendar.add(Calendar.DATE, 6);
        result[1] = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
        return result;
    }

    /**
     * 获取当前时间所在周的开始日期和结束日期
     *
     * @return
     */
    public static Date[] getWeekStartEnd(Date now) {
        Date[] result = new Date[2];
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(now);
        // 设置一周的开始日期
        calendar.setFirstDayOfWeek(Calendar.FRIDAY);

        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        //获得当前日期是一个星期的第几天
        int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);

        //根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        calendar.add(Calendar.DATE, Calendar.FRIDAY - dayWeek);
        Date start = calendar.getTime();
        if (start.after(now)) {
            calendar.add(Calendar.DATE, -7);
        }
        result[0] = calendar.getTime();

        calendar.add(Calendar.DATE, 6);
        result[1] = calendar.getTime();
        return result;
    }

    /**
     * 获取前一个周五日期（包含当前日期）
     *
     * @param localDate 日期
     * @return LocalDate对象
     */
    public static LocalDate getPreviousOrSameDate(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.FRIDAY));
    }

    /**
     * 获取前一个周期日期（包含当前日期）
     *
     * @param localDate 日期
     * @param dayOfWeek
     * @return LocalDate对象
     */
    public static LocalDate getPreviousOrSameDate(LocalDate localDate, DayOfWeek dayOfWeek) {
        return localDate.with(TemporalAdjusters.previousOrSame(dayOfWeek));
    }

    /**
     * 获取一年的最后一天
     *
     * @param year 年份
     * @return LocalDate对象
     */
    public static LocalDate getFinalDateByYear(int year) {
        return LocalDate.of(year, 12, 31);
    }


    /**
     * 获取半年度的第一天
     *
     * @param year         年份
     * @param halfYearType 1-上半年，2-下半年
     * @return LocalDate对象
     */
    public static LocalDate getHalfYearFirstDate(int year, int halfYearType) {
        LocalDate date = LocalDate.of(year, 1, 1);
        switch (halfYearType) {
            case 1:
                date = date.withMonth(1);
                break;
            case 2:
                date = date.withMonth(7);
                break;
            default:
        }
        return date;
    }

    /**
     * 获取半年度的最后一天
     *
     * @param year 年份
     * @return LocalDate对象
     */
    public static LocalDate getHalfYearFinalDate(int year, int timeNum) {
        LocalDate up = LocalDate.of(year, 6, 30);
        LocalDate down = LocalDate.of(year, 12, 31);

        switch (timeNum) {
            case 1:
                return up;
            case 2:
                return down;
            default:
                System.err.println("上下半年输入错误: " + timeNum);
        }
        return null;
    }

    /**
     * 获取季度的第一天
     *
     * @param year    年份    年份
     * @param quarter 季度值
     * @return LocalDate对象
     */
    public static LocalDate getQuarterFirstDate(int year, int quarter) {
        LocalDate date = LocalDate.of(year, 1, 1);

        switch (quarter) {
            case 4:
                date = date.withMonth(10);
                break;
            case 3:
                date = date.withMonth(7);
                break;
            case 2:
                date = date.withMonth(4);
                break;
            case 1:
                date = date.withMonth(1);
                break;
            default:
                System.err.println("一年最多只有4个季度: " + quarter);
                return null;
        }
        return date;
    }

    /**
     * 获取季度的最后一天
     *
     * @param year    年份
     * @param quarter 季度值
     * @return LocalDate对象
     */
    public static LocalDate getQuarterFinalDate(int year, int quarter) {
        LocalDate date = LocalDate.of(year, 1, 31);

        switch (quarter) {
            case 4:
                date = date.withMonth(12);
                break;
            case 3:
                date = date.withMonth(9);
                date = date.withDayOfMonth(30);
                break;
            case 2:
                date = date.withMonth(6);
                date = date.withDayOfMonth(30);
                break;
            case 1:
                date = date.withMonth(3);
                break;
            default:
                System.err.println("一年最多只有4个季度: " + quarter);
                return null;
        }
        return date;
    }

    /**
     * 获取当月的第一天
     *
     * @param year  年份
     * @param month 月份
     * @return LocalDate对象
     */
    public static LocalDate getMonthFirstDate(int year, int month) {
        return LocalDate.of(year, month, 1);
    }

    /**
     * 获取当月的最后一天
     *
     * @param year  年份
     * @param month 月份
     * @return LocalDate对象
     */
    public static LocalDate getMonthFinalDate(Integer year, int month) {
        LocalDate date = LocalDate.of(year, month, 31);

        switch (month) {
            case 4:
            case 6:
            case 9:
            case 11:
                date = date.withDayOfMonth(30);
                break;
            case 2:
                boolean isLeap = DateUtil.isLeapYear(year);
                if (isLeap) {
                    date = date.withDayOfMonth(29);
                } else {
                    date = date.withDayOfMonth(28);
                }
                break;
            default:
        }
        return date;
    }

    /**
     * 获取某日期处于哪一周
     *
     * @param localDate 日期
     * @return 周数
     */
    public static int getCurrentWeek(LocalDate localDate) {
        DayOfWeek dayOfWeek = DayOfWeek.FRIDAY;
        WeekFields weekFields = WeekFields.of(dayOfWeek, 1);
        int weekTh = localDate.get(weekFields.weekOfWeekBasedYear());
        if (weekTh == 1 && localDate.getMonthValue() == Month.DECEMBER.getValue()) {
            localDate = localDate.minusDays(7);
            weekTh = localDate.get(weekFields.weekOfWeekBasedYear());
            weekTh++;
        }
        return weekTh;
    }

    /**
     * 获取某年目标周数的起止日期
     *
     * @param year         年份
     * @param expectWeekTh 一年中的周数
     * @return LocalDate数组: [开始日期, 截止日期]
     */
    public static LocalDate[] getWeekStartEnd(int year, int expectWeekTh) {
        LocalDate now = LocalDate.now();
        now = now.withYear(year);
        int weekTh = getCurrentWeek(now);
        now = now.plusWeeks(expectWeekTh - weekTh);

        return getWeekStartEnd(now);
    }

    /**
     * 获取当前维度的起止日期
     *
     * @param year     年份
     * @param timeUnit 时间维度
     * @param timeNum  第几个时间维度
     * @return LocalDate数组: [开始日期, 截止日期]
     */
    public static LocalDate[] getPeriod(@NotNull Integer year, @NotNull Integer timeUnit, @NotNull Integer timeNum) {
        LocalDate[] period = new LocalDate[2];
        period[0] = null;
        period[1] = null;

        TimeDimensionEnum timeDimensionEnum = TimeDimensionEnum.getByCode(timeUnit);
        switch (timeDimensionEnum) {
            case WEEK:
                if (timeNum > 54) {
                    System.err.println("一年最多只有54个周");
                    return period;
                }
                return getWeekStartEnd(year, timeNum);
            case MONTH:
                if (timeNum > 12) {
                    System.err.println("一年最多只有12个月");
                    return period;
                }
                period[0] = getMonthFirstDate(year, timeNum);
                period[1] = getMonthFinalDate(year, timeNum);
                break;
            case QUARTER:
                if (timeNum > 4) {
                    System.err.println("一年最多只有4个季度");
                    return null;
                }
                period[0] = getQuarterFirstDate(year, timeNum);
                period[1] = getQuarterFinalDate(year, timeNum);
                break;
            case HALF_YEAR:
                if (timeNum > 2) {
                    System.err.println("一年最多只有2个半年");
                    return null;
                }
                period[0] = getHalfYearFirstDate(year, timeNum);
                period[1] = getHalfYearFinalDate(year, timeNum);
                break;
            case YEAR:
                period[0] = LocalDate.of(year, 1, 1);
                period[1] = LocalDate.of(year, 12, 31);
                break;
            default:
        }
        return period;
    }

    /**
     * 获取上一个维度的起止时间
     *
     * @param year     年份
     * @param timeUnit 时间维度
     * @param timeNum  第几段时间
     * @return LocalDate数组: [开始日期, 截止日期]
     */
    public static LocalDate[] getLastPeriod(@NotNull Integer year, @NotNull Integer timeUnit, @NotNull Integer timeNum) {
        if (timeNum > 1) {
            return BusinessDateUtil.getPeriod(year, timeUnit, timeNum - 1);
        }

        // 年份减1
        year--;

        TimeDimensionEnum timeDimensionEnum = TimeDimensionEnum.getByCode(timeUnit);
        // 每年最后一天 12/31
        LocalDate endDate = LocalDate.of(year, 12, 31);

        LocalDate startDate = LocalDate.now();
        switch (timeDimensionEnum) {
            case WEEK:
                LocalDate[] period = BusinessDateUtil.getWeekStartEnd(endDate);
                startDate = period[0];
                break;
            case MONTH:
                startDate = LocalDate.of(year, 12, 1);
                break;
            case QUARTER:
                startDate = getQuarterFirstDate(year, 4);
                break;
            case HALF_YEAR:
                startDate = getHalfYearFirstDate(year, 2);
                break;
            case YEAR:
                startDate = LocalDate.of(year, 1, 1);
                break;
            default:
        }
        LocalDate[] period = new LocalDate[2];
        period[0] = startDate;
        period[1] = endDate;
        return period;
    }

}
