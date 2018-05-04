package com.seu.edu.cart.service;

import java.util.List;

import com.seu.edu.common.utils.EResult;
import com.seu.edu.pojo.TbItem;

public interface CartService {
	EResult addCart(long userId, long itemId, int num);
	EResult mergeCart(long userId, List<TbItem> itemList);
	List<TbItem> getCartList(long userId);
	EResult updateCartNum(long userId, long itemId, int num);
	EResult deleteCartItem(long userId, long itemId);
	EResult clearCartItem(long userId);
}
