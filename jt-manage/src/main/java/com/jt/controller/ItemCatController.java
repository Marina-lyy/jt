package com.jt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jt.anno.CacheFind;
import com.jt.anno.CacheUpdate;
import com.jt.pojo.ItemCat;
import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;

@RestController	//将返回值转化为json
@RequestMapping("/item/cat")
public class ItemCatController {

	@Autowired
	private ItemCatService itemCatService;
	
	/**
	 * 业务需求:根据Id查询商品分类的名称
	 * 1.url:/item/cat/queryItemName
	 * 2.请求参数:itemCatId
	 * 3.返回值类型: 商品分类名称
	 */
	@RequestMapping("/queryItemName")
	@CacheFind(perKey = "ITEM_CAT_NAME")
	public String queryItemName(Long itemCatId) {
		
		ItemCat itemCat = 
				itemCatService.findItemCatNameById(itemCatId);
		return itemCat.getName();
	}
	
	
	/**
	 * url:http://localhost:8091/item/cat/list
	 * 参数:  暂时没有参数
	 * 返回值: List<EasyUITree>对象
	 * 
	 * 业务说明:
	 * 	1.用户第一次查询时展现的是全部的一级目录   没有携带数据  应该指定默认值
	 * 	2.当用户查询子级目录时 ,会携带当前节点的id
	 *  
	 * 参数转化注解:@RequestParam
	 * 作用:接收用户参数,并且可以实现数据的转化
	 * 参数说明:
	 * 		value/name: 用户传递的参数名称
	 * 		boolean required() default true;  是否为必传项
	 * 		defaultValue: 设定默认值
	 * 
	 * 
	 * 
	 * 说明:如果用户没有传递参数则parentId默认为0,否则使用用户的参数查询子级信息.
	 * 
	 * 
	 * 
	 * SpringMVC:功能可以自动的根据接收参数的类型不同,动态的变化,自动的实现类型转化
	 * request对象中只能携带什么类型的参数: string数据类型 规定
	 * 
	 */
	//json之后再无差别
	@RequestMapping("/list")
	@CacheFind(perKey = "ITEM_CAT")
	public List<EasyUITree> findItemCatList(
			@RequestParam(value="id",defaultValue="0")Long parentId){
		/*
		 * Long parentId = id; if(parentId ==null) { parentId = 0L; }
		 */
		//该操作表示查询数据库
		return itemCatService.findItemCatList(parentId);
		//表示查询缓存
		//return itemCatService.findItemCatCache(parentId);
	}
	
	
	/**
	 * 该方法是一个测试方法  主要目的测试更新缓存是否有效
	 * 要求:将返回值结果更新到缓存中
	 * 利用restFul风格实现测试
	 */
	@RequestMapping("/findItemCatById/{id}")
	@CacheFind(perKey = "ITEM_CAT_ID")
	public ItemCat findItemCatById(@PathVariable Long id) {
		
		return itemCatService.findItemCatById(id);
	}
	
	//如果采用对象的方式接收.则可以省略注解
	@RequestMapping("/updateItemCat/{name}/{status}/{id}")
	@CacheUpdate(perKey = "ITEM_CAT_ID")
	public ItemCat updateItemCatById(ItemCat itemCat) {
		
		return itemCatService.updateItemCatById(itemCat);
	}
	 
	
	
	
	
	
	
	
	
	
	
	
	
}
