package com.jt.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jt.service.FileService;
import com.jt.vo.FileImage;

@RestController
public class FileController {
	
	
	/**
	 * 业务:实现文件上传入门案例
	 * 1.url:http://localhost:8091/file
	 * 2.请求参数: fileImage   参数名称必须与页面提交name属性一致.
	 * 3.返回值:"文件上传成功"
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	
	@RequestMapping("/file")
	public String file(MultipartFile  fileImage) throws IllegalStateException, IOException {
		//1.获取文件名称
		String fileName = fileImage.getOriginalFilename(); //获取文件真实名称
		//2.准备文件存储目录结构
		String dirPath = "D:/software/images";  //测试路径
		File dirFile = new File(dirPath);
		if(!dirFile.exists()) {
			//如果文件目录不存在,则需要新建文件目录
			dirFile.mkdirs();	//可以创建多级目录
		}
		//3.利用工具API 实现文件上传
		File file = new File(dirPath+"/"+fileName);
		fileImage.transferTo(file); //将流数据输出到指定的位置
		return "文件上传成功!!!!";
	}
	
	
	@Autowired
	private FileService fileService;
	
	/**
	 * 实现图片文件上传
	 * 1.url:/pic/upload   url请求路径
	 * 2.参数: uploadFile
	 * 3.返回值类型: FileImage
	 */
	@RequestMapping("/pic/upload")
	public FileImage upload(MultipartFile uploadFile) {
		
		return fileService.upload(uploadFile);
	}
	
	
	
	
	
}
