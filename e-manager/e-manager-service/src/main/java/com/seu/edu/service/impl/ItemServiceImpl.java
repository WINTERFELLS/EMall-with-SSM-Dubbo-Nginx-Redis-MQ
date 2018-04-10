package com.seu.edu.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seu.edu.mapper.TbItemMapper;
import com.seu.edu.pojo.TbItem;
import com.seu.edu.pojo.TbItemExample;
import com.seu.edu.pojo.TbItemExample.Criteria;
import com.seu.edu.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	
	@Override
	public TbItem getItemById(long itemId) {
		
		//根据主键查询
		//TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		//return tbItem;
		
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andIdEqualTo(itemId);
		//执行查询
		List<TbItem> list = itemMapper.selectByExample(example);
		if(list != null && list.size() > 0) {
			return list.get(0);
		}
		
		return null;
	}

}
