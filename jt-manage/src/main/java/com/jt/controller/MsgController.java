package com.jt.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MsgController {
	
	//问题:如何获取当前服务器端口号???
	@Value("${server.port}")
	private int port;
	
	@RequestMapping("/getMsg")
	public String getMsg() {
		
		return "当前访问的端口号:"+port;
	}
}
