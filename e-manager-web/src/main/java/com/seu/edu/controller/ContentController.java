package com.seu.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seu.edu.common.utils.EResult;
import com.seu.edu.content.service.ContentService;
import com.seu.edu.pojo.TbContent;

@Controller
public class ContentController {
	
	@Autowired
	private ContentService contentService;

	@RequestMapping(value="/content/save", method=RequestMethod.POST)
	@ResponseBody
	public EResult addContent(TbContent content) {
		return contentService.addContent(content);
	}
}
