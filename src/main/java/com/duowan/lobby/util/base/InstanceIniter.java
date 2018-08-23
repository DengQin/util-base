package com.duowan.lobby.util.base;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duowan.lobby.util.base.annotation.DateTime;
import com.duowan.lobby.util.base.annotation.IgnoreInit;
import com.duowan.lobby.util.base.annotation.NotEmpty;
import com.duowan.lobby.util.base.annotation.Number;
import com.duowan.lobby.util.base.annotation.ParamName;
import com.duowan.lobby.util.base.annotation.Range;
import com.duowan.lobby.util.base.exception.VisualException;

/**
 * 
 * 通过注解，验证参数，并返回实体<br>
 * 使用方法 AA aa = InstanceIniter.initInstance(request, AA.class);
 * 
 * @author tanjh 2014-8-8
 */
public class InstanceIniter {

	private static Logger log = LoggerFactory.getLogger(InstanceIniter.class);

	/**
	 * 根据实体类的配置，检查request的参数并返回对象实体,除去不需要配置的属性
	 * 
	 * @param excludeFields
	 *            不需要设置的属性
	 */
	@SuppressWarnings("unchecked")
	public static <T> T initInstanceExcludeFields(HttpServletRequest request, Class<T> cls, Set<String> excludeFields) {
		Object obj = null;
		try {
			obj = cls.newInstance();
		} catch (Exception e) {
			throw new PropertyCheckException("不能实例化" + cls.getName());
		}
		List<Field> list = getAllField(cls);
		for (Field field : list) {
			if (field != null) {
				String name = field.getName();
				if (excludeFields == null || !excludeFields.contains(name)) {
					if (field.isAnnotationPresent(ParamName.class)) {
						ParamName paramName = field.getAnnotation(ParamName.class);
						initProperty(cls, obj, field, request.getParameter(paramName.value()));
					} else {
						initProperty(cls, obj, field, request.getParameter(name));
					}
				}
			}
		}
		return (T) obj;
	}

	/**
	 * 获取所有的参数，包括祖先类的
	 */
	private static <T> List<Field> getAllField(Class<T> cls) {
		List<Field> list = new LinkedList<Field>();
		Class<?> t = cls;
		while (t != Object.class) {
			Field[] arr = t.getDeclaredFields();
			for (Field field : arr) {
				list.add(field);
			}
			t = t.getSuperclass();
		}
		return list;
	}

	/**
	 * 根据实体类的配置，检查request的参数并返回对象实体,只配置传进的属性
	 * 
	 * @param includeFields
	 *            需要设置的属性
	 */
	@SuppressWarnings("unchecked")
	public static <T> T initInstanceIncludeFields(HttpServletRequest request, Class<T> cls, Set<String> includeFields) {
		Object obj = null;
		try {
			obj = cls.newInstance();
		} catch (Exception e) {
			throw new PropertyCheckException("不能实例化" + cls.getName());
		}
		if (includeFields == null || includeFields.isEmpty()) {
			return (T) obj;
		}
		Field[] arr = cls.getDeclaredFields();
		if (arr != null) {
			for (Field field : arr) {
				if (field != null) {
					String name = field.getName();
					if (includeFields.contains(name)) {
						initProperty(cls, obj, field, request.getParameter(name));
					}
				}
			}
		}
		return (T) obj;
	}

	/**
	 * 根据实体类的配置，检查request的参数并返回对象实体<br>
	 * model的字段可以用com.duowan.lobby.util.base.annotation下面的注解验证。
	 * 
	 * @param request
	 * @param cls
	 * @return
	 */
	public static <T> T initInstance(HttpServletRequest request, Class<T> cls) {
		return initInstanceExcludeFields(request, cls, null);
	}

	/**
	 * 批量提交时，检查request的参数并返回对象实体的list
	 * 
	 * @param excludeFields
	 *            不需要设置的属性
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> initListExcludeFields(HttpServletRequest request, Class<T> cls,
			Set<String> excludeFields) {
		Field[] arr = cls.getDeclaredFields();
		int length = -1;
		Map<Field, Object> map = new HashMap<Field, Object>();
		if (arr != null) {
			for (Field field : arr) {
				if (field != null) {
					String name = field.getName();
					if (excludeFields == null || !excludeFields.contains(name)) {
						String[] valueArr = request.getParameterValues(name);
						if (valueArr == null) {
							throw new PropertyCheckException(name + "提交的个数不对,为空");
						}
						if (length == -1) {
							length = valueArr.length;
						} else {
							if (length != valueArr.length) {
								throw new PropertyCheckException(name + "提交的个数不对,为" + length);
							}
						}
						map.put(field, valueArr);
					}
				}
			}
		}
		List<T> result = new LinkedList<T>();
		for (int i = 0; i < length; i++) {
			Object obj = null;
			try {
				obj = cls.newInstance();
			} catch (Exception e) {
				throw new PropertyCheckException("不能实例化" + cls.getName());
			}
			for (Map.Entry<Field, Object> entry : map.entrySet()) {
				Field field = entry.getKey();
				String[] valueStr = (String[]) entry.getValue();
				try {
					initProperty(cls, obj, field, valueStr[i]);
				} catch (Exception e) {
					throw new BatchCheckException(i, e.getMessage());
				}
			}
			result.add((T) obj);
		}
		return result;
	}

	/**
	 * 批量提交时，检查request的参数并返回对象实体的list
	 * 
	 * @param excludeFields
	 *            需要设置的属性
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> initListincludeFields(HttpServletRequest request, Class<T> cls,
			Set<String> includeFields) {
		if (includeFields == null || includeFields.isEmpty()) {
			return new LinkedList<T>();
		}
		Field[] arr = cls.getDeclaredFields();
		int length = -1;
		Map<Field, Object> map = new HashMap<Field, Object>();
		if (arr != null) {
			for (Field field : arr) {
				if (field != null) {
					String name = field.getName();
					if (includeFields.contains(name)) {
						String[] valueArr = request.getParameterValues(name);
						if (valueArr == null) {
							throw new PropertyCheckException(name + "提交的个数不对,为空");
						}
						if (length == -1) {
							length = valueArr.length;
						} else {
							if (length != valueArr.length) {
								throw new PropertyCheckException(name + "提交的个数不对,为" + length);
							}
						}
						map.put(field, valueArr);
					}
				}
			}
		}
		List<T> result = new LinkedList<T>();
		for (int i = 0; i < length; i++) {
			Object obj = null;
			try {
				obj = cls.newInstance();
			} catch (Exception e) {
				throw new PropertyCheckException("不能实例化" + cls.getName());
			}
			for (Map.Entry<Field, Object> entry : map.entrySet()) {
				Field field = entry.getKey();
				String[] valueStr = (String[]) entry.getValue();
				try {
					initProperty(cls, obj, field, valueStr[i]);
				} catch (Exception e) {
					throw new BatchCheckException(i, e.getMessage());
				}
			}
			result.add((T) obj);
		}
		return result;
	}

	/**
	 * 批量提交时，检查request的参数并返回对象实体的list
	 */
	public static <T> List<T> initList(HttpServletRequest request, Class<T> cls) {
		return initListExcludeFields(request, cls, null);
	}

	/**
	 * 逐个参数检查和初始化
	 */
	private static void initProperty(Class<?> cls, Object Obj, Field field, String valueStr) {
		if (field.isAnnotationPresent(IgnoreInit.class)) {
			return;
		}
		Object value = null;
		if (field.isAnnotationPresent(NotEmpty.class)) {
			NotEmpty notEmpty = field.getAnnotation(NotEmpty.class);
			if (StringUtil.isBlank(valueStr)) {
				throw new PropertyCheckException(notEmpty.name() + "不能为空");
			}
			value = convert(valueStr, field, null);
		} else if (field.isAnnotationPresent(Range.class)) {
			value = checkLength(field, valueStr);
		} else if (field.isAnnotationPresent(Number.class)) {
			value = checkNumber(field, valueStr);
		} else if (field.isAnnotationPresent(DateTime.class)) {
			value = checkDateTime(field, valueStr);
		} else {
			if (valueStr == null) {
				return;
			}
			if (StringUtil.isBlank(valueStr)) {
				if (!field.getType().equals(String.class)) {
					return;
				}
			}
			value = convert(valueStr, field, null);
		}
		if (value != null) {
			XssUtil.checkXSS(value.toString());
		}
		try {
			setProperty(cls, Obj, field, value);
		} catch (Exception e) {
			e.printStackTrace();
			throw new PropertyCheckException("生成对象出错,设置" + cls.getName() + "的" + field.getName() + "出错");
		}
	}

	private static Object checkDateTime(Field field, String valueStr) {
		DateTime dateTime = field.getAnnotation(DateTime.class);
		final String name = dateTime.name();
		final boolean canBeNull = dateTime.canBeNull();
		final String format = dateTime.format();
		if (StringUtil.isBlank(valueStr)) {
			if (canBeNull == true) {
				return null;
			} else {
				throw new PropertyCheckException(name + "不能为空");
			}
		}
		return convert(valueStr, field, format);
	}

	private static Object checkNumber(Field field, String valueStr) {
		Number number = field.getAnnotation(Number.class);
		final String name = number.name();
		final double lower = number.lower();
		final double upper = number.upper();
		final boolean lowerEqual = number.lowerEqual();
		final boolean upperEqual = number.upperEqual();
		if (StringUtil.isBlank(valueStr)) {
			throw new PropertyCheckException(name + "不能为空");
		}
		BigDecimal value = null;
		try {
			value = new BigDecimal(valueStr);
		} catch (Exception e) {
			throw new PropertyCheckException(name + "不是有效数字");
		}
		if (lower != Double.MIN_VALUE) {
			BigDecimal checkLower = new BigDecimal(Double.toString(lower));
			if (lowerEqual) {
				if (value.compareTo(checkLower) == -1) {
					throw new PropertyCheckException(name + "不能小于" + lower);
				}
			} else {
				if (value.compareTo(checkLower) != 1) {
					throw new PropertyCheckException(name + "要大于" + lower);
				}
			}
		}
		if (upper != Double.MAX_VALUE) {
			BigDecimal checkUpper = new BigDecimal(Double.toString(upper));
			if (upperEqual) {
				if (value.compareTo(checkUpper) == 1) {
					throw new PropertyCheckException(name + "不能大于" + upper);
				}
			} else {
				if (value.compareTo(checkUpper) != -1) {
					throw new PropertyCheckException(name + "要小于" + upper);
				}
			}
		}
		return convert(valueStr, field, null);
	}

	private static Object checkLength(Field field, String value) {
		Range range = field.getAnnotation(Range.class);
		final String name = range.name();
		final int length = range.length();
		final int lower = range.lower();
		final int upper = range.upper();
		if (StringUtil.isBlank(value)) {
			throw new PropertyCheckException(name + "不能为空");
		}
		if (length > 0) {
			if (value.length() != length) {
				throw new PropertyCheckException(name + "位数不等于" + length);
			}
		} else {
			if (lower > 0 && value.length() < lower) {
				throw new PropertyCheckException(name + "位数不能小于" + lower);
			}
			if (upper > 0 && value.length() > upper) {
				throw new PropertyCheckException(name + "位数不能大于" + upper);
			}
		}
		return convert(value, field, null);
	}

	private static Object convert(String s, Field field, String dateFormat) {
		try {
			return StringUtil.convert(s.trim(), field.getType(), dateFormat);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PropertyCheckException("转换" + s + "为对象的" + field.getName() + "属性类型出错");
		}
	}

	private static void setProperty(Class<?> cls, Object instance, Field field, final Object value) throws Exception {
		Util.setProperty(instance, field, value);
	}
}

class PropertyCheckException extends VisualException {
	private static final long serialVersionUID = 522701633200682902L;

	public PropertyCheckException() {
		super();
	}

	public PropertyCheckException(String message) {
		super(message);
	}
}

class BatchCheckException extends VisualException {
	private int rowNum;

	private String message;

	private static final long serialVersionUID = 146458310084088150L;

	public BatchCheckException() {
		super();
	}

	public BatchCheckException(int rowNum, String message) {
		super(message);
		this.rowNum = rowNum;
		this.message = message;
	}

	public int getRowNum() {
		return rowNum;
	}

	@Override
	public String toString() {
		return "第" + (rowNum + 1) + "行," + message;
	}

}
