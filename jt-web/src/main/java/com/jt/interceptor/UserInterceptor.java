package com.jt.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

//1.配置web.xml文件 2.编辑拦截器
@Component
public class UserInterceptor implements HandlerInterceptor{

	@Autowired
	private JedisCluster jedisCluster;
	/**
	 * 业务分析:用户不登录,应该重定向到登录页面
	 * 步骤:
	 * 		1.获取cookie信息,校验cookie数据是否有效
	 * 		2.根据ticket信息,查询redis中是否有记录
	 * 		3.如果有记录,则说明用户已经登陆,可以放行
	 * 		  如果查询为null,则说明用户没有登录,应该拦截请求
	 * 		 重定向到用户的登录页面
	 * 
	 *  返回值:true表示放行,访问目标页面
	 *  	   false表示拦截,一般都会重定向
	 *  
	 */
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String ticket = null;
		Cookie[] cookies = request.getCookies();
		if(cookies != null && cookies.length>0) {
			for (Cookie cookie : cookies) {
				if("JT_TICKET".equals(cookie.getName())) {
					ticket = cookie.getValue();
					break;
				}
				
			}
		}
		
		if(!StringUtils.isEmpty(ticket)) {
			//2.判断redis中是否有记录
			if(jedisCluster.exists(ticket)) {
				//如果存在,说明用户登录过
				//动态获取user信息,之后保存到request对象中即可
				String userJSON = jedisCluster.get(ticket);
				User user = ObjectMapperUtil.toObj(userJSON, User.class);
				request.setAttribute("JT_USER", user);
				//System.out.println("添加用户信息");
				return true;
			}
		}
		//要求跳转到用户登录页面
		response.sendRedirect("/user/login.html");
		
		return false;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//移除用户信息,节省存储空间
		request.removeAttribute("JT_USER");
		//System.out.println("移除用户信息");
	}
}
