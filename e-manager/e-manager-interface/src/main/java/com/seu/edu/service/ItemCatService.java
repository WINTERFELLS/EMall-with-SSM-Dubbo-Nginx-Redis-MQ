package com.seu.edu.service;

import java.util.List;

import com.seu.edu.common.pojo.EasyUITreeNode;

public interface ItemCatService {

	List<EasyUITreeNode> getItemCatList(long parentId);
}
