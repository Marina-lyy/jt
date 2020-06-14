package com.jt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class CorsMVCConfig implements WebMvcConfigurer{

	
	//重写关于服务器跨域访问的策略
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		 registry.addMapping("/**")	//标识所有的请求可以跨域
         		 .allowedOrigins("*") //允许哪个网址访问  
         		 .allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS","HEAD")   //请求类型
         		 .allowCredentials(true) //是否允许携带cookie
         		 //1.如果需要进行跨域访问,首先试探性的发起请求.(询问)
         		 //如果服务器允许跨域则在一定的时间之内无需再次试探  默认30分钟
         		 .maxAge(3600); //校验请求的有效期
	}
}
