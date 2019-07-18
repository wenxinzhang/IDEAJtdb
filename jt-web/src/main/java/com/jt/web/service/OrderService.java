package com.jt.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.spring.exetend.PropertyConfig;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.Order;

@Service
public class OrderService {
	@Autowired
	private HttpClientService httpClientService;
	@PropertyConfig
	private String ORDER_URL;
	private static final ObjectMapper MAPPER = new ObjectMapper();
	@Autowired
	private CartService cartService;
	

	//http://order.jt.com/order/create
	public Long create(Order order, String ticket) {
		Long userId = Long.parseLong(cartService.getUserId(ticket));
		order.setUserId(userId);
		order.setStatus(1);		//未支付
		
		String url = ORDER_URL+"/order/create";
		String json;
		try {
			json = MAPPER.writeValueAsString(order);
			String orderId = httpClientService.doPostJson(url, json);
			orderId = orderId.replaceAll("\"", "");
			return Long.parseLong(orderId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Order queryByOrderId(String orderId) {
		String url = ORDER_URL+"/order/query/"+orderId;
		try {
			String jsonData = httpClientService.doGet(url);
			return MAPPER.readValue(jsonData, Order.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
