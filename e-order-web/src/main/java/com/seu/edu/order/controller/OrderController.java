package com.seu.edu.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.seu.edu.cart.service.CartService;
import com.seu.edu.common.utils.EResult;
import com.seu.edu.order.pojo.OrderInfo;
import com.seu.edu.order.service.OrderService;
import com.seu.edu.pojo.TbItem;
import com.seu.edu.pojo.TbUser;

@Controller
public class OrderController {

	@Autowired
	private CartService cartService;
	
	@Autowired 
	private OrderService orderService;
	
	@RequestMapping("/order/order-cart")
	public String showOrderCart(HttpServletRequest request) {
		
		TbUser user = (TbUser) request.getAttribute("user");
		List<TbItem> list = cartService.getCartList(user.getId());
		request.setAttribute("cart-list", list);
		return "order-cart";
	}
	
	@RequestMapping(value="/order/create", method=RequestMethod.POST)
	public String createOrder(OrderInfo orderInfo,  HttpServletRequest request) {
		TbUser user = (TbUser) request.getAttribute("user");
		
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		
		EResult result = orderService.createOrder(orderInfo);
		if(result.getStatus() == 200) {
			cartService.clearCartItem(user.getId());
		}
		request.setAttribute("orderId", result.getData());
		request.setAttribute("payment", orderInfo.getPayment());
		
		return "success";
	}
}
