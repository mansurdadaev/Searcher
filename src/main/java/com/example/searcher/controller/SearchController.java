package com.example.searcher.controller;

import com.example.searcher.api.SearchApi;
import com.example.searcher.dto.DomainItem;
import com.example.searcher.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/search")
public class SearchController implements SearchApi {

	private final SearchService searchService;

	@GetMapping
	@Override
	public ResponseEntity<List<DomainItem>> search(List<String> queries) {
		queries.forEach(searchService::search);
		return ResponseEntity.ok(searchService.getDomainFrequency());
	}
}
