package com.duowan.lobby.util.base.exception;

import com.duowan.lobby.util.base.StringUtil;

/**
 * 可视异常，message是可以返回页面的
 * 
 * @author tanjh
 * 
 */
public class VisualException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4685700624782364996L;

	public VisualException() {
	}

	public VisualException(String msg) {
		super(msg);
	}

	public VisualException(String msg, Throwable t) {
		super(msg, t);
	}

	public static void isTrue(boolean bool, String msg) {
		if (!bool) {
			throw new VisualException(msg);
		}
	}

	public static void assertNotBlank(String str, String msg) {
		if (StringUtil.isBlank(str)) {
			throw new VisualException(msg);
		}
	}

	public static void assertBlank(String str, String msg) {
		if (StringUtil.isNotBlank(str)) {
			throw new VisualException(msg);
		}
	}

	public static void assertNotNull(Object obj, String msg) {
		if (obj == null) {
			throw new VisualException(msg);
		}
	}

	public static void assertNull(Object obj, String msg) {
		if (obj != null) {
			throw new VisualException(msg);
		}
	}

}
