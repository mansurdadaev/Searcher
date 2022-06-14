package com.example.searcher.service;

import com.example.searcher.dto.DomainItem;
import java.util.List;

public interface SearchService {

	void search(String searchWord);

	List<DomainItem> getDomainFrequency();
}
