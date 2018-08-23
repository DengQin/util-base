package com.duowan.lobby.util.base.exception;

import com.duowan.lobby.util.base.StringUtil;

/**
 * 通用异常
 * 
 * @author tanjh
 * 
 */
public class CommonException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6580913469418525590L;

	public CommonException() {
	}

	public CommonException(String msg) {
		super(msg);
	}

	public CommonException(String msg, Throwable t) {
		super(msg, t);
	}

	public static void isTrue(boolean bool, String msg) {
		if (!bool) {
			throw new CommonException(msg);
		}
	}

	public static void assertNotBlank(String str, String msg) {
		if (StringUtil.isBlank(str)) {
			throw new CommonException(msg);
		}
	}

	public static void assertBlank(String str, String msg) {
		if (StringUtil.isNotBlank(str)) {
			throw new CommonException(msg);
		}
	}

	public static void assertNotNull(Object obj, String msg) {
		if (obj == null) {
			throw new CommonException(msg);
		}
	}

	public static void assertNull(Object obj, String msg) {
		if (obj != null) {
			throw new CommonException(msg);
		}
	}
}
