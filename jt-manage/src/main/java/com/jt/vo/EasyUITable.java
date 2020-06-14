package com.jt.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor	//无参构造
@AllArgsConstructor	//全参构造
public class EasyUITable {
	
	private Integer total;		//定义总记录数
	private List rows;      //定义集合信息  List集合中写的就是用户展现记录
	
	//VO对象在进行数据转化时,必须调用对象的get/set方法 必须是public修饰
}
