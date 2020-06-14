package com.jt.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class TestRedisCluster {
	
	//分片采用服务器算法 一致性hash算法,redis只负责存储
	
	//redis集群采用了hash槽算法
	//6台redis  3台主机主要负责数据的存储(扩容),从机主负责数据的同步.
	@Test
	public void test01() {
		Set<HostAndPort> set = new HashSet<HostAndPort>();
		set.add(new HostAndPort("192.168.126.129", 7000));
		set.add(new HostAndPort("192.168.126.129", 7001));
		set.add(new HostAndPort("192.168.126.129", 7002));
		set.add(new HostAndPort("192.168.126.129", 7003));
		set.add(new HostAndPort("192.168.126.129", 7004));
		set.add(new HostAndPort("192.168.126.129", 7005));
		//链接redis的集群
		JedisCluster jedisCluster = new JedisCluster(set);
		jedisCluster.set("cluster", "redis集群搭建成功 哈哈哈哈");
		System.out.println(jedisCluster.get("cluster"));
	}
}
