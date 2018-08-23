package com.duowan.lobby.util.base;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionUtil {
	private static Logger log = LoggerFactory.getLogger(ExceptionUtil.class);

	public static String showExceptionMsg(Exception e, String packageName, int showLineNum) {
		StackTraceElement[] arr = e.getStackTrace();
		if (arr == null) {
			log.error(e.getMessage());
			return "getStackTrace为空";
		}
		List<String> list = new ArrayList<String>();
		for (StackTraceElement stackTraceElement : arr) {
			if (stackTraceElement == null) {
				continue;
			}
			if (list.size() >= showLineNum) {
				break;
			}
			String s = StringUtil.trim(stackTraceElement.toString());
			if (s.contains(packageName)) {
				list.add(s);
			}
		}
		String stackMsg = "";
		for (String s : list) {
			stackMsg += "\n\t" + s;
		}
		return e.getMessage() + stackMsg;
	}

	public static String showExceptionMsg(Exception e, String packageName) {
		return showExceptionMsg(e, packageName, 3);
	}
}
