package com.jt.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;

@Service
public class OrderServiceImpl implements DubboOrderService {
	
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	
	/**
	 * 入库3张表数据
	 */
	@Override
	@Transactional
	public String saveOrder(Order order) {
		//1.准备orderId
		String orderId=""+order.getUserId()+System.currentTimeMillis();
		Date date = new Date();
		
		//2.入库订单物流信息
		order.setOrderId(orderId).setStatus(1).setCreated(date).setUpdated(date);
		orderMapper.insert(order);
		System.out.println("订单入库成功");
		
		OrderShipping orderShipping = order.getOrderShipping();
		orderShipping.setOrderId(orderId).setCreated(date).setUpdated(date);
		orderShippingMapper.insert(orderShipping);
		System.out.println("订单物流入库成功");
		
		//3.入库订单商品
		//手写动态SQL,实现批量入库操作,   sql:insert into tb_order(xxxx,xxxx,...) values(xxx,xxx,xxx),(xxxx,xxx,xxx)
		List<OrderItem> orderItems = order.getOrderItems();
		for (OrderItem orderItem : orderItems) {
			orderItem.setOrderId(orderId).setCreated(date).setUpdated(date);
			orderItemMapper.insert(orderItem);
		}
		System.out.println("订单操作成功");
		return orderId;
	}
	
	@Override
	public Order findOrderById(String id) {
		Order order = orderMapper.selectById(id);
		OrderShipping shipping = orderShippingMapper.selectById(id);
		QueryWrapper<OrderItem> queryWrapper =new QueryWrapper<>();
		queryWrapper.eq("order_id", id);
		List<OrderItem> orderList = orderItemMapper.selectList(queryWrapper);
		return order.setOrderItems(orderList).setOrderShipping(shipping);
	}
}
