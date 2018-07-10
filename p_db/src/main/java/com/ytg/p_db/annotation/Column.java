package com.ytg.p_db.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.FIELD })
public @interface Column {

	public abstract String name() default "";

	public abstract String type() default "";

	public abstract int length() default 0;
}