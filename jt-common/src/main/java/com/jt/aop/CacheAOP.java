package com.jt.aop;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.catalina.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ScanCursor;
import org.springframework.stereotype.Component;

import com.jt.anno.CacheFind;
import com.jt.anno.CacheUpdate;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

@Aspect
@Component
public class CacheAOP {
	
	@Autowired(required = false)
	private JedisCluster jedis;
	//private ShardedJedis jedis;	//引入分片机制   3台redis
	//private Jedis jedis;  //单台redis		
	
	/**
	 * 规定:ProceedingJoinPoint 参数必须位于第一位
	 * 1.利用切入点表达式拦截@CacheFind注解,同时获取CacheFind注解对象
	 * 2.自定义key
	 * 3.根据key查询缓存.
	 * 		有数据:  则返回对象
	 * 		没有数据:  则查询数据库.之后将返回结果 保存到redis中
	 * 
	 * 
	 * 反射相关:
	 * 
	 * 	Method method = 
				joinPoint.getTarget()	//获取目标对象
						 .getClass()	//目标对象的class
						 .getMethod("findItemCatList", Long.class); //方法
				//从方法对象中获取返回值类型
				Class returnClass = method.getReturnType();
	 * 
	 *   
	 */
	@SuppressWarnings("unchecked")
	//@Around("@annotation(com.jt.anno.CacheFind)")
	@Around("@annotation(cacheFind)")
	public Object  around(ProceedingJoinPoint joinPoint,CacheFind cacheFind) {
		
		Object result = null;
		try {
			Object[] args = joinPoint.getArgs();
			//动态拼接key
			String key = cacheFind.perKey()+"::"+Arrays.toString(args);
			//根据key查询redis
			if(!jedis.exists(key)) {
				//如果key不存在,则执行目标方法
				result = joinPoint.proceed();
				System.out.println("AOP查询数据库!!!!!!");
				//将返回值结果保存到redis中
				String json = ObjectMapperUtil.toJSON(result);
				int seconds = cacheFind.seconds();
				if(seconds>0)
					//表示用户需要设定超时时间
					jedis.setex(key, seconds, json);
				else 
					//用户不需要超时时间
					jedis.set(key, json);
			}else {
				String json = jedis.get(key);
				//如何才能动态的获取方法的返回值类型?????  必须获取方法对象
				MethodSignature methodSignature = 
						(MethodSignature) joinPoint.getSignature();	//获取目标对象的方法
				//将json转化为对象
				result = 
						ObjectMapperUtil.toObj(json,methodSignature.getReturnType());
				System.out.println("AOP缓存查询!!!!!!!");
			}
			
			return result;
		} catch (Throwable e) {
			
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 缓存更新AOP实现    首选环绕通知
	 * 要求:
	 * 	1.执行目标方法
	 * 	2.将返回值更新redis缓存
	 */
	@Around("@annotation(cacheUpdate)")
	public Object updateAround(ProceedingJoinPoint joinPoint,CacheUpdate cacheUpdate) {
		try {
			//暂时写死
			ItemCat itemCat = (ItemCat) joinPoint.proceed();
			String key = cacheUpdate.perKey()+"::["+itemCat.getId()+"]";
			String json = ObjectMapperUtil.toJSON(itemCat);
			if(cacheUpdate.seconds()>0) {
				jedis.setex(key,cacheUpdate.seconds(),json);
			}else {
				jedis.set(key, json);
			}
			
			return itemCat;
		} catch (Throwable e) {
			
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
