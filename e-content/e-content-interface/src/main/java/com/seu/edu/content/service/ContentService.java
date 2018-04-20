package com.seu.edu.content.service;

import java.util.List;

import com.seu.edu.common.utils.EResult;
import com.seu.edu.pojo.TbContent;

public interface ContentService {

	EResult addContent(TbContent tbContent);
	List<TbContent> getContentListByCid(long cid);
}
