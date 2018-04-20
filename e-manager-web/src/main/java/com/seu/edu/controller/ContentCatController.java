package com.seu.edu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seu.edu.common.pojo.EasyUITreeNode;
import com.seu.edu.common.utils.EResult;
import com.seu.edu.content.service.ContentCategoryService;

@Controller
public class ContentCatController {

	@Autowired
	private ContentCategoryService contentCategoryService;
	
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatList(@RequestParam(name="id", defaultValue="0")Long parentId){
		List<EasyUITreeNode> list = contentCategoryService.getContentCatList(parentId);
		return list;
	}
	
	@RequestMapping(value="/content/category/create", method=RequestMethod.POST)
	@ResponseBody
	public EResult createContentCategory(Long parentId, String name) {
		EResult eResult = contentCategoryService.addContentCategory(parentId, name);
		return eResult;
	}
	
	@RequestMapping("/content/category/delete")
	@ResponseBody
	public EResult deleteContentCategory(Long id) {
		return contentCategoryService.deleteContentCategory(id);
	}
	
	@RequestMapping(value="/content/category/update", method=RequestMethod.POST)
	@ResponseBody
	public EResult updateContentCategory(Long id, String name) {
		EResult eResult = contentCategoryService.updateContentCategory(id, name);
		return eResult;
	}
}
