package com.jt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.anno.CacheFind;
import com.jt.mapper.ItemCatMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITree;

import redis.clients.jedis.Jedis;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	@Autowired
	private ItemCatMapper itemCatMapper;
	
	@Autowired(required = false)	//不是必须注入
	private Jedis jedis;

	@Override
	//@CacheFind(perKey = "ITEM_CAT_ID")
	public ItemCat findItemCatNameById(Long itemCatId) {
		
		return itemCatMapper.selectById(itemCatId);
	}

	/**
	 * 数据转化:
	 * 	List<EasyUITree> VO对象 页面要求返回的数据结果
	 *  List<ItemCat>    数据库记录
	 *  ItemCat对象转化EasyUITree对象
	 *  
	 */
	@Override
	@CacheFind(perKey = "ITEM_CAT_PARENTID")
	public List<EasyUITree> findItemCatList(Long parentId) {
		//1.根据parentId查询数据库记录 
		List<ItemCat> catList = findItemCatListByParentId(parentId);
		List<EasyUITree> treeList = new ArrayList<EasyUITree>();
		//2.利用循环的方式实现数据的遍历
		for (ItemCat itemCat : catList) {
			//目的为了封装VO对象
			Long id = itemCat.getId();
			String text = itemCat.getName();	//获取节点名称
			//如果是父级则默认closed,否则open  可以被选中
			String state = itemCat.getIsParent() ? "closed" : "open";
			EasyUITree tree = new EasyUITree(id, text, state);
			//将tree对象封装到List集合中
			treeList.add(tree);
		}
		return treeList;
	}

	private List<ItemCat> findItemCatListByParentId(Long parentId) {
		QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<ItemCat>();
		queryWrapper.eq("parent_id", parentId);
		return itemCatMapper.selectList(queryWrapper);
	}
	
	/**
	 * 1.准备redis中的key
	 * 2.根据key查询redis缓存服务器.
	 * 		有值:  证明之前已经查询过,所以直接返回
	 * 		null:  表示第一次查询,先查询数据库.之后将数据保存到redis中
	 * 				方便以后使用.
	 */
	@Override
	public List<EasyUITree> findItemCatCache(Long parentId) {
		
		Long startTime = System.currentTimeMillis();
		String key = "ITEM_CAT::"+parentId;  //规范
		String json = jedis.get(key);
		List<EasyUITree> treeList = new ArrayList<EasyUITree>();
		if(StringUtils.isEmpty(json)) {
			//判断为null,则表示第一次查询
			treeList = findItemCatList(parentId);
			Long endTime = System.currentTimeMillis();
			System.out.println("查询数据库耗时:"+(endTime-startTime)+"毫秒");
			//需要将list集合转化为json保存到redis中
			json = ObjectMapperUtil.toJSON(treeList);
			jedis.set(key, json);
		}else {
			//json数据不为null,需要转化为对象之后返回
			treeList = 
					ObjectMapperUtil.toObj(json, treeList.getClass());
			Long endTime = System.currentTimeMillis();
			System.out.println("查询缓存耗时:"+(endTime-startTime)+"毫秒");
		}
		
		return treeList;
	}

	@Override
	public ItemCat findItemCatById(Long id) {
		
		return itemCatMapper.selectById(id);
	}

	@Override
	public ItemCat updateItemCatById(ItemCat itemCat) {
		
		//规则:根据对象中不为null的属性当做where条件
		itemCatMapper.updateById(itemCat);
		
		return itemCatMapper.selectById(itemCat.getId());
	}
}
