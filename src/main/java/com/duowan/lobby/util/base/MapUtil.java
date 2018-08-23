package com.duowan.lobby.util.base;

import java.util.Map;

public class MapUtil {
	public static boolean isEmpty(Map<?, ?> map) {
		if (map == null || map.isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean isNotEmpty(Map<?, ?> map) {
		if (map == null || map.isEmpty()) {
			return false;
		}
		return true;
	}
}
