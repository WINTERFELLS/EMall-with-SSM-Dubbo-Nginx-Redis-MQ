package com.seu.edu.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seu.edu.cart.service.CartService;
import com.seu.edu.common.utils.CookieUtils;
import com.seu.edu.common.utils.EResult;
import com.seu.edu.common.utils.JsonUtils;
import com.seu.edu.pojo.TbItem;
import com.seu.edu.pojo.TbUser;
import com.seu.edu.service.ItemService;

@Controller
public class CartController {

	@Autowired
	private ItemService itemService;
	
	@Autowired
	private CartService cartService;
	
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId, @RequestParam(defaultValue="1") Integer num, 
			HttpServletRequest request, HttpServletResponse response) {
		
		TbUser user = (TbUser) request.getAttribute("user");
		if(user != null) {
			cartService.addCart(user.getId(), itemId, num);
			return "cartSuccess";
		}
		
		List<TbItem> cartList = getCartListFromCookie(request);
		
		boolean flag = false;
		for (TbItem tbItem : cartList) {
			if(tbItem.getId() == itemId.longValue()) {
				flag = true;
				tbItem.setNum(tbItem.getNum()+num);
				break;
			}
		}
		
		if(!flag) {
			TbItem tbItem = itemService.getItemById(itemId);
			tbItem.setNum(num);
			String image = tbItem.getImage();
			if(StringUtils.isBlank(image)) {
				tbItem.setImage(image.split(",")[0]);
			}
			cartList.add(tbItem);
		}
		
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), 432000, true);
		return "cartSuccess";
	}
	
	private List<TbItem> getCartListFromCookie(HttpServletRequest request){
		String json = CookieUtils.getCookieValue(request, "cart", true);
		if(StringUtils.isBlank(json)) {
			return new ArrayList<>();
		}
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
	
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request, HttpServletResponse response) {
		
		List<TbItem> cartList = getCartListFromCookie(request);
		
		TbUser user = (TbUser) request.getAttribute("user");
		if(user != null) {
			cartService.mergeCart(user.getId(), cartList);
			CookieUtils.deleteCookie(request, response, "cart");
			cartList = cartService.getCartList(user.getId());
			
		}
		
		request.setAttribute("cartList", cartList);
		return "cart";
	}
	
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public EResult updateCartNum(@PathVariable Long itemId, @PathVariable Integer num, 
			HttpServletRequest request, HttpServletResponse response) {
		
		TbUser user = (TbUser) request.getAttribute("user");
		if(user != null) {
			return cartService.updateCartNum(user.getId(), itemId, num);
		}
		
		List<TbItem> cartList = getCartListFromCookie(request);
		
		for (TbItem tbItem : cartList) {
			if(tbItem.getId() == itemId.longValue()) {
				tbItem.setNum(num);
				break;
			}
		}
		
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), 432000, true);
		return EResult.ok();
	}
	
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
		
		TbUser user = (TbUser) request.getAttribute("user");
		if(user != null) {
			cartService.deleteCartItem(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		
		List<TbItem> cartList = getCartListFromCookie(request);
		for (TbItem tbItem : cartList) {
			if(tbItem.getId() == itemId.longValue()) {
				cartList.remove(tbItem);
				break;
			}
		}
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), 432000, true);
		return "redirect:/cart/cart.html";
	}
}
