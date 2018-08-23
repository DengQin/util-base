package com.duowan.lobby.util.base;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanUtil {
	private static Logger log = LoggerFactory.getLogger(BeanUtil.class);

	/**
	 * 获取类的属性,包含继承的属性
	 */
	public static List<Field> getFields(Class<?> cls) {
		List<Field> list = new LinkedList<Field>();
		Set<String> set = new HashSet<String>();
		Class<?> tempCls = cls;
		while (tempCls != null) {
			Field[] fields = tempCls.getDeclaredFields();
			for (Field field : fields) {
				if (!cls.equals(tempCls) && judgeField(field) == null) {
					continue;
				}
				String name = field.getName();
				if (!set.contains(name)) {
					list.add(field);
					set.add(name);
				}
			}
			tempCls = tempCls.getSuperclass();
		}
		return list;
	}

	public static void setProperty(Object instance, Field field, Object value) throws Exception {
		if (value != null && field.getType().equals(Date.class) && value instanceof Long) {
			value = new Date((Long) value);
		}
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

	public static Map<String, Object> describe(Object obj) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Field> l = getFields(obj.getClass());
		for (Field field : l) {
			String name = field.getName();
			Object o = getProperty(obj, name);
			map.put(name, o);
		}
		return map;
	}

	public static void populate(Object obj, Map<String, Object> map) throws Exception {
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			Field field = getFieldWithExtend(obj.getClass(), key);
			if (field == null) {
				continue;
			}
			try {
				setProperty(obj, field, value);
			} catch (Exception e) {
				if(value==null){
					log.error("设置key[{}],value[{}]出错", new Object[] { key, value });
				}else{
					log.error("设置key[{}],value[{}],value类型[{}]出错", new Object[] { key, value,value.getClass() });
				}
				throw e;
			}
		}
	}

	/**
	 * 获取属性，包括继承的
	 */
	public static Field getFieldWithExtend(Class<?> cls, String name) {
		Class<?> tempCls = cls;
		while (tempCls != null) {
			Field field = getDeclaredField(tempCls, name);
			if (field == null) {
				tempCls = tempCls.getSuperclass();
			} else {
				if (tempCls.equals(cls)) {
					return field;
				} else if (judgeField(field) != null) {
					return field;
				} else {
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * 获取所有属性，不包括继承的
	 */
	public static Field getDeclaredField(Class<?> cls, String name) {
		try {
			return cls.getDeclaredField(name);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取public属性，包括继承的
	 */
	public static Field getField(Class<?> cls, String name) {
		try {
			return cls.getField(name);
		} catch (Exception e) {
			return null;
		}
	}

	private static Field judgeField(Field field) {
		if (field == null) {
			return null;
		}
		int modifiers = field.getModifiers();
		if (Modifier.isPrivate(modifiers)) {
			return null;
		}
		if (Modifier.isFinal(modifiers)) {
			return null;
		}
		return field;
	}
}
