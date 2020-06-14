package com.jt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Reference(check = false)
	private DubboCartService cartService;
	
	/**
	 * 业务需求:展现购物车列表页面
	 * url:
	 * 参数:无
	 * 返回值:无
	 * 页面取值:${cartList}
	 * 
	 */
	@RequestMapping("/show")
	public String findCartList(Model model,HttpServletRequest request) {
		User user = (User) request.getAttribute("JT_USER");
		
		Long userId = user.getId();
		List<Cart> cartList=cartService.findCartListById(userId);
		model.addAttribute("cartList", cartList);
		return "cart";
	}
	
	/**
	 * 购物车新增
	 * url: http://www.jt.com/cart/add/562379.html
	 * 2.参数: form表单的形式提交  Cart对象接收
	 * 3.返回值:重定向到购物车列表页面
	 * 注意事项:如果购物车有记录,则更新数量
	 * 
	 */
	@RequestMapping("/add/{itemId}") //可以自动的为属性赋值
	public String saveCart(Cart cart,HttpServletRequest request) {
		User user = (User) request.getAttribute("JT_USER");
		
		Long userId = user.getId();
	
		cart.setUserId(userId);
		
		cartService.saveCart(cart);
		return "redirect:/cart/show.html";
		
	}
	/**
	 * 购物车删除操作
	 * http://www.jt.com/cart/delete/1369278.html
	 * 参数: 商品id号
	 * 返回值:重定向到购物车列表页面
	 */
	@RequestMapping("/delete/{itemId}") //可以自动的为属性赋值
	public String deleteCart(Cart cart,HttpServletRequest request) {
		User user = (User) request.getAttribute("JT_USER");
		
		Long userId = user.getId();
	
		cart.setUserId(userId);
		cartService.deleteCart(cart);
		return "redirect:/cart/show.html";
	}
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateCartNum(Cart cart,HttpServletRequest request) {
		User user = (User) request.getAttribute("JT_USER");
		Long userId = user.getId();
		cart.setUserId(userId);
		cartService.updateCartNum(cart);
		
		return SysResult.success();
	}
}
