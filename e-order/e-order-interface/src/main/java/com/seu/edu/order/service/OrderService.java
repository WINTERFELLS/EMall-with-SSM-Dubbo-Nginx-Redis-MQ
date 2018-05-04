package com.seu.edu.order.service;

import com.seu.edu.common.utils.EResult;
import com.seu.edu.order.pojo.OrderInfo;

public interface OrderService {
	EResult createOrder(OrderInfo orderInfo);
}
