package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;

@Controller
@RequestMapping("/items")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	/**
	 * items/562379.html
	 * 
	 * 1.页面取值:${item.title}  el表达式的写法 request/session/application
	 * 2.获取商品描述信息  
	 * 			 ${itemDesc.itemDesc }    
	 * 		
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/{itemId}")
	public String findItemById(@PathVariable Long itemId,Model model) {
		
		//System.out.println("动态获取itemId:"+itemId);
		
		Item item = itemService.findItemById(itemId);
		ItemDesc itemDesc = itemService.findItemDescById(itemId);
		model.addAttribute("item",item);
		model.addAttribute("itemDesc",itemDesc);
		//跳转到指定的商品展现页面
		return "item";
	}
	
}
