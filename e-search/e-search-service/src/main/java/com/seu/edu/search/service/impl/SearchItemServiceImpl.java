package com.seu.edu.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seu.edu.common.pojo.SearchItem;
import com.seu.edu.common.utils.EResult;
import com.seu.edu.search.mapper.ItemMapper;
import com.seu.edu.search.service.SearchItemService;

@Service
public class SearchItemServiceImpl implements SearchItemService {

	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;
	
	@Override
	public EResult importAllItems() {
		try {
			List<SearchItem> itemList = itemMapper.getItemList();
			for (SearchItem serachItem : itemList) {
				SolrInputDocument solrInputDocument = new SolrInputDocument();
				solrInputDocument.addField("id", serachItem.getId());
				solrInputDocument.addField("item_title", serachItem.getTitle());
				solrInputDocument.addField("item_sell_point", serachItem.getSell_point());
				solrInputDocument.addField("item_price", serachItem.getPrice());
				solrInputDocument.addField("item_image", serachItem.getImage());
				solrInputDocument.addField("item_category_name", serachItem.getCategory_name());
			
				solrServer.add(solrInputDocument);
			}
			solrServer.commit();
			return EResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return EResult.build(500, "数据导入异常");
		}
	}

}
