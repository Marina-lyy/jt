package com.jt.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

@Configuration	//标识配置类  一般和@Bean注解联用
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {
	
	@Value("${redis.cluster}")
	private String nodes;	//node,node,node
	
	//配置redis集群
	@Bean
	public JedisCluster   jedisCluster() {
		//1.定义redis链接池
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(1000);
		poolConfig.setMaxIdle(20);  //最大空闲数量
		
		//2.准备redis节点信息
		Set<HostAndPort> set = new HashSet<>();
		String[] nodeArray = nodes.split(",");
		for (String node : nodeArray) {   //host:port
			String host = node.split(":")[0];
			int port = Integer.parseInt(node.split(":")[1]);
			set.add(new HostAndPort(host, port));
		}
		return new JedisCluster(set, poolConfig);
	}
	
	
	
	
	
	
	
	
	
	/**
	@Value("${redis.shards}")
	private String shards; //node,node,node
	
	/**
	 * 实现springBoot整合redis分片
	 */
	/**
	@Bean
	public ShardedJedis shardedJedis() {
		List<JedisShardInfo> list = new ArrayList<JedisShardInfo>();
		//1.将shards字符串转化为node数组
		String[] nodes = shards.split(","); //[{host:port},{host,port}]
		for (String strNode : nodes) {		//host:port
			//获取节点host信息
			String host = strNode.split(":")[0];
			//获取节点port信息
			int port = Integer.parseInt(strNode.split(":")[1]);
			//实现节点封装
			list.add(new JedisShardInfo(host, port));
		}
		
		return new ShardedJedis(list);
	}
	**/
	
	
	
	/**
	 * //单台测试去掉
	@Value("${redis.host}")
	private String host;
	@Value("${redis.port}")
	private Integer port;
	//将哪个对象交给容器管理,返回值就是谁...
	@Bean
	public Jedis jedis() {
		
		return new Jedis(host, port);
	}
	*/
}
