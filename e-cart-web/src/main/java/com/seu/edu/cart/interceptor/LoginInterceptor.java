package com.seu.edu.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.seu.edu.common.utils.CookieUtils;
import com.seu.edu.common.utils.EResult;
import com.seu.edu.pojo.TbUser;
import com.seu.edu.sso.service.TokenService;

public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private TokenService tokenService;
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		String token = CookieUtils.getCookieValue(request, "token");
		if(StringUtils.isBlank(token)) {
			return true;
		}
		
		EResult result = tokenService.getUserByToken(token);
		if(result.getStatus() != 200) {
			return true;
		}
		
		TbUser user = (TbUser) result.getData();
		
		request.setAttribute("user", user);
		
		return true;
	}

}
