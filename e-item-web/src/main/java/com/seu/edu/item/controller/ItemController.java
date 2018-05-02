package com.seu.edu.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.seu.edu.item.pojo.Item;
import com.seu.edu.pojo.TbItem;
import com.seu.edu.pojo.TbItemDesc;
import com.seu.edu.service.ItemService;

@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	public String showItemInfo(@PathVariable Long itemId, Model model) {
		TbItem tbItem = itemService.getItemById(itemId);
		Item item = new Item(tbItem);
		
		TbItemDesc itemDesc = itemService.getItemDescById(itemId);
		
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		
		return "item";
	}
}
