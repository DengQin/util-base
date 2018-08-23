package com.duowan.lobby.util.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于传入参数名跟类的字段名不同的情况：举例： <br>
 * request.getParameter("game_id"); <br>
 * 类的参数可以这样写<br>
 * @ParamName("game_id");<br>
 * private String gameId;<br>
 * 
 * @author tanjh
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParamName {

	String value() default "";

}
