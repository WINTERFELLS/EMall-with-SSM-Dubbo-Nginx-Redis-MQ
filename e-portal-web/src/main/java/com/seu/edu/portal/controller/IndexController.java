package com.seu.edu.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.seu.edu.content.service.ContentService;
import com.seu.edu.pojo.TbContent;

@Controller
public class IndexController {

	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/index")
	public String showIndex(Model model) {
		
		List<TbContent> ad1List = contentService.getContentListByCid(89);
		model.addAttribute("", ad1List);
		return "index";
	}
}
