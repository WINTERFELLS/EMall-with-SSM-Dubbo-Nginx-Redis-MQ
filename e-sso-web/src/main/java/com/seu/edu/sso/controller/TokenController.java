package com.seu.edu.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seu.edu.common.utils.EResult;
import com.seu.edu.sso.service.TokenService;

@Controller
public class TokenController {

	@Autowired
	private TokenService tokenService;
	
	@RequestMapping("/user/token/{token}")
	@ResponseBody
	public EResult getUserByToken(@PathVariable String token) {
		EResult result = tokenService.getUserByToken(token);
		return result;
	}
}
