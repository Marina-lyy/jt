package com.jt.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.Item;

public interface ItemMapper extends BaseMapper<Item>{
	
	@Select("select * from tb_item order by  updated desc limit #{start},#{rows}")
	List<Item> findItemByPage(int start, int rows);
	
	void deleteItems(Long[] ids);
	/**
	 * 如果是单值传参      array/list/map中key
	 * 如果是多值传参      必须写属性的名称  
	 * @Param("status key")int status(value) 将参数封装为Map集合 
	 * 
	 * @param status
	 * @param updated
	 * @param ids
	 */
	void updateStatus(int status,Date updated,Long[] ids);
	
}
