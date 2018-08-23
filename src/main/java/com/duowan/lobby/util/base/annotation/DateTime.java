package com.duowan.lobby.util.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DateTime {

	/**
	 * 名称
	 */
	String name();

	/**
	 * 允许为空
	 */
	boolean canBeNull() default true;

	/**
	 * 格式,默认yyyy-MM-dd HH:mm:ss
	 */
	String format() default "yyyy-MM-dd HH:mm:ss";

}