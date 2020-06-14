package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SysResult {
	private Integer status;		//业务是否操作成功的标识符目的前台页面交互 200成功  201失败
	private String msg;			//提示信息
	private Object data;		//服务器返回页面数据信息
	
	/*
	 * 为什么写静态方法   简化用户调用
	 * Class.方法名称
	 * 对象.方法名称
	 * */
	
	
	//编辑公共的API,简化用户的调用
	public static SysResult success() {
		
		return new SysResult(200, "操作成功", null);
	}
	
	public static SysResult success(Object data) {
		
		return new SysResult(200, "操作成功", data);
	}
	
	public static SysResult success(String msg,Object data) {
		
		return new SysResult(200, msg, data);
	}
	
	public static SysResult fail() {
		
		return new SysResult(201, "业务调用失败", null);
	}
}
