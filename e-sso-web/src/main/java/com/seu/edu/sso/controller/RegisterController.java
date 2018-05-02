package com.seu.edu.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seu.edu.common.utils.EResult;
import com.seu.edu.pojo.TbUser;
import com.seu.edu.sso.service.RegisterService;

@Controller
public class RegisterController {
	
	@Autowired
	private RegisterService registerService;

	@RequestMapping("page/register")
	public String showRegister() {
		return "register";
	}
	
	@RequestMapping("/page/register/{param}/{type}")
	@ResponseBody
	public EResult checkDate(@PathVariable String param,  @PathVariable Integer type) {
		EResult eResult = registerService.checkDate(param, type);
		return eResult;
	}
	
	@RequestMapping(value="/user/register", method=RequestMethod.POST)
	@ResponseBody
	public EResult register(TbUser user) {
		return registerService.register(user);
	}
}
