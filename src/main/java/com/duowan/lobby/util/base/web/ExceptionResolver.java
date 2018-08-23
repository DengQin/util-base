package com.duowan.lobby.util.base.web;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.duowan.lobby.util.base.StringUtil;
import com.duowan.lobby.util.base.annotation.ExceptionForward;
import com.duowan.lobby.util.base.vo.ExceptionResolverResultVo;

public class ExceptionResolver {

	private static final Logger log = LoggerFactory.getLogger(ExceptionResolver.class);

	public static ExceptionResolverResultVo resolve(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex) {
		try {
			Method[] methods = handler.getClass().getDeclaredMethods();
			String uri = request.getRequestURI();
			for (Method method : methods) {
				ExceptionForward exceptionForward = method.getAnnotation(ExceptionForward.class);
				if (exceptionForward == null) {
					continue;
				}
				RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
				if (requestMapping == null) {
					continue;
				}
				String[] values = requestMapping.value();
				for (String string : values) {
					if (!uri.endsWith(string)) {
						continue;
					}
					String forwardUrl = getForwardUrl(exceptionForward, ex, request);
					if (StringUtil.isBlank(forwardUrl)) {
						break;
					}
					if (forwardUrl.indexOf(".do") > -1) {
						try {
							request.getRequestDispatcher(forwardUrl).forward(request, response);
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}
						return new ExceptionResolverResultVo(true, null);
					}
					return new ExceptionResolverResultVo(true, new ModelAndView(forwardUrl));
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new ExceptionResolverResultVo(false, null);
	}

	private static String getForwardUrl(ExceptionForward exceptionForward, Exception ex, HttpServletRequest request) {
		Class<? extends Exception>[] clzs = exceptionForward.classes();
		String[] urls = exceptionForward.urls();
		for (int i = 0; i < clzs.length; i++) {
			Class<? extends Exception> clz = clzs[i];
			if (clz.isAssignableFrom(ex.getClass()) && urls.length > i) {
				return transUrl(urls[i], '{', '}', request);
			}
		}
		return null;
	}

	private static String transUrl(String url, char c1, char c2, HttpServletRequest request) {
		char[] arr = url.toCharArray();
		boolean found = false;
		StringBuilder result = new StringBuilder();
		StringBuilder str = new StringBuilder();
		for (char c : arr) {
			if (c == c1 && !found) {// 找到开头
				found = true;
				continue;
			}
			if (found) {
				if (c == c2) {// 找到结尾
					result.append(request.getParameter(str.toString().trim()));
					found = false;
				} else {
					str.append(c);
				}
			} else {
				result.append(c);
			}
		}
		return result.toString();
	}
}
