package com.jt.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.common.util.CookieUtils;
import com.jt.web.pojo.Cart;
import com.jt.web.pojo.User;
import com.jt.web.service.CartService;
import com.jt.web.threadlocal.UserThreadLocal;

@Controller
@RequestMapping("/cart")
public class CartController {
	@Autowired
	private CartService cartService;
	
	//转向购物车页面	http://www.jt.com/cart/show.html
	@RequestMapping("/show")
	public String show(HttpServletRequest request, Model model){
		User _user = UserThreadLocal.get();
		List<Cart> cartList = cartService.queryList(_user.getId());
		model.addAttribute("cartList", cartList);
		
		return "cart";	//cart.jsp
	}
	
	//加入商品到购物车	http://www.jt.com/cart/add/931849.html
	@RequestMapping("/add/{itemId}")
	public String add(HttpServletRequest request, @PathVariable Long itemId){
		User _user = UserThreadLocal.get();
		
		cartService.save(_user.getId(), itemId);
		return "redirect:/cart/show.html";
	}
	
	//某个商品数量更新	http://www.jt.com/service/cart/update/num/927779/4
	@RequestMapping("/update/num/{itemId}/{num}")
	public String update(HttpServletRequest request,@PathVariable Long itemId,@PathVariable Integer num){
		//先获取cookie中值
		String ticket = CookieUtils.getCookieValue(request, UserController.cookieName);
		//判断如果没有登录，直接转向登录页面
		if(StringUtils.isEmpty(ticket)){
			return "redirect:/user/login.html";
		}		
		cartService.update(ticket, itemId, num);
		return "redirect:/cart/show.html";
	}
	
	//删除商品	http://www.jt.com/cart/delete/21.html
	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable Long id){
		cartService.delete(id);
		return "redirect:/cart/show.html";
	}
}
