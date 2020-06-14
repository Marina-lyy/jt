package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("tb_item_cat")
@Data
@Accessors(chain = true)
public class ItemCat extends BasePojo{
	
	//POJO属性定义全部采用包装类型  切记!!!!  以对象中不为null的属性充当where条件
	@TableId(type = IdType.AUTO)
	private Long id;		//主键
	private Long parentId;	//父级id 
	private String name;	//名称
	private Integer status;	//状态信息
	private Integer sortOrder;	//排序号
	private Boolean isParent;	//是否为父级
	
}
