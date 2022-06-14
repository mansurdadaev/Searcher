package com.example.searcher.searchs.yandex.service;

import com.example.searcher.searchs.SearchEngine;
import com.example.searcher.searchs.yandex.api.YandexClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "search-clients", name = "default-searcher", havingValue = "yandex")
public class YandexService implements SearchEngine {

	@Value("${search-clients.yandex.user}")
	private String user;
	@Value("${search-clients.yandex.key}")
	private String key;

	private static final String I10N = "ru";
	private static final String SORT_BY = "tm.order ascending";
	private static final String FILTER = "strict";
	private static final String GROUP_BY = "attr mode - flat.groups-on-page D10.docs-in-group D1";


	private final YandexClient yandexClient;

	@Override
	public List<String> getDomainList(String searchWord) {

		log.info("Searching word on Yandex: {}", searchWord);

		String response = sendRequest(searchWord);
		return YandexXMLParser.getDomainList(response);
	}

	private String sendRequest(String searchWord) {

		return yandexClient.sendRequest(
			user,
			key,
			searchWord,
			I10N,
			SORT_BY,
			FILTER,
			GROUP_BY);
	}
}
