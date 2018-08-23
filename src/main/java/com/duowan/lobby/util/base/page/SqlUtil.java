package com.duowan.lobby.util.base.page;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.duowan.lobby.util.base.StringUtil;

public class SqlUtil {

	public static String parseToColName(String methodName) {
		if (StringUtil.isBlank(methodName)) {
			return "";
		}
		char[] arr = methodName.toCharArray();
		boolean found = false;
		StringBuilder sb = new StringBuilder();
		for (char c : arr) {
			if ('_' == c) {
				found = true;
				continue;
			}
			if (found) {
				sb.append(Character.toUpperCase(c));
				found = false;
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 去除orderby 子句
	 * 
	 * @param sql
	 * @return
	 */
	public static String removeOrders(String sql) {
		Pattern p = Pattern.compile("\\s*order\\s*by\\s*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql);
		if (m.find()) {
			return sql.substring(0, m.start());
		}
		return null;
	}

}
