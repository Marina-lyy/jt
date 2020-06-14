package com.jt.dubbo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;
import com.jt.service.DubboCartService;

@Service
public class DubboCartServiceImpl implements DubboCartService{

	@Autowired
	private CartMapper cartMapper;
	
	@Override
	@Transactional
	public void updateCartNum(Cart cart) {
		
		Cart cartTemp = new Cart();
		cartTemp.setNum(cart.getNum())
				.setUpdated(new Date());
		
		UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("user_id", cart.getUserId())
					.eq("item_id", cart.getItemId());
		cartMapper.update(cartTemp, updateWrapper);
	}

	@Override
	public List<Cart> findCartListById(Long userId) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		return cartMapper.selectList(queryWrapper);
	}
	
	/**
	 * 如果购物车中有记录,则更新数据
	 * 问题:如何判断购物车中是否有记录  user_id/item_id
	 */
	@Override
	@Transactional
	public void saveCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", cart.getUserId())
					.eq("item_id", cart.getItemId());
		
		Cart cartDB = cartMapper.selectOne(queryWrapper);
		
		if(cartDB ==null) {  //应该购物车新增
			cart.setCreated(new Date())
				.setUpdated(cart.getCreated());
			cartMapper.insert(cart);
			
		}else {//跟新数据库数量
			int num = cart.getNum()+cartDB.getNum();
			cartDB.setNum(num).setUpdated(new Date());
			cartMapper.updateById(cartDB);
		}
	}
	
	@Override
	public void deleteCart(Cart cart) {
		//MP规则:根据对象中不为null的属性
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>(cart);
		cartMapper.delete(queryWrapper);
		
		
	}
	
}
