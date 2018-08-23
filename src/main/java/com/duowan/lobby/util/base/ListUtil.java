package com.duowan.lobby.util.base;

import java.util.Collection;
import java.util.List;

public class ListUtil {
	public static boolean isEmpty(Collection<?> c) {
		if (c == null || c.isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean isNotEmpty(Collection<?> c) {
		if (c == null || c.isEmpty()) {
			return false;
		}
		return true;
	}

	public static boolean isInList(Object o, List<?> c) {
		if (o == null || c == null || c.isEmpty()) {
			return false;
		}
		for (Object t : c) {
			if (o.equals(t)) {
				return true;
			}
		}
		return false;
	}

	public static boolean strIgnoreCaseInList(String o, List<String> c) {
		if (o == null || c == null || c.isEmpty()) {
			return false;
		}
		for (String s : c) {
			if (o.equalsIgnoreCase(s)) {
				return true;
			}
		}
		return false;
	}

}
