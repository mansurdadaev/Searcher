package com.example.searcher.searchs.google.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "google-xml-client", url = "${search-clients.google.url}")
public interface GoogleClient {

	@RequestMapping(method = RequestMethod.GET, value = "/search/xml")
	String sendRequest(
		@RequestParam String user,
		@RequestParam String key,
		@RequestParam String query,
		@RequestParam String i10n,
		@RequestParam String sortBy,
		@RequestParam String filter,
		@RequestParam String groupBy);
}
