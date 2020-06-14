package com.jt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;

@Configuration	//标识配置类
public class MybatisPlusConfig {
	
	//将整合对象 ,交给spring容器管理
	@Bean
	public PaginationInterceptor  paginationInterceptor() {
		
		return new PaginationInterceptor();
	}
}
