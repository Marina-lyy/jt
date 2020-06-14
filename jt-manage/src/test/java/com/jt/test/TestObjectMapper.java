package com.jt.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;

public class TestObjectMapper {
	
	@Test
	public void test01() throws JsonProcessingException {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(100L)
				.setItemDesc("测试数据")
				.setCreated(new Date())
				.setUpdated(itemDesc.getCreated());
		
		//1.对象转化为JSON 
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(itemDesc);
		System.out.println(json);
		
		//2.JSON转化为对象
		ItemDesc itemDesc2 = objectMapper.readValue(json, ItemDesc.class);
		System.out.println(itemDesc2.toString()+":"+itemDesc2.getCreated());
	}
	
	//该类被调用1次   则对象被创建1次
	//static 属性属于类的  无论调用多少次  对象1个   小明 类.OBJECTmAPPER=修改的对象
	//static final 为了安全  不允许别人修改
	private static final ObjectMapper OBJECTMAPPER = new ObjectMapper();
	
	//LIST集合转化为JSON
	@Test
	public void test02() throws JsonProcessingException {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(100L)
				.setItemDesc("测试数据")
				.setCreated(new Date())
				.setUpdated(itemDesc.getCreated());
		
		ItemDesc itemDesc2 = new ItemDesc();
		itemDesc2.setItemId(100L)
				.setItemDesc("测试数据")
				.setCreated(new Date())
				.setUpdated(itemDesc.getCreated());
		
		List<ItemDesc> list = new ArrayList<>();
		list.add(itemDesc);
		list.add(itemDesc2);
		String json = OBJECTMAPPER.writeValueAsString(list);
		System.out.println(json);
		//List<T>集合  接收对象List<ItemDesc>
		List<ItemDesc> list2 = 
				OBJECTMAPPER.readValue(json, list.getClass());
		//1.LinkedhashMap在类型上不能与POJO类型混用,但是在数值上可以通用的取值.
		//2.如果需要将map集合转化为对象 需要手动封装一下.set方法
		System.out.println(list2);
	}
	
	
	/**
	 * 原理说明:
	 * 	1.对象转化JSON时,其实调用的是对象身上的getXXXX()方法.
	 * 	获取所有的getLyj()方法-----之后去掉get-----首字母小写---lyj属性.
	 *  json串中的key就是该属性.value就是属性的值.  lyj:"xxxxx"
	 *  
	 *  2.JSON转化为对象原理说明
	 *  1).定义转化对象的类型(ItemDesc.class)
	 *  2).利用反射机制实例化对象  class.forName(class)   现在的属性都为null
	 *  3).将json串解析
	 *  			  object key:value
	 *  			  array  value1,value2
	 *  4).根据json串中的属性的itemId,之后调用对象的(set+首字母大写)setItemId方法实现赋值
	 */
	@Test
	public void test03() throws JsonProcessingException {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(100L)
				.setItemDesc("测试数据")
				.setCreated(new Date())
				.setUpdated(itemDesc.getCreated());
		
		//思考:对象转化为JSON时,底层实现如何.
		String json = OBJECTMAPPER.writeValueAsString(itemDesc);
		System.out.println(json);
		
		//{id:1,name:"xxxx"}
		OBJECTMAPPER.readValue(json,ItemDesc.class);
	}
	
	
	
}
