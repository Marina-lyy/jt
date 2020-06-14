package com.jt.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.ItemCat;
			//com.jt.util
public class ObjectMapperUtil {

	//1.定义mapper对象
	private static final  ObjectMapper MAPPER = new ObjectMapper();
	
	//2.将对象转化为JSON
	public static String toJSON(Object target) {
		try {
			return MAPPER.writeValueAsString(target); 
		} catch (JsonProcessingException e) {
			//检查异常转化为运行时异常
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	//3.将JSON串转化为对象   用户传递什么类型,就能返回什么对象
	//需求:	ItemCat itemCat = (ItemCat) ObjectMapperUtil.toObj("", ItemCat.class);
	//什么时候使用泛型:  不知道用户传递什么样的数据 就可以使用泛型代替/object
	//<T> 定义了泛型对象
	public static <T> T toObj(String json,Class<T> target) {
		try {
			return MAPPER.readValue(json, target);
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
