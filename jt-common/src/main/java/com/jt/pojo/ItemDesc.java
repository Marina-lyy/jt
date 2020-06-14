package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data	//toString只针对当前属性
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_item_desc")
public class ItemDesc extends BasePojo{
	
	@TableId //只设定主键  不能自增
	private Long itemId;
	private String itemDesc;
	
	
	/*
	 * public String getLyj() {
	 * 
	 * return "超级帅!!!!!"; }
	 */
	
	
	
	
	
	
}
