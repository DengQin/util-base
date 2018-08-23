package com.duowan.lobby.util.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duowan.lobby.util.base.exception.CommonException;

public class JsonUtil {
	private static Logger log = LoggerFactory.getLogger(JsonUtil.class);

	private static ObjectMapper mapper = new ObjectMapper(); // can reuse, share

	private static ObjectMapper ignoreQuoteMapper = new ObjectMapper();

	static {
		ignoreQuoteMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
	}

	public static String toJson(Object obj) {
		if (obj == null) {
			return null;
		}
		try {
			String str = mapper.writeValueAsString(obj);
			return str;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new CommonException(e.getMessage());
		}
	}

	public static <T> T toObject(String content, Class<T> valueType) {
		if (StringUtil.isBlank(content)) {
			return null;
		}
		try {
			return mapper.readValue(content, valueType);
		} catch (Exception e) {
			log.error("content<" + content + ">转换出错", e);
			throw new CommonException(e.getMessage());
		}
	}

	/**
	 * json转List
	 * 
	 * @param content
	 *            json数据
	 * @param valueType
	 *            泛型数据类型
	 * @return
	 */
	public static <T> List<T> toListObject(String content, Class<T> valueType) {
		try {
			return mapper.readValue(content, mapper.getTypeFactory().constructParametricType(List.class, valueType));
		} catch (Exception e) {
			log.error("content<" + content + ">转换出错", e);
			throw new CommonException(e.getMessage());
		}
	}

	public static Map<?, ?> toMap(String content) {
		if (StringUtil.isBlank(content)) {
			return null;
		}
		try {
			Map<?, ?> map = mapper.readValue(content, Map.class);
			return map;
		} catch (Exception e) {
			log.error("content<" + content + ">转换出错", e);
			throw new CommonException(e.getMessage());
		}
	}

	/**
	 * 允许使用单引号的非标准json
	 * 
	 * @param content
	 * @return
	 */
	public static Map<?, ?> toMapIgnoreQuote(String content) {
		if (StringUtil.isBlank(content)) {
			return null;
		}
		try {
			Map<?, ?> map = ignoreQuoteMapper.readValue(content, Map.class);
			return map;
		} catch (Exception e) {
			log.error("content<" + content + ">转换出错", e);
			throw new CommonException(e.getMessage());
		}
	}

	private static final String SPLIT = ".class,";

	/**
	 * 序列化一个对象
	 */
	public static String serialize(Object object) {
		if (object == null) {
			return "";
		}
		try {
			String className = object.getClass().getName();
			String json = JsonUtil.toJson(object);
			String str = className + SPLIT + json;
			return str;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 发序列化成一个对象
	 */
	public static Object unserialize(String content) {
		if (StringUtil.isBlank(content)) {
			return null;
		}
		try {
			int index = content.indexOf(SPLIT);
			String className = content.substring(0, index);
			String json = content.substring(index + SPLIT.length());
			Class<?> clazz = getClass(className);
			return JsonUtil.toObject(json, clazz);
		} catch (Exception e) {
			log.error("content<" + content + ">转换出错", e);
			throw new CommonException(e.getMessage());
		}
	}

	/**
	 * 序列化一个map对象
	 */
	public static String serializeMap(Map<String, Object> map) {
		if (MapUtil.isEmpty(map)) {
			return "";
		}
		try {
			Map<String, String> newMap = new HashMap<String, String>();
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				newMap.put(entry.getKey(), serialize(entry.getValue()));
			}
			return toJson(newMap);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new CommonException(e.getMessage());
		}
	}

	/**
	 * 反序列化一个map对象
	 */
	public static Map<String, Object> unserializeMap(String content) {
		if (StringUtil.isBlank(content)) {
			return new HashMap<String, Object>(1);
		}
		try {
			Map<?, ?> newMap = toMap(content);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			for (Map.Entry<?, ?> entry : newMap.entrySet()) {
				resultMap.put(entry.getKey().toString(), unserialize(entry.getValue().toString()));
			}
			return resultMap;
		} catch (Exception e) {
			log.error("content<" + content + ">转换出错", e);
			throw new CommonException(e.getMessage());
		}
	}

	private static Map<String, Class<?>> clazzCache = new HashMap<String, Class<?>>();

	private static Class<?> getClass(String className) {
		Class<?> clazz = clazzCache.get(className);
		if (clazz != null) {
			return clazz;
		}
		try {
			clazz = Class.forName(className);
			clazzCache.put(className, clazz);
			return clazz;
		} catch (ClassNotFoundException e) {
			log.error("找不到类<" + className + ">");
			throw new CommonException(e.getMessage());
		}
	}

}
