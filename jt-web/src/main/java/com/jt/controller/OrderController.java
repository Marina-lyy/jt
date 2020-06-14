package com.jt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.service.DubboOrderService;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/order")
public class OrderController {

	private static final String USER = "JT_USER";
	@Reference(check = false)
	private DubboCartService cartService;
	
	@Reference(check = false)
	private DubboOrderService orderService;
	/**
	 * 跳转订单页面
	 * 1.url地址:http://www.jt.com/order/create.html
	 * 2.参数:页面没有传值  查询某个用户的订单userId
	 * 3.返回值:order-cart.jsp
	 * 页面的取值:${carts}  需要展现购物车的记录
	 */
	@RequestMapping("/create")

	public String findOrderCreate(HttpServletRequest request,Model model) {
		User user = (User) request.getAttribute("JT_USER");
		Long userId = user.getId();
		List<Cart> cartList =  cartService.findCartListById(userId);
		model.addAttribute("carts", cartList);
		return "order-cart";
	}
	
	/**
	 * 订单新增操作
	 * 1.url:order/submit
	 * 2.参数:form表达
	 * 3.返回值:SysResult
	 */
	@RequestMapping("/submit")
	@ResponseBody
	public SysResult saveOreder(Order order,HttpServletRequest request) {
		User user = (User) request.getAttribute(USER);
		Long userId = user.getId();
		order.setUserId(userId);
		String orderId= orderService.saveOrder(order);
		if(StringUtils.isEmpty(orderId)) {
			return SysResult.fail();
		}else {
			return SysResult.success(orderId);
		}
	}
	/**
	 * url:http://www.jt.com/order/success.html?id=71591770618729
	 * 参数:id=xxxxx  订单号
	 * 返回值:
	 */
	@RequestMapping("success")
	public String findOrderById(String id,Model model) {
		
		Order order = orderService.findOrderById(id);
		model.addAttribute("order", order);
		return "success";
	}
	
}
