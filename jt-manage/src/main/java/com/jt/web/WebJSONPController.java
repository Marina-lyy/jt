package com.jt.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.ItemDesc;
import com.jt.util.ObjectMapperUtil;

@RestController
public class WebJSONPController {
	
	/**
	 * url地址:
	 * 		http://manage.jt.com/web/testJSONP?callback=jQuery111108175936158972192_1591340174795&_=1591340174796
	 * 参数:
	 * 		callback=回调函数
	 * 返回值:  callback(json)
	 */
	//@RequestMapping("/web/testJSONP")
	public String testJSONP(String callback) {
		
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemDesc("测试数据");
		itemDesc.setItemId(1000L);
		String json = ObjectMapperUtil.toJSON(itemDesc);
		//需要手动的拼接json串
		return callback+"("+json+")";
	}
	
	@RequestMapping("/web/testJSONP")
	public JSONPObject testJSONP2(String callback) {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemDesc("测试数据");
		itemDesc.setItemId(1000L);
		
		return new JSONPObject(callback, itemDesc);
	}
}
