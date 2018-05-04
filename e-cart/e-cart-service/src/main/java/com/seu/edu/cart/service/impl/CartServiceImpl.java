package com.seu.edu.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.seu.edu.cart.service.CartService;
import com.seu.edu.common.jedis.JedisClient;
import com.seu.edu.common.utils.EResult;
import com.seu.edu.common.utils.JsonUtils;
import com.seu.edu.mapper.TbItemMapper;
import com.seu.edu.pojo.TbItem;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private JedisClient jedisClient;
	
	@Autowired
	private TbItemMapper itemMapper;
	
	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	
	@Override
	public EResult addCart(long userId, long itemId, int num) {

		Boolean hexists = jedisClient.hexists(REDIS_CART_PRE + ":" + userId, itemId + "");
		if(hexists) {
			String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "'");
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			item.setNum(item.getNum() + num);
			jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
			return EResult.ok();
		}
		
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		item.setNum(num);
		String images = item.getImage();
		if(StringUtils.isNoneBlank(images)) {
			item.setImage(images.split(",")[0]);
		}
		jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
		
		return EResult.ok();
	}

	@Override
	public EResult mergeCart(long userId, List<TbItem> itemList) {
		
		for (TbItem tbItem : itemList) {
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		return EResult.ok();
	}

	@Override
	public List<TbItem> getCartList(long userId) {
		List<String> jsonList = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
		List<TbItem> itemList = new ArrayList<>();
		for (String string : jsonList) {
			TbItem item = JsonUtils.jsonToPojo(string, TbItem.class);
			itemList.add(item);
		}
		return itemList;
	}

	@Override
	public EResult updateCartNum(long userId, long itemId, int num) {
		String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId+"");
		TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
		item.setNum(num);
		jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId+"", JsonUtils.objectToJson(item));
		return EResult.ok();
	}

	@Override
	public EResult deleteCartItem(long userId, long itemId) {
		jedisClient.hdel(REDIS_CART_PRE + ":" + userId, itemId+"");
		return EResult.ok();
	}

	@Override
	public EResult clearCartItem(long userId) {
		jedisClient.del(REDIS_CART_PRE + ":" + userId);
		return null;
	}

}
