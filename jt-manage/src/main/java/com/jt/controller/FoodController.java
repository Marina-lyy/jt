package com.jt.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Food;
import com.jt.vo.EasyUITable;

/**
 * 该方法是测试使用 不作为业务逻辑
 * @author pc
 * 返回数据json串
 */
@RestController
public class FoodController {
	
	/**
	 * 1.url:	/food/findFood
	 * 2.参数:   ?page=1&rows=10
	 * 3.返回值   EasyUITable
	 */
	@RequestMapping("/food/findFood")
	public EasyUITable findFood(Integer page,Integer rows) {
		System.out.println("动态获取分页数据信息:"+page+":"+rows);
		
		int total = 3888;
		//该list集合是页面展现的数据集合 数据库查询
		List<Food> foodList = new ArrayList<>();
		Food food1 = new Food();
		food1.setCode(100L);
		food1.setName("溜肥肠!!!");
		food1.setPrice(10L);
		
		Food food2 = new Food();
		food2.setCode(200L);
		food2.setName("青草");
		food2.setPrice(30L);
		foodList.add(food1);
		foodList.add(food2);
		EasyUITable easyUITable = new EasyUITable(total, foodList);
		return easyUITable;
	}
	
}
