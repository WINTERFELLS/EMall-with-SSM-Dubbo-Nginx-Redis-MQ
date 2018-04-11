package com.seu.edu.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seu.edu.common.pojo.EasyUITreeNode;
import com.seu.edu.mapper.TbItemCatMapper;
import com.seu.edu.pojo.TbItemCat;
import com.seu.edu.pojo.TbItemCatExample;
import com.seu.edu.pojo.TbItemCatExample.Criteria;
import com.seu.edu.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	@Override
	public List<EasyUITreeNode> getItemCatList(long parentId) {
		
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		
		List<EasyUITreeNode> resultList = new ArrayList<>();
		
		for(TbItemCat tbItemCat : list) {
			EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
			
			easyUITreeNode.setId(tbItemCat.getId());
			easyUITreeNode.setText(tbItemCat.getName());
			easyUITreeNode.setState(tbItemCat.getIsParent() ? "closed" : "open" );
			resultList.add(easyUITreeNode);
		}
		return resultList;
	}

}
