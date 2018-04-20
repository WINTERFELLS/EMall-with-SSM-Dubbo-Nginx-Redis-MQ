package com.seu.edu.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seu.edu.common.pojo.EasyUITreeNode;
import com.seu.edu.common.utils.EResult;
import com.seu.edu.content.service.ContentCategoryService;
import com.seu.edu.mapper.TbContentCategoryMapper;
import com.seu.edu.pojo.TbContentCategory;
import com.seu.edu.pojo.TbContentCategoryExample;
import com.seu.edu.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	@Override
	public List<EasyUITreeNode> getContentCatList(long parentId) {

		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> catList = contentCategoryMapper.selectByExample(example);
		
		List<EasyUITreeNode> nodeList = new ArrayList<>();
		for(TbContentCategory contentCategory : catList) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(contentCategory.getId());
			node.setText(contentCategory.getName());
			node.setState(contentCategory.getIsParent() ? "closed" : "open");
			nodeList.add(node);
		}
		return nodeList;
	}

	@Override
	public EResult addContentCategory(long parentId, String name) {
		TbContentCategory contentCategory = new TbContentCategory();
		
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		contentCategory.setStatus(1);
		contentCategory.setSortOrder(1);
		contentCategory.setIsParent(false);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		
		contentCategoryMapper.insert(contentCategory);
		
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parent.getIsParent()) {
			parent.setIsParent(true);
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		
		return EResult.ok(contentCategory);
	}

	@Override
	public EResult deleteContentCategory(long id) {
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		long parentId = contentCategory.getParentId();
		
		TbContentCategory parentContentCategory = contentCategoryMapper.selectByPrimaryKey(parentId);
		
		contentCategoryMapper.deleteByPrimaryKey(id);
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		int count = contentCategoryMapper.countByExample(example);
		if(count == 0) {
			parentContentCategory.setIsParent(false);
			contentCategoryMapper.updateByPrimaryKey(parentContentCategory);
		}
		
		return EResult.ok();
	}

	@Override
	public EResult updateContentCategory(long id, String name) {
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		contentCategory.setName(name);
		contentCategory.setUpdated(new Date());
		contentCategoryMapper.updateByPrimaryKey(contentCategory);
		return EResult.ok();
	}

}
