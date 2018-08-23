package com.duowan.lobby.util.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExceptionForward {
	/**
	 * 捕获异常
	 */
	Class<? extends Exception>[] classes() default { Exception.class };

	/**
	 * 跳转地址
	 */
	String[] urls() default {};

}
