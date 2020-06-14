package com.jt.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
//如果需要使用spring容器注入对象,则本身应该交给容器管理
@Service
public class HttpClientService {
	@Autowired
	private CloseableHttpClient httpClient;
	@Autowired
	private RequestConfig requestConfig;


	/**
	 * 工具API作用:
	 * 	 为了用户简化操作而定义该工具API.
	 * 	 目的:用户通过如下方法实现远程url调用并且获取响应结果.
	 * 	 String result = httpClientService.doGet(url,xx,xx);
	 * 
	 * 编辑工具API考虑的问题:
	 * 	 1.参数问题     (1). url请求地址     (2).Map<String,String>  (3) 设定字符集编码格式
	 * 返回值问题: String类型
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 */

	public String doGet(String url,Map<String,String> params,String charset) {

		//1.校验字符集编码格式
		if(StringUtils.isEmpty(charset)) {
			//如果用户没有指定字符集,则设定默认值
			charset = "UTF-8";
		}

		//2.封装get请求的参数.
		//2.1 get请求时没有参数      http://manage.jt.com/getXXXX
		//2.2 get请求有参数              http://manage.jt.com/getXXX?id=1&name=xxx&
		if(params != null) {
			url += "?";
			//遍历map 动态获取key=value
			for (Map.Entry<String,String> entry : params.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				url += (key+"="+value+"&");
			}
			//http://manage.jt.com/getXXX?id=1&name=xxx&
			//去除多余的&符号
			url = url.substring(0, url.length()-1);
		}

		//3.准备请求对象
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(requestConfig);  //设定请求超时时间

		//4.利用httpClient发起请求
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			//获取状态码信息
			int status = 
					httpResponse.getStatusLine().getStatusCode();
			if(status == 200) {	//请求正确
				//获取返回值实体
				HttpEntity httpEntity = httpResponse.getEntity();
				String result = EntityUtils.toString(httpEntity,charset);
				return result;
			}

		} catch (IOException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return null;
	}


	//方法的重载      满足不同的用户需求
	public String doGet(String url) {

		return doGet(url, null, null);
	}

	public String doGet(String url,Map<String,String> params) {

		return doGet(url, params, null);
	}



	//实现httpClient POST提交
	public String doPost(String url,Map<String,String> params,String charset){
		String result = null;

		//1.定义请求类型
		HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig);  	//定义超时时间

		//2.判断字符集是否为null
		if(StringUtils.isEmpty(charset)){

			charset = "UTF-8";
		}

		//3.判断用户是否传递参数
		if(params !=null){
			//3.2准备List集合信息
			List<NameValuePair> parameters = 
					new ArrayList<>();

			//3.3将数据封装到List集合中
			for (Map.Entry<String,String> entry : params.entrySet()) {

				parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}

			//3.1模拟表单提交
			try {
				UrlEncodedFormEntity formEntity = 
						new UrlEncodedFormEntity(parameters,charset); //采用u8编码

				//3.4将实体对象封装到请求对象中
				post.setEntity(formEntity);
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
			}
		}

		//4.发送请求
		try {
			CloseableHttpResponse response = 
					httpClient.execute(post);

			//4.1判断返回值状态
			if(response.getStatusLine().getStatusCode() == 200) {

				//4.2表示请求成功
				result = EntityUtils.toString(response.getEntity(),charset);
			}else{
				System.out.println("获取状态码信息:"+response.getStatusLine().getStatusCode());
				throw new RuntimeException();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}



	public String doPost(String url){

		return doPost(url, null, null);
	}

	public String doPost(String url,Map<String,String> params){

		return doPost(url, params, null);
	}

	public String doPost(String url,String charset){

		return doPost(url, null, charset);
	}
}
