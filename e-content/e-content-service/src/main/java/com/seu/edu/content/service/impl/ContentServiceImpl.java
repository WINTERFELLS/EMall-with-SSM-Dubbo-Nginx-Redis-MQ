package com.seu.edu.content.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seu.edu.common.utils.EResult;
import com.seu.edu.content.service.ContentService;
import com.seu.edu.mapper.TbContentMapper;
import com.seu.edu.pojo.TbContent;
import com.seu.edu.pojo.TbContentExample;
import com.seu.edu.pojo.TbContentExample.Criteria;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	
	@Override
	public EResult addContent(TbContent tbContent) {
		tbContent.setCreated(new Date());
		tbContent.setUpdated(new Date());
		
		contentMapper.insert(tbContent);
		return EResult.ok();
	}

	@Override
	public List<TbContent> getContentListByCid(long cid) {
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(cid);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		return list;
	}

}
