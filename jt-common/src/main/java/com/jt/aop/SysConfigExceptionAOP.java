package com.jt.aop;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jt.vo.SysResult;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice	//返回值结果都是json字符串
@Slf4j
public class SysConfigExceptionAOP {
	
	/**
	 * 1.拦截什么样的异常   运行时异常
	 * 2.返回值结果是什么   系统返回值VO对象
	 */
	@ExceptionHandler(RuntimeException.class)
	public Object sysResult(Exception exception) {
		
		log.error(exception.getMessage());
		exception.printStackTrace();
		return SysResult.fail();
	}
}
