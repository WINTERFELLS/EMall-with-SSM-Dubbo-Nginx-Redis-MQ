package com.seu.edu.service;

import com.seu.edu.common.pojo.EasyUIDataGridResult;
import com.seu.edu.common.utils.EResult;
import com.seu.edu.pojo.TbItem;
import com.seu.edu.pojo.TbItemDesc;

public interface ItemService {
	TbItem getItemById(long itemId);
	EasyUIDataGridResult getItemList(int page, int rows);
	EResult addItem(TbItem item, String desc);
	TbItemDesc getItemDescById(long itemId);
}
