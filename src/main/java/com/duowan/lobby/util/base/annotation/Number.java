package com.duowan.lobby.util.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Number {
	/**
	 * 下限
	 */
	double lower() default Double.MIN_VALUE;

	/**
	 * 下限取等于
	 */
	boolean lowerEqual() default true;

	/**
	 * 上限
	 */
	double upper() default Double.MAX_VALUE;

	/**
	 * 上限取等于
	 */
	boolean upperEqual() default true;

	/**
	 * 名称
	 */
	String name();
}