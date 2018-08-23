package com.duowan.lobby.util.base;

import java.util.HashMap;
import java.util.Map;

/** 前端通用返回结果 */
public class JsonResult {

	public static String outputJson(int status, String message, Object data) {
		Map<String, Object> map = new HashMap<String, Object>(4, 1);
		map.put("status", status);
		map.put("message", message);
		map.put("data", data);
		return JsonUtil.toJson(map);
	}

	public static String success() {
		return outputJson(200, "", null);
	}

	public static String success(Object obj) {
		return outputJson(200, "", obj);
	}

	public static String fail(String message) {
		return outputJson(-1, message, null);
	}

	public static String fail(int status, String message) {
		return outputJson(status, message, null);
	}

}
