package com.duowan.lobby.util.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.duowan.lobby.util.base.exception.VisualException;

public class DateUtil {

	private static final String RANDOM_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static boolean isBetween(Date date, Date start, Date end) {
		long dateTs = date.getTime();
		long startTs = start.getTime();
		long endTs = end.getTime();
		VisualException.isTrue(endTs >= startTs, "参数错误，end早于start");
		if (dateTs < startTs) {
			return false;
		}
		if (dateTs > endTs) {
			return false;
		}
		return true;
	}

	/**
	 * 获得currentTimeMillis的压缩字符串
	 */
	public static String getTimeString() {
		return getTimeString(System.currentTimeMillis());
	}

	/**
	 * 获得currentTimeMillis的压缩字符串
	 */
	public static String getTimeString(long time) {
		char[] arr = RANDOM_CHARS.toCharArray();
		StringBuilder str = new StringBuilder();
		int length = arr.length;
		while (time != 0) {
			str.append(arr[(int) (time % length)]);
			time = time / length;
		}
		return str.toString();
	}

	/**
	 * 返回压缩字符串对应的时间
	 */
	public static long getTime(String compressorStr) {
		char[] arr = compressorStr.toCharArray();
		long result = 0;
		int length = RANDOM_CHARS.length();
		for (int i = arr.length - 1; i >= 0; i--) {
			char c = arr[i];
			int index = RANDOM_CHARS.indexOf(c);
			result = length * result + index;
		}
		return result;
	}

	/**
	 * 判断是否合法的日期
	 */
	public static boolean isLegalDate(int year, int month, int day) {
		if (year < 1 || month < 1 || month > 12 || day < 1 || day > 31) {
			return false;
		}
		if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
			return false;
		}
		if (month == 2) {
			if ((year % 4 != 0) && day > 28) {
				return false;
			}
			if (year % 4 == 0) {
				if (year % 100 == 0 && year % 400 != 0) {
					if (day > 28)
						return false;
				} else if (day > 29) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 获取当前日期的字符串
	 * 
	 * @param pattern
	 *            --格式
	 */
	public static String getCurrentDateStr(String pattern) {
		SimpleDateFormat sf = new SimpleDateFormat(pattern);
		Date dt = new Date(System.currentTimeMillis());
		return sf.format(dt);
	}

	/**
	 * 字符串转为日期
	 */
	public static Date strToDate(String dateStr, String pattern) throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat(pattern);
		return sf.parse(dateStr);
	}

	public static Date strToDate(String dateStr) throws ParseException {
		return strToDate(dateStr, DateFormats.DATE_FORMAT);
	}

	public static Date strToTime(String dateStr) throws ParseException {
		return strToDate(dateStr, DateFormats.DATE_TIME_FORMAT);
	}

	/**
	 * 格式化日期
	 */
	public static String formatDate(Date date, String pattern) {
		if (date == null)
			return null;
		SimpleDateFormat sf = new SimpleDateFormat(pattern);
		return sf.format(date);
	}

	/**
	 * 返回年月日
	 */
	public static String formatDate(Date date) {
		return formatDate(date, DateFormats.DATE_FORMAT);
	}

	/**
	 * 返回年月日时分秒
	 */
	public static String formatTime(Date date) {
		return formatDate(date, DateFormats.DATE_TIME_FORMAT);
	}

	/**
	 * 日期增减
	 */
	public static Date addDay(Date date, int dayNum) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, dayNum);
		return calendar.getTime();
	}

	/**
	 * 获取自然周
	 */
	public static String getCurrnetWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int week = calendar.get(Calendar.WEEK_OF_YEAR);
		int year = calendar.get(Calendar.YEAR);
		if (week < 10) {
			return year + "0" + week;
		}
		return year + "" + week;
	}

	/**
	 * date：2013-01-01 10:00:00 返回：2013-01-01 00:00:00
	 */
	public static Date getStartOfDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		Calendar t = Calendar.getInstance();
		t.clear();
		t.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		return t.getTime();
	}

	/**
	 * date：2013-01-01 10:00:00 返回：2013-01-01 23:59:59
	 */
	public static Date getEndOfDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		Calendar t = Calendar.getInstance();
		t.clear();
		t.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		return t.getTime();
	}

	/**
	 * 获取参数所在月的第一天
	 */
	public static Date getFirstDayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		Calendar t = Calendar.getInstance();
		t.clear();
		t.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1, 0, 0, 0);
		return t.getTime();
	}

	/**
	 * 获取参数所在月的最后一天
	 */
	public static Date getLastDayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 1);

		Calendar t = Calendar.getInstance();
		t.clear();
		t.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
		t.add(Calendar.DAY_OF_MONTH, -1);
		return t.getTime();
	}

	/**
	 * 获取2个日期的天数差
	 */
	public static int getAbsDifferDays(Date date1, Date date2) {
		long day = Math.abs((date1.getTime() - date2.getTime())) / (24L * 3600 * 1000);
		return (int) (Math.abs(day));
	}
}
