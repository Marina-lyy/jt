package com.jt.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.params.SetParams;

@SpringBootTest		//如果需要利用spring容器中的对象则添加注解
public class TestRedis {

	@Autowired
	private Jedis jedis2;
	
	@Test
	public void testJedis2() {
		
		jedis2.set("aaa", "你好Redis");
		System.out.println(jedis2.get("aaa"));
	}

	
	
	/**
	 * 1.检查防火墙 关闭
	 * 2.redis配置文件     IP绑定   保护模式  后台启动
	 * 3.启动redis的方式   redis-server redis.conf
	 */
	@Test
	public void testString01() {
		//1.链接Redis
		String host = "192.168.126.129";
		int port = 6379;
		Jedis jedis = new Jedis(host, port);
		//2.操作redis
		jedis.set("2002", "学习好辛苦!!!!");
		String value = jedis.get("2002");
		System.out.println(value);
		
		//3.判断redis中是否有指定数据
		if(!jedis.exists("2020")) {
			
			jedis.set("2020", "两会刚结束"); 
		}
		//4.删除
		jedis.del("2020");
		//5.检索数据
		System.out.println(jedis.keys("*"));
		//6.清空数据
		jedis.flushAll();
	}
	
	//测试类的初始化操作
	private Jedis jedis;
	
	@BeforeEach  //在执行@Test注解方法之前执行
	public void init() {
		
		jedis = new Jedis("192.168.126.129", 6379);
	}
	
	//问题:如果采用expire则不能保证超时时间的原子性(同时)操作!!!!
	//lock锁: 死锁!!!!
	@Test
	public void testStringEX() throws InterruptedException {
		jedis.set("abc", "测试数据的有效期");	//1.没有设定超时时间  永不过期
		//int a = 1/0;	//如果出错,则不能添加超时时间
		jedis.expire("abc", 5);				//2.设定超时
		Thread.sleep(2000);
		Long seconds = jedis.ttl("abc");
		System.out.println("abc剩余的存活时间:"+seconds);
		//jedis.persist("abc");
		
		//保证赋值的原子性操作
		jedis.setex("www", 10, "超时测试");
	}
	
	/**
	 * 需求说明:
	 * 	1.如果key存在时,不允许修改.
	 * @throws InterruptedException
	 */
	@Test
	public void testStringNX() throws InterruptedException {
		
		/*
		 * jedis.set("a", "123"); jedis.set("a", "456");
		 * System.out.println(jedis.get("a"));
		 */
		
		/*
		 * if(!jedis.exists("a")) {
		 * 
		 * jedis.set("a", "11111"); } System.out.println(jedis.get("a"));
		 */
		
		//如果key不存在时,则赋值
		jedis.setnx("a", "123");
		jedis.setnx("a", "456");
		System.out.println(jedis.get("a"));
		
	}
	
	
	/**
	 * 1.保证超时时间的原子性操作              EX
	 * 2.保证如果key存在,则不允许赋值.  NX
	 * 要求:又满足超时定义,同时满足数据不允许修改
	 * SetParams:参数
	 * 	 EX:秒
	 * 	 PX:毫秒
	 * 	 NX:有值不修改
	 * 	 XX:如果key不存在,则数据不修改. 只有key存在时,修改
	 */
	@Test
	public void testStringEXNX() {
		SetParams setParams = new SetParams();
		setParams.ex(20).xx();
		jedis.set("a", "66666666", setParams);
		System.out.println(jedis.get("a"));
	}
	
	/**
	 * 一般将有关联关系的数据利用hash方式进行保存.
	 * orderId:  
	 * 			userid:下单用户
	 * 			price:  xxxxx
	 * 			items:  [xxxxxx,xxxxxx]
	 * 			orderShipping: xxxxxxx	
	 */
	@Test
	public void testHash() {
		jedis.hset("hash", "id", "100");
		jedis.hset("hash", "name", "abc");
		System.out.println(jedis.hgetAll("hash"));
		System.out.println(jedis.hkeys("hash"));
		System.out.println(jedis.hvals("hash"));
	}
	
	
	@Test
	public void testSet() {
		
		jedis.sadd("set1", "1","2","3");
		jedis.sadd("set2", "3","4","5");
		System.out.println(jedis.sdiff("set1","set2"));
		System.out.println(jedis.smembers("set1"));
	}
	
	@Test
	public void testList() {
		
		jedis.lpush("list", "1","2","3","4","5");
		String value = jedis.rpop("list");
		System.out.println(value);
	}
	
	
	@Test
	public void testTX() {
		
		Transaction  transaction =  jedis.multi();	//开始事务
		try {
			transaction.set("a", "a");
			transaction.set("b", "b");
			//int a = 1/0;
			transaction.exec();		//提交事务
		} catch (Exception e) {
			e.printStackTrace();
			transaction.discard();	//事务回滚
		}
	}
	
	
	//思考: shards的数据如何存储的????
	@Test
	public void testShards() {
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo("192.168.126.129",6379));
		shards.add(new JedisShardInfo("192.168.126.129",6380));
		shards.add(new JedisShardInfo("192.168.126.129",6381));
		ShardedJedis jedis = new ShardedJedis(shards);
		//利用shardedjedis对象操作的是多台redis
		jedis.set("shards", "测试redis分片是否正常!!!!");
		System.out.println(jedis.get("shards"));
 	}
	
}
