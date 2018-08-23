package com.duowan.lobby.util.base;

public class IntUtil {
	// private static Logger log = LoggerFactory.getLogger(Util.class);

	public static int compare(int i1, int i2) {
		if (i1 > i2) {
			return 1;
		} else if (i1 == i2) {
			return 0;
		} else {
			return -1;
		}
	}

	public static int parseInt(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 把字符串转换为非负数,非有效数字字符串或小于0的数返回0
	 * 
	 */
	public static int parseNonnegative(String s) {
		int v = parseInt(s);
		return v < 0 ? 0 : v;
	}

	/**
	 * 把字符串转换为正数,非有效数字字符串或小于1的数返回1
	 * 
	 * @param s
	 * @return
	 */
	public static int parsePositive(String s) {
		int v = parseInt(s);
		return v < 1 ? 1 : v;
	}

}
