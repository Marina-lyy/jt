package com.jt.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

public class TestRedisSentinel {
	
	@Test
	public void test01() {
		//定义哨兵的集合信息       host:port
		Set<String> sentinels = new HashSet<String>();
		sentinels.add("192.168.126.129:26379");
		//定义了连接池  
		JedisSentinelPool pool = 
				new JedisSentinelPool("mymaster", sentinels);
		//从池中获取redis链接
		Jedis jedis = pool.getResource();
		jedis.set("aa", "你好我是哨兵机制!!!!");
		System.out.println(jedis.get("aa"));
		jedis.close(); //关闭链接
	}	
}
