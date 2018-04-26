package com.seu.edu.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seu.edu.common.pojo.SearchResult;
import com.seu.edu.search.dao.SearchDao;
import com.seu.edu.search.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchDao searchDao;
	
	@Override
	public SearchResult search(String keyword, int page, int rows) throws Exception {
		SolrQuery solrQuery = new SolrQuery();
		
		solrQuery.setQuery(keyword);
		
		if(page <= 0) {
			page = 1;
		}
		solrQuery.setStart((page-1)*rows);
		solrQuery.setRows(rows);
		
		solrQuery.set("df", "item_title");
		
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
		solrQuery.setHighlightSimplePost("</em>");
		
		SearchResult searchResult = searchDao.search(solrQuery);

		long recordCount = searchResult.getRecordCount();
		int totalPage = (int) (recordCount/rows);
		if(recordCount % rows > 0) {
			totalPage++;
		}
		searchResult.setPageCount(totalPage);
		return searchResult;
	}

}
