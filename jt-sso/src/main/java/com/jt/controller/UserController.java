package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.service.UserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JedisCluster jedisCluster;
	/**
	 * 业务需求:  根据用户的参数和类型查询是否有该数据.
	 * url地址:   http://sso.jt.com/user/check/{param}/{type}
	 * 参数:      param 需要校验的参数
	 * 		      type  校验的类型     1 username、2 phone、3 email
	 * 返回值:    SysResult对象
	 * 
	 * 跨域访问:  跨域的API和方法实现   ?callback      callback(json)
	 */
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject checkUser(@PathVariable String param,
								 @PathVariable Integer type,
								 String callback) {
		
		//1.定义标识变量.  数据已存在返回true  数据不存在  false
		boolean flag = userService.checkUser(param,type);
		SysResult sysResult = SysResult.success(flag);
		return  new JSONPObject(callback, sysResult);    
	}
	
	
	/**
	 * url:http://sso.jt.com/user/query/a867deb7d214424586330f3ea574f0f1?callback=jsonp1591670443525&_=1591670443568
	 * 参数:ticket信息
	 * 返回值信息:SysResult对象,并且携带userJSON
	 * 注意事项:jsonp请求,注意jsonp格式封装
	 */
	@RequestMapping("/query/{ticket}")
	public JSONPObject findUserByTicket(@PathVariable String ticket,String callback) {
		//如何通过ticket获取userjson数据
		String userJSON = jedisCluster.get(ticket);
		if(StringUtils.isEmpty(userJSON)) {
			
			return new JSONPObject(callback, SysResult.fail());
		}
		
		return new JSONPObject(callback, SysResult.success(userJSON));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * @RequestMapping("/getMsg") public String getMsg() {
	 * 
	 * return "测试sso成功!!!!!"; }
	 */
	
}
