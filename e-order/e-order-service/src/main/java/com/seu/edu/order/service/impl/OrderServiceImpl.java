package com.seu.edu.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.seu.edu.common.jedis.JedisClient;
import com.seu.edu.common.utils.EResult;
import com.seu.edu.mapper.TbOrderItemMapper;
import com.seu.edu.mapper.TbOrderMapper;
import com.seu.edu.mapper.TbOrderShippingMapper;
import com.seu.edu.order.pojo.OrderInfo;
import com.seu.edu.order.service.OrderService;
import com.seu.edu.pojo.TbOrder;
import com.seu.edu.pojo.TbOrderItem;
import com.seu.edu.pojo.TbOrderShipping;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;
	
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${ORDER_ID_GEN_KEY}")
	private String ORDER_ID_GEN_KEY;
	
	@Value("${ORDER_ID_START}")
	private String ORDER_ID_START;
	
	@Value("${ORDER_DETAIL_ID_GEN_KEY}")
	private String ORDER_DETAIL_ID_GEN_KEY;
	@Override
	public EResult createOrder(OrderInfo orderInfo) {
		
		if(jedisClient.exists(ORDER_ID_GEN_KEY)) {
			jedisClient.set(ORDER_ID_GEN_KEY, ORDER_ID_START);
		}
		String orderId = jedisClient.incr(ORDER_ID_GEN_KEY).toString();
		
		orderInfo.setOrderId(orderId);
		orderInfo.setStatus(1);
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		orderMapper.insert(orderInfo);

		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		for (TbOrderItem orderItem : orderItems) {
			String odId = jedisClient.incr("ORDER_DETAIL_ID_GEN_KEY").toString();
			orderItem.setId(odId);
			orderItem.setItemId(orderId);
			orderItemMapper.insert(orderItem);
		}
		
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		orderShippingMapper.insert(orderShipping);
		
		return EResult.ok(orderId);
	}

}
