package com.seu.edu.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seu.edu.common.pojo.SearchItem;
import com.seu.edu.common.pojo.SearchResult;

@Repository
public class SearchDao {	
	
	@Autowired
	private SolrServer solrServer;
	
	public SearchResult search(SolrQuery query) throws Exception {
		
		QueryResponse queryResponse = solrServer.query(query);
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		
		SearchResult result = new SearchResult();
		
		long numFound = solrDocumentList.getNumFound();
		result.setRecordCount(numFound);
		
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		
		List<SearchItem> itemList = new ArrayList<>();
		for (SolrDocument solrDocument : solrDocumentList) {
			SearchItem searchItem = new SearchItem();
			searchItem.setId((String) solrDocument.get("id"));
			searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
			searchItem.setImage((String) solrDocument.get("item_image"));
			searchItem.setPrice((long) solrDocument.get("item_price"));
			searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
			
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String title = "";
			if(list != null && list.size()>0) {
				title = list.get(0);
			}else {
				title = (String) solrDocument.get("item_title");
			}
			searchItem.setTitle(title);
			itemList.add(searchItem);
		}
		result.setItemList(itemList);
		
		return result;
	}
	
}
