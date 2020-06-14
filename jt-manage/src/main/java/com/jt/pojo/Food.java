package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

//该pojo是测试API 不是业务逻辑
@Data
public class Food {
	private Long code;
	private String name;
	private Long price;	
}
