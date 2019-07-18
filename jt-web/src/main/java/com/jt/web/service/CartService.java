package com.jt.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.service.RedisService;
import com.jt.common.spring.exetend.PropertyConfig;
import com.jt.web.pojo.Cart;
import com.jt.web.pojo.User;

@Service
public class CartService {
	@Autowired
	private RedisService redisService;
	@Autowired
	private HttpClientService httpClientService;
	@PropertyConfig
	private String CART_URL;
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	public List<Cart> queryList(Long userId) {
		try {
			
			String url = CART_URL+"/cart/query/" + userId;
			
			String jsonListData = httpClientService.doGet(url);
			List<Cart> cartList = MAPPER.readValue(jsonListData,
					MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
			return cartList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void save(Long userId, Long itemId) {
		String url = CART_URL+"/cart/save";
		Map<String,String> params = new HashMap<String,String>();
		params.put("userId", String.valueOf(userId));
		params.put("itemId", String.valueOf(itemId));
		
		try {
			httpClientService.doPost(url, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update(String ticket, Long itemId, Integer num) {
		//"/update/num/{userId}/{itemId}/{num}";
		String url = CART_URL+"/cart/update/num/"+this.getUserId(ticket)+"/"+itemId+"/"+num;
		
		try {
			httpClientService.doGet(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//根据id删除购物车中的商品	http://cart.jt.com/cart/delete/{id}
	public void delete(Long id) {
		String url = CART_URL+"/cart/delete/"+id;
		
		try {
			httpClientService.doGet(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//获取当前用户ID
	public String getUserId(String ticket){
		String jsonData = redisService.get(ticket);
		User curUser = null;
		try {
			curUser = MAPPER.readValue(jsonData, User.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return String.valueOf(curUser.getId());
	}


}
