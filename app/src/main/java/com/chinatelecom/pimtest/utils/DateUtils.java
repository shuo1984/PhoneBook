package com.chinatelecom.pimtest.utils;

import android.content.ContentResolver;
import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Shuo on 2016/10/8.
 */


public class DateUtils{

    public static final String FMT_DATE = "yyyy-MM-dd";

    public static final String FMT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FMT_DATETIME_EN="yyyy/MM/dd HH:mm:ss";
    public static final String FMT_DATETIME_CHINESE="yyyy年MM月dd日 HH:mm:ss";


    /**
     * 获取某天的午夜时间
     *
     * @param date 时间
     * @return 午夜时间
     */
    public static Date getMidnight(Calendar date) {
        Calendar now = new GregorianCalendar();
        now.setTime(date.getTime());
        now.add(Calendar.DATE, -1);
        now = new GregorianCalendar(
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DATE), 23, 59, 59);
        return now.getTime();
    }

    /**
     * @return 当天开始到现在的时间范围
     */
    public static DateRange getToday() {
        Calendar now = new GregorianCalendar();
        return new DateRange(getMidnight(now), new Date());
    }

    /**
     * @return 本周开始到现在的时间范围
     */
    public static DateRange getThisWeek() {
        Calendar now = new GregorianCalendar();
        now.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return new DateRange(getMidnight(now), new Date());
    }

    /**
     * @return 本月开始到现在的时间范围
     */
    public static DateRange getThisMonth() {
        Calendar now = new GregorianCalendar();
        now.set(Calendar.DAY_OF_MONTH, 1);
        return new DateRange(getMidnight(now), new Date());
    }

    public static Date parse(String format, String date) {
        DateFormat df = new SimpleDateFormat(format);
        try {
            return df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date parse( String date) {
        return parse(FMT_DATETIME,date);
    }


    public static String format(Date date, String format) {
        return new SimpleDateFormat(format).format(date);

    }

    public static String formatDate(Date date) {
        return format(date, FMT_DATE);
    }

    public static String formatDateTime(Date date) {
        return format(date, FMT_DATETIME);
    }

    //日期格式化
    public static String formatDate(String date, String format) {
        return new SimpleDateFormat(format).format(Long.valueOf(date));
    }

    public static Calendar createCalendar(String format, String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parse(format, date));
        return calendar;
    }

    public static Calendar createCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static String format(Date date) {
        return format(date, false);
    }

    public static String format(Long time) {
        String pattern = "yyyy-MM-dd HH:mm";
        return new SimpleDateFormat(pattern).format(new Date(time));
    }

    public static String format(Long time, String pattern) {
        return new SimpleDateFormat(pattern).format(new Date(time));

    }

    public static String format(Date date, boolean useChinese) {
        Calendar now = new GregorianCalendar();
        Calendar input = new GregorianCalendar();
        input.setTime(date);
        String pattern = null;
        if (sameYear(now, input) && sameMonth(now, input) && sameDay(now, input)) {
            pattern = "HH:mm";
        } else if (sameYear(now, input) && sameMonth(now, input) && !sameDay(now, input) && (now.getTimeInMillis() - date.getTime() < 1 * 24 * 60 * 60 * 1000)) {
            pattern = "昨天";
        } else if (sameYear(now, input) && sameMonth(now, input) && (now.getTimeInMillis() - date.getTime() > 1 * 24 * 60 * 60 * 1000
                && now.getTimeInMillis() - date.getTime() < 2 * 24 * 60 * 60 * 1000)) {
            pattern = "前天";
        }
//        else if (sameYear(now, input) && sameMonth(now, input) &&
//                (getThisWeek().getStart().before(date) && getThisWeek().getEnd().after(date))) {
//            pattern = getWeekOfDate(date);
//        }
        else if (sameYear(now, input) && sameMonth(now, input) && (now.getTimeInMillis() - date.getTime() > 2 * 24 * 60 * 60 * 1000
                && now.getTimeInMillis() - date.getTime() <4 * 24 * 60 * 60 * 1000)) {
            pattern = getWeekOfDate(date);
        }
//        else if (sameYear(now, input)) {
//            pattern = useChinese ? "MM月dd日" : "MM/dd";
//        }
        else {
            pattern = useChinese ? "yy年MM月dd日" : "yyyy/MM/dd";
        }
        return new SimpleDateFormat(pattern).format(date);
    }


    /**
     * 获取当前日期是星期几<br>
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }


    public static String format(Context context, Date date) {
        Calendar now = new GregorianCalendar();
        Calendar input = new GregorianCalendar();
        input.setTime(date);
        String pattern = null;
        if (sameYear(now, input) && sameMonth(now, input) && sameDay(now, input)) {
            ContentResolver cv = context.getContentResolver();
            String strTimeFormat = android.provider.Settings.System.getString(cv, android.provider.Settings.System.TIME_12_24);
            pattern = "HH:mm";
            if (strTimeFormat != null) {
                if (strTimeFormat.equals("24")) {
                    pattern = "HH:mm";
                } else {
                    pattern = " a h:mm";
                }
            }
        } else if (sameYear(now, input)) {
            pattern = "MM/dd";
        } else {
            pattern = "yyyy/MM/dd";
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    public static CharSequence currentDataFormat(Date date) {
        Calendar now = Calendar.getInstance();
        Calendar input = new GregorianCalendar();
        input.setTime(date);
        String pattern = null;
        long create = date.getTime();
        long ms  = 1000*(now.get(Calendar.HOUR_OF_DAY)*3600+now.get(Calendar.MINUTE)*60+now.get(Calendar.SECOND));//毫秒数
        long ms_now = now.getTimeInMillis();
        //严格时间天
        /*if (sameYear(now, input) && sameMonth(now, input) && sameDay(now, input)) {
            return "今天";
        } else if (sameYear(now, input) && sameMonth(now, input) && !sameDay(now, input) && (now.getTimeInMillis() - date.getTime() < 1 * 24 * 60 * 60 * 1000)) {
            return "昨天";
        } else if (sameYear(now, input) && sameMonth(now, input) && (now.getTimeInMillis() - date.getTime() > 1 * 24 * 60 * 60 * 1000
                && now.getTimeInMillis() - date.getTime() < 2 * 24 * 60 * 60 * 1000)) {
            return "前天";
        }*/
        //自然天
        if(ms_now-create<ms) {
            return "今天";
        }else if(ms_now-create<(ms+24*3600*1000)) {
            return "昨天";
        }else if(ms_now-create<(ms+24*3600*1000*2)){
            return "前天";
        } else if (sameYear(now, input) && sameMonth(now, input) &&
                (getThisWeek().getStart().before(date) && getThisWeek().getEnd().after(date))) {
            return getWeekOfDate(date);
        }else if (sameYear(now, input) && sameMonth(now, input) &&
                (getThisWeek().getStart().before(date) && getThisWeek().getEnd().after(date))) {
            return getWeekOfDate(date);
        } else if (sameYear(now, input)) {
            pattern = "MM月dd日";
        } else {
            pattern = "yy年MM月dd日";
        }
        return new SimpleDateFormat(pattern).format(date);
//        return new SimpleDateFormat("MM月dd日").format(input);
    }

    public static CharSequence currentTimeFormat(Date input) {
        return new SimpleDateFormat("HH:mm").format(input);
    }

    public static CharSequence currentAllDateFormat(Date input) {
        return new SimpleDateFormat("MM月dd日 HH:mm").format(input);
    }

    private static long interval(Calendar left, Calendar right) {
        long leftDate = left.getTime().getTime();
        long rightDate = right.getTime().getTime();
        return leftDate - rightDate;
    }

    private static boolean sameYear(Calendar left, Calendar right) {
        return left.get(Calendar.YEAR) == right.get(Calendar.YEAR);
    }

    private static boolean sameMonth(Calendar left, Calendar right) {
        return left.get(Calendar.MONTH) == right.get(Calendar.MONTH);
    }

    public static boolean isDateInCurrentWeek(Date date) {
        Calendar currentCalendar = Calendar.getInstance();
        int week = currentCalendar.get(Calendar.WEEK_OF_YEAR);
        int year = currentCalendar.get(Calendar.YEAR);
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(date);
        int targetWeek = targetCalendar.get(Calendar.WEEK_OF_YEAR);
        int targetYear = targetCalendar.get(Calendar.YEAR);
        return week == targetWeek && year == targetYear;
    }

    private static boolean sameDay(Calendar left, Calendar right) {
        return left.get(Calendar.DATE) == right.get(Calendar.DATE);
    }

    public static boolean sameDay(long time1, long time2) {
        Calendar left = new GregorianCalendar();
        left.setTimeInMillis(time1);
        Calendar right = new GregorianCalendar();
        right.setTimeInMillis(time2);
        return sameDay(left, right);
    }

    public static boolean today(long time) {
        Calendar now = new GregorianCalendar();
        Calendar input = new GregorianCalendar();
        input.setTimeInMillis(time);
        return sameYear(now, input) && sameMonth(now, input) && sameDay(now, input);
    }

    public static CharSequence longFormat(Date input) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(input);
    }

    public static String middleFormat(Date date) {
        Calendar now = new GregorianCalendar();
        Calendar input = new GregorianCalendar();
        input.setTime(date);
        String pattern = null;
        if (sameYear(now, input) && sameMonth(now, input) && sameDay(now, input)) {
            pattern = "HH:mm";
        } else if (sameYear(now, input)) {
            pattern = "MM/dd HH:mm";
        } else {
            pattern = "yyyy/MM/dd HH:mm";
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    public static float getDayCountForDate(long todayMs, long returnMs) {
        long intervalMs = todayMs - returnMs;
        return (float) intervalMs / (1000 * 86400);
//        return (float) intervalMs / (1000 * 10);
    }

    /**
     * left 是否在 right 之后
     *
     * @param left  left
     * @param right right
     * @return before?
     */
    public static boolean after(Date left, Date right) {
        return left != null && (right == null || left.after(right));
    }

    public static boolean isSameDay(final Date date1, final Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        final Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        final Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    /**
     * 判断时间是否在时间段内
     *
     * @param date
     *            当前时间 yyyy-MM-dd HH:mm:ss
     * @param strDateBegin
     *            开始时间 00:00:00
     * @param strDateEnd
     *            结束时间 00:05:00
     * @return
     */
    public static boolean isInDate(Date date, String strDateBegin,
                                   String strDateEnd) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(date);
        // 截取当前时间时分秒
        int strDateH = Integer.parseInt(strDate.substring(11, 13));
        int strDateM = Integer.parseInt(strDate.substring(14, 16));
        int strDateS = Integer.parseInt(strDate.substring(17, 19));
        // 截取开始时间时分秒
        int strDateBeginH = Integer.parseInt(strDateBegin.substring(0, 2));
        int strDateBeginM = Integer.parseInt(strDateBegin.substring(3, 5));
        int strDateBeginS = Integer.parseInt(strDateBegin.substring(6, 8));
        // 截取结束时间时分秒
        int strDateEndH = Integer.parseInt(strDateEnd.substring(0, 2));
        int strDateEndM = Integer.parseInt(strDateEnd.substring(3, 5));
        int strDateEndS = Integer.parseInt(strDateEnd.substring(6, 8));
        if ((strDateH >= strDateBeginH && strDateH <= strDateEndH)) {
            // 当前时间小时数在开始时间和结束时间小时数之间
            if (strDateH > strDateBeginH && strDateH < strDateEndH) {
                return true;
                // 当前时间小时数等于开始时间小时数，分钟数在开始和结束之间
            } else if (strDateH == strDateBeginH && strDateM >= strDateBeginM
                    && strDateM <= strDateEndM) {
                return true;
                // 当前时间小时数等于开始时间小时数，分钟数等于开始时间分钟数，秒数在开始和结束之间
            } else if (strDateH == strDateBeginH && strDateM == strDateBeginM
                    && strDateS >= strDateBeginS && strDateS <= strDateEndS) {
                return true;
            }
            // 当前时间小时数大等于开始时间小时数，等于结束时间小时数，分钟数小等于结束时间分钟数
            else if (strDateH >= strDateBeginH && strDateH == strDateEndH
                    && strDateM <= strDateEndM) {
                return true;
                // 当前时间小时数大等于开始时间小时数，等于结束时间小时数，分钟数等于结束时间分钟数，秒数小等于结束时间秒数
            } else if (strDateH >= strDateBeginH && strDateH == strDateEndH
                    && strDateM == strDateEndM && strDateS <= strDateEndS) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * <p>Checks if two calendar objects are on the same day ignoring time.</p>
     *
     * <p>28 Mar 2002 13:45 and 28 Mar 2002 06:01 would return true.
     * 28 Mar 2002 13:45 and 12 Mar 2002 13:45 would return false.
     * </p>
     *
     * @param cal1  the first calendar, not altered, not null
     * @param cal2  the second calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is <code>null</code>
     * @since 2.1
     */
    public static boolean isSameDay(final Calendar cal1, final Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
    public static Date addDays(final Date date, final int amount) {
        return add(date, Calendar.DAY_OF_MONTH, amount);
    }
    private static Date add(final Date date, final int calendarField, final int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }
}
