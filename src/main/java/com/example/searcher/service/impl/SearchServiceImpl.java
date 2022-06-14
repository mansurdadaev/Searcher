package com.example.searcher.service.impl;

import com.example.searcher.dto.DomainItem;
import com.example.searcher.searchs.SearchEngine;
import com.example.searcher.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SearchServiceImpl implements SearchService {

	private final SearchEngine searchEngine;

	private final Map<String, List<String>> cacheOfDomainItems = new HashMap<>();

	@Override
	public void search(String searchWord) {

		log.info("Searching word: {}", searchWord);

		List<String> domainList = searchEngine.getDomainList(searchWord);
		cacheOfDomainItems.put(searchWord, domainList);
	}

	@Override
	public List<DomainItem> getDomainFrequency() {

		return cacheOfDomainItems.values().stream()
			.flatMap(Collection::stream)
			.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
			.entrySet().stream()
			.map(this::toDomainItem)
			.collect(Collectors.toList());
	}

	private DomainItem toDomainItem(Entry<String, Long> it) {
		DomainItem domainItem = new DomainItem();
		domainItem.setDomain(it.getKey());
		domainItem.setFrequency(it.getValue());
		return domainItem;
	}
}
