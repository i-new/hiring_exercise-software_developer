package com.cyan.amescua.controller;

import com.cyan.amescua.services.FeedService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FeedController.class)
class FeedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FeedService feedService;

    @Test
    void should_Retrieve_RSS_and_then_sayOk() throws Exception {
        mockMvc.perform(get("/analyse/new")
            .contentType("application/json")
            .param("url1", "https://news.google.com/rss?cf=all&hl=en-US&pz=1&gl=US&ceid=US:en")
            .param("url2", "https://www.api.org/news-policy-and-issues/news-feed")
            .param("url3", "http://feeds.bbci.co.uk/news/rss.xml"))
            .andExpect(status().isOk());
    }

    @Test
    void should_Retrieve_RSS_and_then_sayNotAcceptable() throws Exception {
        mockMvc.perform(get("/analyse/new")
                .contentType("application/json")
                .param("url1", "https://news.google.com/rss?cf=all&hl=en-US&pz=1&gl=US&ceid=US:en"))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void retrieveResults() throws Exception {
        mockMvc.perform(get("/frequency/1")
                .contentType("application/json"))
                .andExpect(status().isNotFound());
    }
}