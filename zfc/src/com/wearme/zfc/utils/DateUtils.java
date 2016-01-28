package com.wearme.zfc.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类 Created by bob on 2015/2/28.
 */
public class DateUtils {
	
	private static final long ONE_MINUTE = 60000L;
	private static final long ONE_HOUR = 3600000L;
	private static final long ONE_DAY = 86400000L;
	private static final long ONE_WEEK = 604800000L;

	private static final String ONE_SECOND_AGO = "秒前";
	private static final String ONE_MINUTE_AGO = "分钟前";
	private static final String ONE_HOUR_AGO = "小时前";
	private static final String ONE_DAY_AGO = "天前";
	private static final String ONE_MONTH_AGO = "月前";
	private static final String ONE_YEAR_AGO = "年前";
	
	public static String format(Date date) {
		long delta = new Date().getTime() - date.getTime();
		if (delta < 1L * ONE_MINUTE) {
			long seconds = toSeconds(delta);
			return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
		}
		if (delta < 45L * ONE_MINUTE) {
			long minutes = toMinutes(delta);
			return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
		}
		if (delta < 24L * ONE_HOUR) {
			long hours = toHours(delta);
			return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
		}
		if (delta < 48L * ONE_HOUR) {
			return "昨天";
		}
		if (delta < 30L * ONE_DAY) {
			long days = toDays(delta);
			return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
		}
		if (delta < 12L * 4L * ONE_WEEK) {
			long months = toMonths(delta);
			return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
		} else {
			long years = toYears(delta);
			return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
		}
	}
	
	public static int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}

	private static long toSeconds(long date) {
		return date / 1000L;
	}

	private static long toMinutes(long date) {
		return toSeconds(date) / 60L;
	}

	private static long toHours(long date) {
		return toMinutes(date) / 60L;
	}

	private static long toDays(long date) {
		return toHours(date) / 24L;
	}

	private static long toMonths(long date) {
		return toDays(date) / 30L;
	}

	private static long toYears(long date) {
		return toMonths(date) / 365L;
	}

	public static String showTime(long oldTime) {
		
		String timer ; 
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
		Date duraTime = new Date(oldTime);
		timer = format(duraTime) ; 
		return timer;
	}

	public static String getFormatDate(long currentTime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(currentTime);
		return formatter.format(date);
	}

	/**
	 * 得到当前时间
	 * 
	 * @param dateFormat
	 *            时间格式
	 * @return 转换后的时间格式
	 */
	public static String getStringToday(String dateFormat) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 将字符串型日期转换成日期
	 * 
	 * @param dateStr
	 *            字符串型日期
	 * @param dateFormat
	 *            日期格式
	 * @return
	 */
	public static Date stringToDate(String dateStr, String dateFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		try {
			return formatter.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 日期转字符串
	 * 
	 * @param date
	 * @param dateFormat
	 * @return
	 */
	public static String dateToString(Date date, String dateFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		return formatter.format(date);
	}

	/**
	 * 两个时间点的间隔时长（分钟）
	 * 
	 * @param before
	 *            开始时间
	 * @param after
	 *            结束时间
	 * @return 两个时间点的间隔时长（分钟）
	 */
	public static long compareMin(Date before, Date after) {
		if (before == null || after == null) {
			return 0l;
		}
		long dif = 0;
		if (after.getTime() >= before.getTime()) {
			dif = after.getTime() - before.getTime();
		} else if (after.getTime() < before.getTime()) {
			dif = after.getTime() + 86400000 - before.getTime();
		}
		dif = Math.abs(dif);
		return dif / 60000;
	}

	/**
	 * 获取指定时间间隔分钟后的时间
	 * 
	 * @param date
	 *            指定的时间
	 * @param min
	 *            间隔分钟数
	 * @return 间隔分钟数后的时间
	 */
	public static Date addMinutes(Date date, int min) {
		if (date == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, min);
		return calendar.getTime();
	}

	/**
	 * 根据时间返回指定术语，自娱自乐，可自行调整
	 * 
	 * @param hourday
	 *            小时
	 * @return
	 */
	public static String showTimeView(int hourday) {
		if (hourday >= 22 && hourday <= 24) {
			return "晚上";
		} else if (hourday >= 0 && hourday <= 6) {
			return "凌晨";
		} else if (hourday > 6 && hourday <= 12) {
			return "上午";
		} else if (hourday > 12 && hourday < 22) {
			return "下午";
		}
		return null;
	}

}
