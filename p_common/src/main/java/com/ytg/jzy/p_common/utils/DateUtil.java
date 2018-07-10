/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.ytg.jzy.p_common.utils;


import com.ytg.jzy.p_common.utils.enums.DateStyle;
import com.ytg.jzy.p_common.utils.enums.Week;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * 时间转换工具类
 *
 * @author Jorstin Chan@YTG
 * @version 4.0
 * @date 2014-12-10
 */
public class DateUtil {

    public static final TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
    public static final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final long ONEDAY = 86400000;
    public static final int SHOW_TYPE_SIMPLE = 0;
    public static final int SHOW_TYPE_COMPLEX = 1;
    public static final int SHOW_TYPE_ALL = 2;
    public static final int SHOW_TYPE_CALL_LOG = 3;
    public static final int SHOW_TYPE_CALL_DETAIL = 4;

    /**
     * 获取当前当天日期的毫秒数 2012-03-21的毫秒数
     *
     * @return
     */
    public static long getCurrentDayTime() {
        Date d = new Date(System.currentTimeMillis());
        String formatDate = yearFormat.format(d);
        try {
            return (yearFormat.parse(formatDate)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static String getDateString(long time, int type) {
        Calendar c = Calendar.getInstance();
        c = Calendar.getInstance(tz);
        c.setTimeInMillis(time);
        long currentTime = System.currentTimeMillis();
        Calendar current_c = Calendar.getInstance();
        current_c = Calendar.getInstance(tz);
        current_c.setTimeInMillis(currentTime);

        int currentYear = current_c.get(Calendar.YEAR);
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH) + 1;
        int d = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        long t = currentTime - time;
        long t2 = currentTime - getCurrentDayTime();
        String dateStr = "";
		if (t < t2 && t > 0) {
			if (type == SHOW_TYPE_SIMPLE) {
				dateStr = (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_COMPLEX) {
				dateStr = "今天  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_CALL_LOG) {
				dateStr = "今天  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_CALL_DETAIL) {
				dateStr = "今天  ";
			}else {
				dateStr = (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute) + ":"
						+ (second < 10 ? "0" + second : second);
			}
		} else if (t < (t2 + ONEDAY) && t > 0) {
			if (type == SHOW_TYPE_SIMPLE || type == SHOW_TYPE_CALL_DETAIL) {
				dateStr = "昨天  ";
			} else if (type == SHOW_TYPE_COMPLEX ) {
				dateStr = "昨天  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_CALL_LOG) {
				dateStr = "昨天  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else {
				dateStr = "昨天  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute) + ":"
						+ (second < 10 ? "0" + second : second);
			}
		} else
        if (y == currentYear) {
            if (type == SHOW_TYPE_SIMPLE) {
                dateStr = (m < 10 ? "0" + m : m) + "-" + (d < 10 ? "0" + d : d);
            } else if (type == SHOW_TYPE_COMPLEX) {
                dateStr = (m < 10 ? "0" + m : m) + "-" + (d < 10 ? "0" + d : d);
            } else if (type == SHOW_TYPE_CALL_LOG || type == SHOW_TYPE_COMPLEX) {
                dateStr = (m < 10 ? "0" + m : m) + "-"
                        + (d < 10 ? "0" + d : d) + " "
                        + (hour < 10 ? "0" + hour : hour) + ":"
                        + (minute < 10 ? "0" + minute : minute);
            } else if (type == SHOW_TYPE_CALL_DETAIL) {
                dateStr = y + "-" + (m < 10 ? "0" + m : m) + "-"
                        + (d < 10 ? "0" + d : d);
            } else {
                dateStr = (m < 10 ? "0" + m : m) + "-" + (d < 10 ? "0" + d : d)
                        + " " + (hour < 10 ? "0" + hour : hour) + ":"
                        + (minute < 10 ? "0" + minute : minute) + ":"
                        + (second < 10 ? "0" + second : second);
            }
        } else {
            if (type == SHOW_TYPE_SIMPLE) {
                dateStr = y + "/" + (m < 10 ? "0" + m : m) + "-"
                        + (d < 10 ? "0" + d : d);
            } else if (type == SHOW_TYPE_COMPLEX) {
                dateStr = y + "-" + (m < 10 ? "0" + m : m) + "-"
                        + (d < 10 ? "0" + d : d);
            } else if (type == SHOW_TYPE_CALL_LOG || type == SHOW_TYPE_COMPLEX) {
                dateStr = y + /* 年 */"-" + (m < 10 ? "0" + m : m) + "-"
                        + (d < 10 ? "0" + d : d) + "  ";
            } else if (type == SHOW_TYPE_CALL_DETAIL) {
                dateStr = y + "-" + (m < 10 ? "0" + m : m) + "-"
                        + (d < 10 ? "0" + d : d);
            } else {
                dateStr = y + "-" + (m < 10 ? "0" + m : m) + "-"
                        + (d < 10 ? "0" + d : d) + " "
                        + (hour < 10 ? "0" + hour : hour) + ":"
                        + (minute < 10 ? "0" + minute : minute) + ":"
                        + (second < 10 ? "0" + second : second);
            }
        }
        return dateStr;
    }
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static final SimpleDateFormat sequenceFormat = new SimpleDateFormat(
            "yyyyMMddHHmmss");
    /**
     * @return
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis() / 1000;
    }

    public static long getHistoryTime(String soure)  {
        try {
            Date date= sdf.parse(soure);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    public static long getActiveTimelong(String result) {
        try {
            Date parse = yearFormat.parse(result);
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    public static String getDefaultFormat() {
        return getDateFormat(sequenceFormat);
    }

    public static String getDateFormat(DateFormat df) {
        return getDateFormat(System.currentTimeMillis(), df);
    }

    public static String getDataFormat(long currentMillis) {
        return getDateFormat(currentMillis, sequenceFormat);
    }
    public static String getDataFormatBysdf(long currentMillis) {
        return getDateFormat(currentMillis, sdf);
    }
    public static String getDateFormat(long time, DateFormat df) {
        Date d = new Date(time);
        return df.format(d);
    }



    public static String toGMTString(long milliseconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM y HH:mm:ss 'GMT'", Locale.US);
        TimeZone gmtZone = TimeZone.getTimeZone("GMT");
        sdf.setTimeZone(gmtZone);
        GregorianCalendar gc = new GregorianCalendar(gmtZone);
        gc.setTimeInMillis(milliseconds);
        return sdf.format(new Date(milliseconds));
    }



    public static String formatCallTime(long time) {
        return time >= 3600 ? String.format(Locale.US, "%d:%02d:%02d", (time / 3600), (time % 3600 / 60), (time % 60)) :
                String.format(Locale.US, "%d:%02d", (time / 60), (time % 60));
    }

    /**
     * 转换收藏时间
     *
     * @param time
     * @return
     */
    public static String getFavoriteTime(String time) {
        //当前时间
        long currentTime = System.currentTimeMillis();
        Date oldDate = new Date(Long.parseLong(time));
        Date currentDate = new Date(currentTime);
        int dDay = differentDays(currentDate, oldDate);
        if (dDay == 0) {
            //1天之内
            return "今天";
        } else if (dDay == 1) {
            return "昨天 ";
        } else if (dDay > 1 && dDay < 8) {
            return dDay + "天前";
        } else {
            return getDate(oldDate, DateStyle.YYYY_MM_DD_EN);
        }
    }

    public static int differentDays(Date currentDate, Date oldDate) {
        Calendar oldCalendar = Calendar.getInstance();
        oldCalendar.setTime(oldDate);
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(currentDate);
        int oldDay = oldCalendar.get(Calendar.DAY_OF_YEAR);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_YEAR);
        int oldYear = oldCalendar.get(Calendar.YEAR);
        int currentYear = currentCalendar.get(Calendar.YEAR);
        if (oldYear != currentYear) {
            int timeDistance = 0;
            for (int i = oldYear; i < currentYear; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    timeDistance += 366;
                } else {
                    timeDistance += 365;
                }
            }
            return timeDistance + (currentDay - oldDay);
        } else {
            return currentDay - oldDay;
        }
    }

    /**
     * 获取日期
     *
     * @param date
     * @param dateStyle
     * @return
     */
    public static String getDate(Date date, DateStyle dateStyle) {
        return DateToString(date, dateStyle);
    }

    /**
     * 将日期转化为日期字符串。失败返回null。
     *
     * @param date      日期
     * @param dateStyle 日期风格
     * @return 日期字符串
     */
    public static String DateToString(Date date, DateStyle dateStyle) {
        String dateString = null;
        if (dateStyle != null) {
            dateString = DateToString(date, dateStyle.getValue());
        }
        return dateString;
    }

    /**
     * 将日期转化为日期字符串。失败返回null。
     *
     * @param date     日期
     * @param parttern 日期格式
     * @return 日期字符串
     */
    public static String DateToString(Date date, String parttern) {
        String dateString = null;
        if (date != null) {
            try {
                dateString = getDateFormat(parttern).format(date);
            } catch (Exception e) {
            }
        }
        return dateString;
    }

    public static String formatDateItem(int item){
        if(item < 10){
            return "0" + item;
        }
        return item + "";
    }
    /**
     * 普通时间显示
     * @param time
     * @return
     */
    public static String getNormalTime(long time){
        //当前时间
        long currentTime = System.currentTimeMillis();
        Date oldDate = new Date(time);
        Date currentDate = new Date(currentTime);

        int oldHour = getHour(oldDate);
        int oldMinute = getMinute(oldDate);

        int dDay = differentDays(currentDate, oldDate);

        String resultTime = formatDateItem(oldHour) + ":" + formatDateItem(oldMinute);
        if(dDay == 0){
            //两个时间在同一天之内，显示几点积分
            return resultTime;
        } else if(dDay == 1){
            //两个时间不在同一天之内，今天与昨天
            return "昨天 " + resultTime;
        } else if(dDay < 7 && dDay > 1){
            //两个时间在七天之内, 显示星期数
            Week week = getWeek(oldDate);
            return week.getChineseName() + " " + resultTime;
        } else{
            //显示具体时间，2016/11/29
            return getDate(oldDate, DateStyle.YYYY_MM_DD_HH_MM_CN);
        }
    }

    /**
     * 转换朋友圈时间
     * @param time
     * @return
     */
    public static String getCircleTime(long time, boolean showDetailTime){
        //当前时间
        long currentTime = System.currentTimeMillis();
        Date oldDate = new Date(time);
        Date currentDate = new Date(currentTime);
        int currentHour = getHour(currentDate);
        int currentMinute = getMinute(currentDate);
        int oldHour = getHour(oldDate);
        int oldMinute = getMinute(oldDate);

        int dHour = currentHour - oldHour;
        int dMinute= currentMinute - oldMinute;
        int dDay = differentDays(currentDate, oldDate);


        Date d = new Date(currentTime);
        Date d2 = new Date(time);
        long c =  Math.abs( d.getTime() - d2.getTime()) / (1000 * 60);
        if (c < 60 && dHour == 1){
            return c + "分钟前";
        } else{
            if(dDay == 0 && dHour == 0 && dMinute == 0){
                //1分钟之内
                return "刚刚";
            } else if(dDay == 0 && dHour == 0){
                //1小时之内
                return dMinute + "分钟前";
            } else if(dDay == 0){
                //1天之内
                return dHour + "小时前";
            } else if(dDay == 1){
                return "昨天";
            } else if(dDay <= 16){
                return dDay + "天前";
            } else{
                //显示具体时间，2016年11月29日 23:44
                if (showDetailTime){
                    return getDate(oldDate, DateStyle.YYYY_MM_DD_HH_MM_CN);
                } else{
                    return getDate(oldDate, DateStyle.YYYY_MM_DD_CN);
                }
            }
        }
    }

    /**
     * 获取日期的星期。失败返回null。
     * @param date 日期字符串
     * @return 星期
     */
    public static Week getWeek(String date) {
        Week week = null;
        DateStyle dateStyle = getDateStyle(date);
        if (dateStyle != null) {
            Date myDate = StringToDate(date, dateStyle);
            week = getWeek(myDate);
        }
        return week;
    }
    /**
     * 获取日期字符串的日期风格。失敗返回null。
     * @param date 日期字符串
     * @return 日期风格
     */
    public static DateStyle getDateStyle(String date) {
        DateStyle dateStyle = null;
        Map<Long, DateStyle> map = new HashMap<Long, DateStyle>();
        List<Long> timestamps = new ArrayList<Long>();
        for (DateStyle style : DateStyle.values()) {
            Date dateTmp = StringToDate(date, style.getValue());
            if (dateTmp != null) {
                timestamps.add(dateTmp.getTime());
                map.put(dateTmp.getTime(), style);
            }
        }
        dateStyle = map.get(getAccurateDate(timestamps).getTime());
        return dateStyle;
    }
    /**
     * 获取日期的星期。失败返回null。
     * @param date 日期
     * @return 星期
     */
    public static Week getWeek(Date date) {
        Week week = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekNumber = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        switch (weekNumber) {
            case 0:
                week = Week.SUNDAY;
                break;
            case 1:
                week = Week.MONDAY;
                break;
            case 2:
                week = Week.TUESDAY;
                break;
            case 3:
                week = Week.WEDNESDAY;
                break;
            case 4:
                week = Week.THURSDAY;
                break;
            case 5:
                week = Week.FRIDAY;
                break;
            case 6:
                week = Week.SATURDAY;
                break;
        }
        return week;
    }

    /**formatDuring
     * 获取SimpleDateFormat
     *
     * @param parttern 日期格式
     * @return SimpleDateFormat对象
     * @throws RuntimeException 异常：非法日期格式
     */
    private static SimpleDateFormat getDateFormat(String parttern) throws RuntimeException {
        return new SimpleDateFormat(parttern);
    }
    /**
     * 获取日期的小时。失败返回0。
     * @param date 日期
     * @return 小时
     */
    public static int getHour(Date date) {
        return getInteger(date, Calendar.HOUR_OF_DAY);
    }
    /**
     * 获取日期的分钟。失败返回0。
     * @param date 日期字符串
     * @return 分钟
     */
    public static int getMinute(String date) {
        return getMinute(StringToDate(date));
    }

    /**
     * 获取日期的分钟。失败返回0。
     * @param date 日期
     * @return 分钟
     */
    public static int getMinute(Date date) {
        return getInteger(date, Calendar.MINUTE);
    }
    /**
     * 将日期字符串转化为日期。失败返回null。
     * @param date 日期字符串
     * @return 日期
     */
    public static Date StringToDate(String date) {
        DateStyle dateStyle = null;
        return StringToDate(date, dateStyle);
    }
    /**
     * 将日期字符串转化为日期。失败返回null。
     * @param date 日期字符串
     * @param parttern 日期格式
     * @return 日期
     */
    public static Date StringToDate(String date, String parttern) {
        Date myDate = null;
        if (date != null) {
            try {
                myDate = getDateFormat(parttern).parse(date);
            } catch (Exception e) {
            }
        }
        return myDate;
    }
    /**
     * 将日期字符串转化为日期。失败返回null。
     * @param date 日期字符串
     * @param dateStyle 日期风格
     * @return 日期
     */
    public static Date StringToDate(String date, DateStyle dateStyle) {
        Date myDate = null;
        if (dateStyle == null) {
            List<Long> timestamps = new ArrayList<Long>();
            for (DateStyle style : DateStyle.values()) {
                Date dateTmp = StringToDate(date, style.getValue());
                if (dateTmp != null) {
                    timestamps.add(dateTmp.getTime());
                }
            }
            myDate = getAccurateDate(timestamps);
        } else {
            myDate = StringToDate(date, dateStyle.getValue());
        }
        return myDate;
    }

    /**
     * 获取精确的日期
     * @param timestamps 时间long集合
     * @return 日期
     */
    private static Date getAccurateDate(List<Long> timestamps) {
        Date date = null;
        long timestamp = 0;
        Map<Long, long[]> map = new HashMap<Long, long[]>();
        List<Long> absoluteValues = new ArrayList<Long>();

        if (timestamps != null && timestamps.size() > 0) {
            if (timestamps.size() > 1) {
                for (int i = 0; i < timestamps.size(); i++) {
                    for (int j = i + 1; j < timestamps.size(); j++) {
                        long absoluteValue = Math.abs(timestamps.get(i) - timestamps.get(j));
                        absoluteValues.add(absoluteValue);
                        long[] timestampTmp = { timestamps.get(i), timestamps.get(j) };
                        map.put(absoluteValue, timestampTmp);
                    }
                }

                // 有可能有相等的情况。如2012-11和2012-11-01。时间戳是相等的
                long minAbsoluteValue = -1;
                if (!absoluteValues.isEmpty()) {
                    // 如果timestamps的size为2，这是差值只有一个，因此要给默认值
                    minAbsoluteValue = absoluteValues.get(0);
                }
                for (int i = 0; i < absoluteValues.size(); i++) {
                    for (int j = i + 1; j < absoluteValues.size(); j++) {
                        if (absoluteValues.get(i) > absoluteValues.get(j)) {
                            minAbsoluteValue = absoluteValues.get(j);
                        } else {
                            minAbsoluteValue = absoluteValues.get(i);
                        }
                    }
                }

                if (minAbsoluteValue != -1) {
                    long[] timestampsLastTmp = map.get(minAbsoluteValue);
                    if (absoluteValues.size() > 1) {
                        timestamp = Math.max(timestampsLastTmp[0], timestampsLastTmp[1]);
                    } else if (absoluteValues.size() == 1) {
                        // 当timestamps的size为2，需要与当前时间作为参照
                        long dateOne = timestampsLastTmp[0];
                        long dateTwo = timestampsLastTmp[1];
                        if ((Math.abs(dateOne - dateTwo)) < 100000000000L) {
                            timestamp = Math.max(timestampsLastTmp[0], timestampsLastTmp[1]);
                        } else {
                            long now = new Date().getTime();
                            if (Math.abs(dateOne - now) <= Math.abs(dateTwo - now)) {
                                timestamp = dateOne;
                            } else {
                                timestamp = dateTwo;
                            }
                        }
                    }
                }
            } else {
                timestamp = timestamps.get(0);
            }
        }

        if (timestamp != 0) {
            date = new Date(timestamp);
        }
        return date;
    }
    /**
     * 获取日期中的某数值。如获取月份
     * @param date 日期
     * @param dateType 日期格式
     * @return 数值
     */
    private static int getInteger(Date date, int dateType) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(dateType);
    }

    public static boolean isReadTime(long litme){
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH) - 7;
        c.set(Calendar.DAY_OF_MONTH, day);
        Date datetime = c.getTime();
        long dtime = datetime.getTime();
        LogUtil.i("delMessagesByMsgtime",datetime.getTime()+"-----datetime-----"+DateUtil.getDataFormatBysdf(datetime.getTime())+"litme---"+litme);
//        如果消息的时间 是七天之前都是不可读的
        if(litme<=dtime){
            return  false;
        }
        return  true;
    }


}
