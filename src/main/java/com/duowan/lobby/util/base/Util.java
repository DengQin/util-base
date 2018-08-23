package com.duowan.lobby.util.base;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {

	private static Logger log = LoggerFactory.getLogger(Util.class);

	/**
	 * 做安全验证,需要配置nginx的proxy_set_header X-Real-IP $remote_addr;
	 *
	 * @param request
	 * @return
	 */
	public static String getRealIp(HttpServletRequest request) {
		return StringUtil.trim(request.getHeader("X-Real-IP"));
	}

	/**
	 * 做日志记录的,获取用户原始ip,这个可以伪造的
	 *
	 * @param request
	 * @return
	 */
	public static String getOriginIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (StringUtil.isBlank(ip)) {
			return request.getRemoteAddr();
		}
		int index = ip.indexOf(',');
		String clientIp = ip;
		if (index > 0) {
			clientIp = ip.substring(0, index).trim();
		}
		if ("127.0.0.1".equals(clientIp) || !isLicitIp(clientIp)) {
			return request.getRemoteAddr();
		}
		return clientIp;
	}

	private static final java.util.regex.Pattern IS_LICIT_IP_PATTERN = java.util.regex.Pattern
			.compile("^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$");

	public static boolean isLicitIp(final String ip) {
		if (StringUtil.isBlank(ip)) {
			return false;
		}
		Matcher m = IS_LICIT_IP_PATTERN.matcher(ip);
		if (!m.find()) {
			return false;
		}
		return true;
	}

	/**
	 * 获取字节数(一个中文相当于2个字节).
	 *
	 * @param str
	 * @return
	 */
	public static int getBytes(String str) {
		if (StringUtil.isBlank(str)) {
			return 0;
		}
		try {
			return str.getBytes("GBK").length;
		} catch (UnsupportedEncodingException e) {
			throw new NullPointerException("转换编码出错.");
		}
	}

	public static void setProperty(Object instance, Field field, final Object value) throws Exception {
		Class<?> cls = instance.getClass();
		String methodName = "set" + StringUtil.upCaseFirst(field.getName());
		Method set = cls.getMethod(methodName, field.getType());
		set.invoke(instance, value);
	}

	public static Object getProperty(Object instance, final String name) throws Exception {
		Class<?> cls = instance.getClass();
		String methodName = "get" + StringUtil.upCaseFirst(name);
		Method get = cls.getMethod(methodName);
		return get.invoke(instance);
	}

	public static String encode(String str) {
		if (StringUtil.isBlank(str)) {
			return "";
		}
		try {
			return URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error("编码[" + str + "]出错:" + e.getMessage());
			return str;
		}
	}

	public static String decode(String str) {
		if (StringUtil.isBlank(str)) {
			return "";
		}
		try {
			return URLDecoder.decode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error("解码[" + str + "]出错:" + e.getMessage());
			return str;
		}
	}
}
