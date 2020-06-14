package com.jt.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

//@Component		//将AOP 交给spring容器管理
//@Aspect			//标识该类为AOP切面
public class TestAOP {

	//AOP   切面 = 切入点表达式 + 通知方法
	//1.bean  2.within   3.execution(返回值类型  包名.类名.方法名(参数列表))
	//@Pointcut("bean(itemCatServiceImpl)")
	//@Pointcut("within(com.jt.service.ItemCatServiceImpl)")
	@Pointcut("execution(* com.jt..*.*(..))")
	public void pointCut() {
		
	}
	
	//1.定义前置通知   获取参数数据
	@Before("pointCut()")
	public void before(JoinPoint joinPoint) {
		String className = 
				joinPoint.getSignature().getDeclaringTypeName();
		String methodName = joinPoint.getSignature().getName();
		Object[] objs = joinPoint.getArgs();	//获取目标方法的参数
		System.out.println(className);
		System.out.println(methodName);
		System.out.println(objs);
	}
	
}
