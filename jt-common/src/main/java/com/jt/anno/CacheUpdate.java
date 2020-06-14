package com.jt.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)	//该注解对方法有效
@Retention(RetentionPolicy.RUNTIME)	//运行期有效
public @interface CacheUpdate {
	
	public String perKey();
	public int seconds() default 0;
}
