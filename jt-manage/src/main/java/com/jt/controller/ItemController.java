package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
/**
 * 业务说明:由于easyUI要求返回的数据都是json串,所以RestController简化代码
 * @author pc
 *
 */
import com.jt.vo.EasyUITable;
import com.jt.vo.SysResult;
@RestController
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	/**
	 * 1.url地址:http://localhost:8091/item/query?page=1&rows=50
	 * 2.参数:   page=1&rows=50
	 * 3.返回值: easyUI表格数据要求   EasyUITable VO对象
	 */
	@RequestMapping("/query")
	public EasyUITable findItemByPage(int page,int rows) {
		
		//在业务层,实现分页处理
		return itemService.findItemByPage(page,rows);
	}
	
	
	/**
	 * 1.url:http://localhost:8091/item/save
	 * 2.参数: form表单提交
	 * 3.返回值: VO对象
	 */
	@RequestMapping("/save")
	public SysResult saveItem(Item item,ItemDesc itemDesc) {
		/*
		 * try { itemService.saveItem(item); return SysResult.success(); } catch
		 * (Exception e) { e.printStackTrace(); return SysResult.fail(); //try-catch
		 * 必然导致代码可读性差 优化 }
		 */
		itemService.saveItem(item,itemDesc);
		return SysResult.success();
	}
	
	
	/**
	 * 商品更新
	 * 1.url:/item/update
	 * 2.参数:form表单序列化
	 * 3.返回值:Sysresult对象
	 */
	@RequestMapping("/update")
	public SysResult updateItem(Item item,ItemDesc itemDesc) {
		
		itemService.updateItem(item,itemDesc);
		return SysResult.success();
	}
	
	
	/**
	 * url:/item/delete
	 * 参数: ids:1001,1002  如果中间有","号 则可以使用数组/可变参数方式接收
	 * 返回值: SysResult
	 */
	@RequestMapping("/delete")
	public SysResult deleteItems(Long[] ids) {
		
		itemService.deleteItems(ids);
		return SysResult.success();
	}
	
	/**
	 * 1.url:http://localhost:8091/item/updateStatus/2
	 * 2.参数:ids: 1474391963,1001,1002
	 * 3.返回值结果  SysResult
	 * @return
	 */
	@RequestMapping("/updateStatus/{status}")
	public SysResult updateStatus(@PathVariable int status,Long... ids) {

		itemService.updateStatus(status,ids);
		return SysResult.success();
	}
	
	
	/**
	 * 1.url地址:http://localhost:8091/item/query/item/desc/1474391972
	 * 2.请求参数: 无
	 * 3.返回值: SysResult对象
	 */
	@RequestMapping("/query/item/desc/{itemId}")
	public SysResult findItemDescById(@PathVariable Long itemId) {
		
		ItemDesc itemDesc = itemService.findItemDescById(itemId);
		return SysResult.success(itemDesc);
	}
	
	
	
	
	
	
}
