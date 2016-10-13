package com.lancheng.jneng.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * 日期工具类
 */
public final class CalendarUtil {
    private static final SimpleDateFormat sHourMinuteSdf = new SimpleDateFormat("HH:mm", Locale.CHINESE);
    private static final SimpleDateFormat sYearMonthDayHourMinuteSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINESE);
    public static final SimpleDateFormat sYearMonthDaySdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
    private static final SimpleDateFormat sYearMonthDayWeekSdf = new SimpleDateFormat("yyyy-MM-dd E", Locale.CHINESE);
    private static final SimpleDateFormat sChineseYearMonthDaySdf = new SimpleDateFormat("yyyy年MM月dd", Locale.CHINESE);
    private static final SimpleDateFormat sChineseYearMonthDayWeekSdf = new SimpleDateFormat("yyyy年MM月dd E", Locale.CHINESE);
    public static final SimpleDateFormat sYearMonthDayHourMinuteSecondsSdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINESE);

    private static final long MINUTE_MILLISECONDS = 60 * 1000;
    private static final long HOUR_MILLISECONDS = 60 * MINUTE_MILLISECONDS;
    private static final long DAY_MILLISECONDS = 24 * HOUR_MILLISECONDS;


    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    private static final SimpleDateFormat sdfShortDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private static final SimpleDateFormat sdfutc = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZ yyyy", Locale.ENGLISH);


    /***
     * @param date
     * @param hastime
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String formatDate2Chinese(Date date, boolean hastime) {
        if (date == null)
            return null;
        Date dateNow = new Date();
        if (hastime) {
            long Minutes = (dateNow.getTime() - date.getTime()) / 1000 / 60;
            if (Minutes == 0)
                return "刚刚";
            if (Minutes < 60 && Minutes > 0)
                return String.valueOf(Minutes) + "分钟前";
        }
        String formatstr = "";
        int yeardiff = dateNow.getYear() - date.getYear();
        int daydiff = differenceDates(dateNow, date, false);
        if (yeardiff > 0)
            formatstr = "yyyy-MM-dd";
        else if (daydiff == 0)
            formatstr = "今天";
        else if (daydiff == -1)
            formatstr = "昨天";
        else if (daydiff == -2)
            formatstr = "前天";
        else if (daydiff == 1)
            formatstr = "明天";
        else
            formatstr = "MM-dd";
        formatstr = hastime ? formatstr + " HH:mm" : formatstr;
        SimpleDateFormat sdf = new SimpleDateFormat(formatstr, Locale.CHINESE);
        return sdf.format(date);
    }


    /**
     * 计算两个日期之间相差的天数
     *
     * @param Date1 基准日期
     * @param Date2 对比日期
     * @param abs   比较结果是否取绝对值
     * @return 如果不取绝对值, 日期相差的天数 >0: Date2在Date1之后 0: Date2和Date1在同一天 <0:
     * Date2在Date1之前
     */
    public static int differenceDates(Date Date1, Date Date2, boolean abs) {
        if (Date1 == null)
            return 0;
        if (Date2 == null)
            return 0;
        try {
            Date1 = sdfShortDate.parse(sdfShortDate.format(Date1));
            Date2 = sdfShortDate.parse(sdfShortDate.format(Date2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(Date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(Date2);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        if (abs)
            between_days = Math.abs(between_days);
        return Integer.parseInt(String.valueOf(between_days));
    }


    /**
     * 取得指定日期的星期
     *
     * @param date
     * 日期 为null时表示当天
     */
    public static String[] weekday = new String[]{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    public static String getWeekDay(Date date) {

        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        int day = cal.get(Calendar.DAY_OF_WEEK) - 1;

        return weekday[day];
    }


    /**
     * 计算两个日期之间相差的天数
     *
     * @param Date1 基准日期
     * @param Date2 对比日期
     * @return 日期相差的天数绝对值
     */
    public static int differenceDates(Date Date1, Date Date2) {
        return differenceDates(Date1, Date2, true);
    }

    public static long differenceSecond(Date Date1, Date Date2, boolean abs) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(Date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(Date2);
        long time2 = cal.getTimeInMillis();
        long between_s = (time2 - time1) / (1000);
        if (abs)
            between_s = Math.abs(between_s);
        return between_s;
    }


    /***
     * 获取星座
     *
     * @param date
     * @return
     */
    public static String getConstellation(Date date) {
        if (date == null)
            return "";
        String constellation = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        // 魔羯座12/22-1/19
        // 水瓶座1/20-2/18
        // 双鱼座2/19-3/20
        // 牡羊座3/21- 4/20
        // 金牛座4/21-5/20
        // 双子座5/21-6/21
        // 巨蟹座6/22-7/22
        // 狮子座7/23-8/22
        // 处女座8/23-9/22
        // 天秤座9/23-10/22
        // 天蝎座10/23-11/21
        // 射手座11/22-12/21
        // 魔羯座12/22-1/19
        switch (month) {
            case 1:
                if (day <= 19)
                    constellation = "魔羯";
                else
                    constellation = "水瓶";
                break;
            case 2:
                if (day <= 18)
                    constellation = "水瓶";
                else
                    constellation = "双鱼";
                break;
            case 3:
                if (day <= 20)
                    constellation = "双鱼";
                else
                    constellation = "牡羊";
                break;
            case 4:
                if (day <= 20)
                    constellation = "牡羊";
                else
                    constellation = "金牛";
                break;
            case 5:
                if (day <= 20)
                    constellation = "金牛";
                else
                    constellation = "双子";
                break;
            case 6:
                if (day <= 21)
                    constellation = "双子";
                else
                    constellation = "巨蟹";
                break;
            case 7:
                if (day <= 22)
                    constellation = "巨蟹";
                else
                    constellation = "狮子";
                break;
            case 8:
                if (day <= 22)
                    constellation = "狮子";
                else
                    constellation = "处女";
                break;
            case 9:
                if (day <= 22)
                    constellation = "处女";
                else
                    constellation = "天秤";
                break;
            case 10:
                if (day <= 22)
                    constellation = "天秤";
                else
                    constellation = "天蝎";
                break;
            case 11:
                if (day <= 21)
                    constellation = "天蝎";
                else
                    constellation = "射手";
                break;
            case 12:
                if (day <= 21)
                    constellation = "射手";
                else
                    constellation = "魔羯";
                break;

        }
        return constellation;
    }


    /**
     * 取得指定日期过 day 天后的日期 (当 day 为负数表示指日期之前);
     *
     * @param date 日期 为null时表示当天
     */
    public static Date nextDay(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.DAY_OF_YEAR, day);
        return cal.getTime();
    }


    /**
     * 将时，分，秒，以及毫秒值设置为0
     *
     * @param calendar
     */
    public static void zeroFromHour(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    /**
     * 将时间戳转换成当天零点的时间戳
     *
     * @param milliseconds
     * @return
     */
    public static Calendar zeroFromHour(long milliseconds) {
        Calendar calendar = getCalendar();
        calendar.setTimeInMillis(milliseconds);
        zeroFromHour(calendar);
        return calendar;
    }

    /**
     * 将时间戳转换成当天零点的时间戳
     *
     * @param milliseconds
     * @return
     */
    public static long zeroFromHourMilliseconds(long milliseconds) {
        return zeroFromHour(milliseconds).getTimeInMillis();
    }

    public static Calendar getCalendar() {
        return Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"), Locale.CHINA);
    }

    public static Calendar getCalendar(long milliseconds) {
        Calendar calendar = getCalendar();
        calendar.setTimeInMillis(milliseconds);
        return calendar;
    }

    /**
     * 获取当前时间零秒零毫秒的时间戳
     *
     * @return
     */
    public static Calendar getZeroSecondCalendar() {
        Calendar calendar = getCalendar();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    /**
     * 获取今天零点零小时零分零秒零毫秒的时间戳
     *
     * @return
     */
    public static long getTodayZeroTimeInMillis() {
        Calendar calendar = getZeroSecondCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取明天零点零小时零分零秒零毫秒的时间戳
     *
     * @return
     */
    public static long getTomorrowZeroTimeInMillis() {
        Calendar calendar = getZeroSecondCalendar();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTimeInMillis();
    }

    public static String formatHourMinute(Date date) {
        return sHourMinuteSdf.format(date);
    }

    public static String formatYearMonthDayHourMinute(Date date) {
        return sYearMonthDayHourMinuteSdf.format(date);
    }

    public static String formatYearMonthDayHourMinute(long milliseconds) {
        return sYearMonthDayHourMinuteSdf.format(new Date(milliseconds));
    }

    public static String formatYearMonthDay(Date date) {
        return sYearMonthDaySdf.format(date);
    }

    public static String formatYearMonthDay(long milliseconds) {
        return formatYearMonthDay(new Date(milliseconds));
    }

    public static String formatYearMonthDayWeek(Date date) {
        return sYearMonthDayWeekSdf.format(date);
    }

    public static String formatYearMonthDayWeek(long milliseconds) {
        return formatYearMonthDayWeek(new Date(milliseconds));
    }

    public static long parseYearMonthDay(String date) {
        if (date == null || date.equals("")) {
            return 0;
        }
        try {
            return sYearMonthDaySdf.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String formatChineseYearMonthDay(Date date) {
        return sChineseYearMonthDaySdf.format(date);
    }

    public static String formatChineseYearMonthDay(long milliseconds) {
        return formatChineseYearMonthDay(new Date(milliseconds));
    }

    public static String formatChineseYearMonthDayWeek(Date date) {
        return sChineseYearMonthDayWeekSdf.format(date);
    }

    public static String formatChineseYearMonthDayWeek(long milliseconds) {
        return formatChineseYearMonthDayWeek(new Date(milliseconds));
    }

    public static int daysBetween(long start, long end) {
        return new Long(end / DAY_MILLISECONDS).intValue() - new Long(start / DAY_MILLISECONDS).intValue();
    }

    public static int daysBetweenToday(long start) {
        return daysBetween(start, getCalendar().getTimeInMillis());
    }

}