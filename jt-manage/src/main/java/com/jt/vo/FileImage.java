package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

//实现图片回显VO
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class FileImage {
	
	private Integer error;   //=0
	private String url;
	private Integer width;
	private Integer height;
	
	public static FileImage success(String url,Integer width,Integer height) {
		
		return new FileImage(0, url, width, height);
	}
	
	public static FileImage fail() {
		
		return new FileImage(1, null, null, null);
	}
}
