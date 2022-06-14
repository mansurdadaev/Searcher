package com.example.searcher.controller;

import com.example.searcher.service.SearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@SpringBootTest
public class SearchControllerTest {

  private static final String SEARCH_PATH = "/search";
  private static final ObjectMapper mapper = new ObjectMapper();

  private MockMvc mockMvc;

  @Mock
  private SearchService searchService;

  @InjectMocks
  private SearchController searchController;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(searchController).build();
  }

  @Test
  void whenReturnOk() throws Exception {
    mockMvc.perform(
        get(SEARCH_PATH)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .param("query", "yandex")
    ).andExpect(status().isOk());
  }
}
