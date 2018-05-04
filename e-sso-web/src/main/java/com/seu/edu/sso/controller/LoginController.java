package com.seu.edu.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seu.edu.common.utils.CookieUtils;
import com.seu.edu.common.utils.EResult;
import com.seu.edu.sso.service.LoginService;

public class LoginController {

	@Autowired
	private LoginService loginService;
	
	@RequestMapping("page/login")
	public String showLogin(String redirect, Model model) {
		model.addAttribute("redirect",redirect );
		return "login";
	}
	
	@RequestMapping(value="/user/login", method=RequestMethod.POST)
	@ResponseBody
	public EResult login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
		EResult result = loginService.userLogin(username, password);
		if(result.getStatus() == 200) {
			String token = result.getData().toString();
			CookieUtils.setCookie(request, response, "token", token);
		}
		return result;
	}
}
