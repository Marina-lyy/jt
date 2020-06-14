package com.jt.dubbo.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

@Service
public class DubboUserServiceImpl implements DubboUserService {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private JedisCluster jedisCluster;
	/**
	 * 1.需要将密码加密  MD5方式
	 * 2.用电话号码代替Email
	 * 3.新增时间
	 */
	@Override
	@Transactional  
	public void saveUser(User user) {
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass)
			.setEmail(user.getPhone())
			.setCreated(new Date())
			.setUpdated(user.getCreated());
			
	
		userMapper.insert(user);
		
	}
	/**
	 * 1.根据用户名和密码查询数据库
	 * 2.有结果   :  用户的输入正确
	 *   没有结果    用户输入有误
	 */
	@Override
	public String findUserByUP(User user) {
		//1.将密码进行加密处理
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		//2.根据用户信息查询数据库
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("username",user.getUsername())
					.eq("password",md5Pass);
		User userDB = userMapper.selectOne(queryWrapper);
		//3.判断数据库中是否有记录
		String uuid = null;
		if(userDB != null) {
			//3.1准备uuid
			uuid = UUID.randomUUID().toString().replace("-", "");
			//将一些涉密数据察除
			userDB.setPassword("123456");
			String userJSON = ObjectMapperUtil.toJSON(userDB);
			jedisCluster.setex(uuid, 7*24*60*60,userJSON);
		}
		return uuid;
	}
}
