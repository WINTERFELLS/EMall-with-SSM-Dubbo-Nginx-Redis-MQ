package com.seu.edu.search.service;

import com.seu.edu.common.pojo.SearchResult;

public interface SearchService {

	SearchResult search(String keyword, int page, int rows) throws Exception;
}
