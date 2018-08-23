package com.duowan.lobby.util.base;

public class LongUtil {
	/**
	 * 把字符串转换为long
	 * 
	 * @param str
	 * @return
	 */
	public static long parseLong(String str) {
		if (str == null || str.length() == 0) {
			return 0L;
		}
		try {
			return Long.parseLong(str);
		} catch (Exception e) {
			return 0L;
		}
	}

	/**
	 * 判断是否为是否可以转为long
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isLong(String str) {
		try {
			long result = Long.parseLong(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
