package com.jt.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)	//该注解对方法有效
@Retention(RetentionPolicy.RUNTIME)	//运行期有效
public @interface CacheFind {

	//要求用户指定key的前缀,之后动态拼接key 
	public String perKey();
	//如果用户有超时时间则定义超时用法
	public int seconds() default 0;
}










