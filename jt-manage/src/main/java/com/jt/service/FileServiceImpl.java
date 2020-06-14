package com.jt.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.vo.FileImage;

@Service
//加载指定的配置文件
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImpl implements FileService {
	
	/**
	 * 思考:
	 * 	1.校验用户上传的是否为图片类型    jpg|png
	 * 	2.校验图片信息是否为恶意程序        木马.exe.jpg
	 *  3.为了检索效率,采用分目录存储     策略1:可以根据hash值动态拆分路径信息 aas/dfa/dfa/sdf/1.jpg
	 *  							  策略2:可以采用以时间为维度的文件结构 yyyy/MM/dd  
	 *  4.防止文件重名  				 UUID形式定义文件名称
	 * 
	 * 文件上传实现具体步骤:
	 * 	1.动态获取图片名称        abc.jpg
	 * 
	 * 
	 */
	
	@Value("${image.localDir}")
	private String localDir;	// = "D:/software/images";  //定义本地存储路径
	@Value("${image.urlPath}")
	private String urlPath;		// = "http://image.jt.com";	 //定义虚拟空间地址
	
	@Override
	public FileImage upload(MultipartFile uploadFile) {
		
		//1.动态获取图片名称   a.jpg|A.JPG
		String fileName = uploadFile.getOriginalFilename();
		fileName = fileName.toLowerCase();	//将数据全部小写
		//2 正则表达式  匹配字符串是否满足规范
		if(!fileName.matches("^.+\\.(jpg|png|jpeg|gif)$")) {
			
			return FileImage.fail();
		}
		//3.准备一个图片类型模板对象,将用户上传的数据赋值给模板对象,如果赋值不成功,则表示为恶意程序
		try {
			BufferedImage bufferedImage = 
										ImageIO.read(uploadFile.getInputStream());
			int height = bufferedImage.getHeight();	//获取高度
			int width = bufferedImage.getWidth();	//获取宽度
			if(height == 0 || width == 0) {
				return FileImage.fail();
			}
			
			//4.准备文件存储路径 yyyy/MM/dd
			String dateDir = new SimpleDateFormat("/yyyy/MM/dd/")
							 .format(new Date());
			//5.准备文件保存的目录
			String imageDir = localDir + dateDir;
			File dirFile = new File(imageDir);
			if(!dirFile.exists()) {
				
				dirFile.mkdirs();	//如果文件目录不存在,则创建
			}
			
			//6.利用UUID定义文件名称
			String uuid = UUID.randomUUID().toString().replace("-", "");
			String fileType = fileName.substring(fileName.lastIndexOf("."));
			String realFileName = uuid+fileType;
			
			//7.实现图片上传
			uploadFile.transferTo(new File(imageDir+realFileName));
			
			//8.拼接图片的虚拟访问地址http://image.jt.com/yyyy/MM/dd/a.jpg
			String url = urlPath + dateDir + realFileName;
			return FileImage.success(url, width, height);
		
		} catch (IOException e) {
			e.printStackTrace();
			return FileImage.fail();
		}
	}

}
