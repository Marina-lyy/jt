package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors
@NoArgsConstructor
@AllArgsConstructor
public class EasyUITree {
	private Long id;		//节点ID
	private String text;    //节点名称
	private String state;	//打开  open/关闭 closed 
	 
}
