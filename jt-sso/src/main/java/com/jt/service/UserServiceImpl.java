package com.jt.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;

	/**
	 * 任务: 根据用户传参 校验数据是否存在
	 * 如何判断数据是否存在????
	 * 		1.根据条件查询数据返回对象   之后检查对象是否为null
	 * 		2.根据条件查询数据返回记录数 ,之后校验是否为0
	 * 判断sql: select count(*) from tb_user where 
	 */
	@Override
	public boolean checkUser(String param, Integer type) {
		//1.实现type与字段的匹配.   1-username 2-phone  3-email  5.座机  6.xxx
		Map<Integer,String> map = new HashMap<Integer, String>();
		map.put(1, "username");
		map.put(2, "phone");
		map.put(3, "email");
		String column = map.get(type);
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
		queryWrapper.eq(column, param);
		//count表示记录数   >0 true
		int count = userMapper.selectCount(queryWrapper);
		return count>0?true:false;
	}
	
	
	
}
