package com.seu.edu.content.service;

import java.util.List;

import com.seu.edu.common.pojo.EasyUITreeNode;
import com.seu.edu.common.utils.EResult;

public interface ContentCategoryService {

	List<EasyUITreeNode> getContentCatList(long parentId);
	EResult addContentCategory(long parentId, String name);
	EResult deleteContentCategory(long id);
	EResult updateContentCategory(long id, String name);

}
