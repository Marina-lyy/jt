package com.jt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {
	
	
	/**
	 * url:/page/item-add
	 * url:/page/item-list
	 * url:/page/item-param-list
	 * 
	 * 思路:动态获取url中的参数,之后实现页面的跳转
	 * 
	 * restFul风格1:
	 * 		语法:
	 * 			1.参数必须使用/方式进行分割
	 * 			2.参数必须使用{}包裹, 名称任意 见名知意即可
	 * 			3.在方法中添加同名参数 + 注解 @PathVariable
	 * 		
	 * 		注解说明:
	 * 			  value/name
	 * 			   1.如果参数命名和方法中的参数名称不一致时,则使用value或者name进行转化
	 * 			  required() default true 该值为必传数据.
	 *		总结: 
	 *			1.可以动态的获取url中的参数
	 *			2.可以根据不同的请求类型,实现不同的业务操作.
	 *
	 *	restFul风格2:
	 *		用法:根据不同的请求类型,实现不同的业务功能
	 *		需求:  /user/addUser    			新增用户
	 *			  /user/deleteUser?id=xx    删除用户
	 *			  /user/updateUser?xxx   	更新用户
	 *			  /user/getUser?id=1       	查询用户
	 *		页面中url地址使用 
	 *		      url: /user 可以自动的实现CRUD操作.
	 *			  
	 *			  修改页面:href:'/page/item-edit'"
	 * */
	@RequestMapping("/page/{moduleName}")
	public String toModule(@PathVariable(value = "moduleName") String moduleName) {
		
		return moduleName;
	}
	
	/**
	 * 规定:根据不同的请求类型,控制方法的执行  get/post/put/delete
	 * 		新增操作: post
	 * 		查询操作: get
	 * 		更新操作: put
	 * 		删除操作: delete    
	 * @param user
	 * @return
	 */
	/*
	 * //1.新增为例 //@RequestMapping(value = "/user",method = RequestMethod.POST)
	 * 
	 * @PostMapping("/user") public XXXXX saveUser(User user) {
	 * 
	 * }
	 * 
	 * //2.查询用户
	 * 
	 * @GetMapping("/user")
	 * 
	 * @PutMapping("/user")
	 * 
	 * @DeleteMapping("/user") public XXXXX getUser(Integer id) {
	 * 
	 * return xxx; }
	 */
	
}
