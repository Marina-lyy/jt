package com.jt.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jt.util.HttpClientService;

//开发springCloud远程调用的源码 /是开发银行SDK的中组件/访问其他服务器数据的万能用法
@SpringBootTest
public class TestHttpClient {
	
	/**
	 * 步骤:
	 * 	1.确定目标服务器的网址
	 * 	2.定义请求类型    get/post/put/delete
	 *  3.创建httpClient工具API对象
	 *  4.发起http请求,获取服务器响应数据.
	 *  5.获取服务器响应之后开始解析数据. 判断状态码信息 200/404/406/500/504
	 *  6.获取数据执行业务调用.
	 * 	
	 * 	httpClient可以当做简单的爬虫使用 JSOUP/PYTHON
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@Test
	public void testGet() throws ClientProtocolException, IOException {
		String url = "https://www.baidu.com";
		HttpGet httpGet = new HttpGet(url);
		HttpClient httpClient = HttpClients.createDefault();
		HttpResponse httpResponse = httpClient.execute(httpGet);
		int status = httpResponse.getStatusLine().getStatusCode();
		if(status == 200) {
			//表示请求正确
			HttpEntity httpEntity = httpResponse.getEntity();	//获取响应数据
			String data = EntityUtils.toString(httpEntity, "UTF-8");
			System.out.println(data);
		}else {
			System.out.println("请求有误!!!!!!!!!"+status);
		}
		
	}
	
	
	@Autowired
	private HttpClientService httpClient;
	
	@Test
	public void doGet() {
		Map<String,String> params = new HashMap<String, String>();
		params.put("name", "httpClient说明");
		params.put("age", "19");
		params.put("sex", "男");
		String url = "http://www.baidu.com";
		String result = httpClient.doGet(url, params, null);
		System.out.println(result);
	}
	
}
