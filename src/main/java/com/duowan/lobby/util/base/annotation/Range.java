package com.duowan.lobby.util.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 规定字符串长度大小、范围
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Range {

	/**
	 * 下限
	 */
	int lower() default -1;

	/**
	 * 上限
	 */
	int upper() default -1;

	/**
	 * 长度
	 */
	int length() default -1;

	/**
	 * 名称
	 */
	String name();
}