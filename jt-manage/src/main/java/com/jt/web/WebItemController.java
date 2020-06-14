package com.jt.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;

@RestController
@RequestMapping("/web/item")
public class WebItemController {
	
	@Autowired
	private ItemService itemService;
	/**
	 * 根据jt-web请求获取商品信息
	 * url:/findItemById?itemId=562379
	 * 参数:itemId
	 * 返回值: item对象
	 */
	@RequestMapping("/findItemById")
	public Item findItemById(Long itemId) {
		
		return itemService.findItemById(itemId);
	}
	
	@RequestMapping("/findItemDescById")
	public ItemDesc findItemDescById(Long itemId) {
		
		return itemService.findItemDescById(itemId);
	}
	
}
